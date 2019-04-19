package com.wexalian.mods.minetech.energy.kinetic;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;
import com.google.common.util.concurrent.AtomicDouble;
import com.wexalian.mods.minetech.api.MineTechAPI;
import com.wexalian.mods.minetech.api.energy.kinetic.IKineticNode;
import com.wexalian.mods.minetech.api.network.INetworkNode;
import com.wexalian.mods.minetech.api.network.NodeNetwork;
import com.wexalian.mods.minetech.network.MessageKineticUpdate;
import com.wexalian.mods.minetech.network.NetworkHandler;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;

public class KineticManager
{
    public static final KineticManager INSTANCE = new KineticManager();
    public final Object2ObjectMap<UUID, Float> clientUpdates = new Object2ObjectOpenHashMap<>();
    
    private final Set<IKineticNode> serverNodes = new HashSet<>();
    private final Set<IKineticNode> clientNodes = new HashSet<>();
    
    private KineticManager()
    {
        MineTechAPI.Kinesis.INSTANCE.setNodeConsumers(this::add, this::remove);
    }
    
    private void add(IKineticNode node, boolean isRemote)
    {
        if (isRemote) clientNodes.add(node);
        else serverNodes.add(node);
        
        node.setRemote(isRemote);
    }
    
    private void remove(IKineticNode node)
    {
        if (node.isRemote()) clientNodes.remove(node);
        else serverNodes.remove(node);
    }
    
    @SuppressWarnings({"Duplicates", "UnstableApiUsage"})
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        
        serverNodes.forEach(INetworkNode::resetVisitState);
        
        Table<World, ChunkPos, Set<IKineticNode>> allChunks = HashBasedTable.create();
        Table<World, ChunkPos, IKineticNode> networkChunks = HashBasedTable.create();
        
        Set<NodeNetwork<IKineticNode, Float>> networks = NodeNetwork.getNetworks(serverNodes, World::isBlockLoaded, 1F);
        
        for (NodeNetwork<IKineticNode, Float> network : networks)
        {
            if (!network.hasFailed()) computeServerNetwork(network);
            else network.forEachNode((node, ratio) -> node.update(0, 0, 0));
            
            network.putNetworkChunks(networkChunks);
            
            for (World world : networkChunks.rowKeySet())
            {
                networkChunks.row(world).forEach((pos, node) -> {
                    Set<IKineticNode> nodes = allChunks.get(world, pos);
                    if (nodes == null) allChunks.put(world, pos, nodes = new HashSet<>());
                    nodes.add(node);
                });
            }
            networkChunks.clear();
        }
        
        serverNodes.forEach(INetworkNode::resetVisitState);
        
        Multimap<EntityPlayer, ChunkPos> playerChunks = MultimapBuilder.hashKeys().hashSetValues().build();
        
        for (World world : allChunks.rowKeySet())
        {
            Map<ChunkPos, Set<IKineticNode>> map = allChunks.row(world);
            PlayerChunkMap chunkMap = ((WorldServer) world).getPlayerChunkMap();
            
            for (ChunkPos chunkPos : map.keySet())
            {
                PlayerChunkMapEntry entry = chunkMap.getEntry(chunkPos.x, chunkPos.z);
                if (entry != null)
                {
                    for (EntityPlayer player : entry.getWatchingPlayers())
                    {
                        playerChunks.put(player, chunkPos);
                    }
                }
            }
        }
        
        for (EntityPlayer player : playerChunks.keySet())
        {
            Object2ObjectMap<UUID, Float> updates = new Object2ObjectOpenHashMap<>();
            BiPredicate<World, BlockPos> posValidator = (world, pos) -> world.isBlockLoaded(pos) && playerChunks.containsEntry(player, new ChunkPos(pos));
            Queue<IKineticNode> queue = new ArrayDeque<>();
            
            for (ChunkPos pos : playerChunks.get(player)) queue.addAll(allChunks.get(player.world, pos));
            
            queue.forEach(IKineticNode::resetVisitState);
            
            while (!queue.isEmpty())
            {
                IKineticNode node = Objects.requireNonNull(queue.poll());
                if (!node.isVisited())
                {
                    NodeNetwork.visitNetwork(node, posValidator);
                    updates.put(node.getNodeId(), node.getVelocity());
                }
            }
            NetworkHandler.CHANNEL.sendTo(new MessageKineticUpdate(updates), ((EntityPlayerMP) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }
    
    @SuppressWarnings("Duplicates")
    private void computeServerNetwork(NodeNetwork<IKineticNode, Float> network)
    {
        AtomicDouble systemPower = new AtomicDouble(0);
        AtomicDouble generatedPower = new AtomicDouble(0);
        AtomicDouble consumedPower = new AtomicDouble(0);
        AtomicDouble totalInertia = new AtomicDouble(0);
        AtomicInteger direction = new AtomicInteger(0);
        
        network.forEachNode((node, ratio) -> {
            systemPower.addAndGet(node.getPower());
            generatedPower.addAndGet(Math.copySign(node.getAppliedPower(), ratio));
            consumedPower.getAndAdd(node.getConsumedPower());
            totalInertia.addAndGet(node.getInertia());
            float vel = node.getVelocity();
            direction.addAndGet(vel == 0 ? 0 : (ratio >= 0 ? 1 : -1) * (vel > 0 ? 1 : -1));
        });
        
        if (direction.get() < 0) systemPower.set(-systemPower.get());
        
        float totalPower = (float) (systemPower.get() + generatedPower.get());
        float power = (float) Math.copySign(Math.max(0, Math.abs(totalPower) - consumedPower.get() - totalInertia.get()), totalPower);
        
        if (power == 0)
        {
            network.forEachNode((node, ratio) -> node.update(0, 0, 0));
            return;
        }
        
        float totalVelocitySq = (float) (Math.abs(power) / totalInertia.get());
        float totalVelocity = (float) Math.copySign(Math.sqrt(totalVelocitySq), power);
        
        network.forEachNode((node, ratio) -> {
            float nPower = node.getInertia() * totalVelocitySq / (ratio * ratio);
            float nVelocity = totalVelocity / ratio;
            float nTorque = nPower / nVelocity;
            
            node.update(nTorque, nVelocity, nPower);
        });
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        
        clientNodes.forEach(IKineticNode::resetVisitState);
        
        Set<NodeNetwork<IKineticNode, Float>> networks = NodeNetwork.getNetworks(clientNodes, World::isBlockLoaded, 1F);
        
        for (NodeNetwork<IKineticNode, Float> network : networks)
        {
            if (!network.hasFailed())
            {
                network.forEachNode((node, ratio) -> {
                    Float update = clientUpdates.getOrDefault(node.getNodeId(), Float.NaN);
                    if (Float.isNaN(update)) return;
                    
                    computeClientNetwork(network, update);
                });
            }
            else network.forEachNode((node, ratio) -> node.update(Float.NaN, 0, Float.NaN));
        }
        
        clientUpdates.clear();
    }
    
    private void computeClientNetwork(NodeNetwork<IKineticNode, Float> network, float totalVelocity)
    {
        network.forEachNode((node, ratio) -> node.update(Float.NaN, totalVelocity / ratio, Float.NaN));
    }
    
}

