package wexalian.mods.minetech.kinesis;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;
import com.google.common.util.concurrent.AtomicDouble;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wexalian.mods.minetech.MineTech;
import wexalian.mods.minetech.network.PacketKineticUpdate;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;

public enum ServerKineticManager implements KineticManager
{
    INSTANCE;
    
    private final Set<IKineticNode> nodes = new HashSet<>();
    
    public void add(IKineticNode node)
    {
        nodes.add(node);
    }
    
    public void remove(IKineticNode node)
    {
        nodes.remove(node);
    }
    
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        
        nodes.forEach(IKineticNode::resetVisitState);
        
        Table<World, ChunkPos, Set<IKineticNode>> allChunks = HashBasedTable.create();
        Table<World, ChunkPos, IKineticNode> networkChunks = HashBasedTable.create();
        
        Queue<IKineticNode> queue = new ArrayDeque<>(nodes);
        while (!queue.isEmpty())
        {
            IKineticNode activeNode = Objects.requireNonNull(queue.poll());
            if (activeNode.isVisited()) continue;
            
            TObjectFloatMap<IKineticNode> nodes = new TObjectFloatHashMap<>(16, 0.75F, Float.NaN);
            
            if (findNetwork(nodes, activeNode, World::isBlockLoaded)) computeNetwork(nodes);
            else nodes.keySet().forEach(node -> node.update(0, 0, 0));
            
            for (IKineticNode node : nodes.keySet()) networkChunks.put(node.getWorld(), node.getChunkPos(), node);
            
            for (World world : networkChunks.rowKeySet())
            {
                networkChunks.row(world).forEach((chunkPos, node) -> {
                    Set<IKineticNode> nodeSet = allChunks.get(world, chunkPos);
                    if (nodeSet == null) allChunks.put(world, chunkPos, nodeSet = new HashSet<>());
                    nodeSet.add(node);
                });
            }
            networkChunks.clear();
        }
        
        nodes.forEach(IKineticNode::resetVisitState);
        
        Multimap<EntityPlayer, ChunkPos> playerChunks = MultimapBuilder.hashKeys().hashSetValues().build();
        
        for (World world : allChunks.rowKeySet())
        {
            Map<ChunkPos, Set<IKineticNode>> map = allChunks.row(world);
            PlayerChunkMap chunkMap = ((WorldServer) world).getPlayerChunkMap();
            for (ChunkPos chunkPos : map.keySet())
            {
                PlayerChunkMapEntry entry = chunkMap.getEntry(chunkPos.x, chunkPos.z);
                if (entry != null && entry.isSentToPlayers())
                {
                    for (EntityPlayer player : world.playerEntities)
                    {
                        if (entry.containsPlayer((EntityPlayerMP) player))
                        {
                            playerChunks.put(player, chunkPos);
                        }
                    }
                }
            }
        }
        
        for (EntityPlayer player : playerChunks.keySet())
        {
            Set<IKineticNode> updates = new HashSet<>();
            BiPredicate<World, BlockPos> posValidator = (world, pos) -> world.isBlockLoaded(pos) && playerChunks.containsEntry(player, new ChunkPos(pos));
            queue = new ArrayDeque<>();
            
            for (ChunkPos pos : playerChunks.get(player)) queue.addAll(allChunks.get(player.world, pos));
            
            queue.forEach(IKineticNode::resetVisitState);
            
            while (!queue.isEmpty())
            {
                IKineticNode node = Objects.requireNonNull(queue.poll());
                if (!node.isVisited())
                {
                    visitNetwork(node, posValidator);
                    updates.add(node);
                }
            }
            
            MineTech.networkManager.sendTo(new PacketKineticUpdate(updates), (EntityPlayerMP) player);
        }
    }
    
    private void computeNetwork(TObjectFloatMap<IKineticNode> nodeMap)
    {
        AtomicDouble systemPower = new AtomicDouble(0);
        AtomicDouble appliedPower = new AtomicDouble(0);
        AtomicDouble consumedPower = new AtomicDouble(0);
        AtomicDouble totalInertia = new AtomicDouble(0);
        AtomicInteger direction = new AtomicInteger(0);
        
        nodeMap.forEachEntry((node, ratio) -> {
            systemPower.addAndGet(node.getPower());
            appliedPower.addAndGet(Math.copySign(node.getAppliedPower(), ratio));
            consumedPower.addAndGet(node.getConsumedPower());
            totalInertia.addAndGet(node.getInertia() * ratio * ratio);
            float vel = node.getVelocity();
            direction.addAndGet(vel == 0 ? 0 : (ratio >= 0 ? 1 : -1) * (vel > 0 ? 1 : -1));
            return true;
        });
        
        if (direction.get() < 0) systemPower.set(-systemPower.get());
        
        float pow = (float) (systemPower.get() + appliedPower.get());
        float totalPower = (float) Math.copySign(Math.max(0, Math.abs(pow) - consumedPower.get()), pow);
        
        if (totalPower == 0)
        {
            nodeMap.keySet().forEach(node -> node.update(0, 0, 0));
            return;
        }
        
        float totalVelocitySq = (float) (Math.abs(totalPower) / totalInertia.get());
        float totalVelocity = (float) Math.copySign(Math.sqrt(totalVelocitySq), totalPower);
        
        nodeMap.forEachEntry((node, ratio) -> {
            float power = node.getInertia() * totalVelocitySq / (ratio * ratio);
            float velocity = totalVelocity / ratio;
            float torque = power / velocity;
            
            node.update(torque, velocity, power);
            return true;
        });
    }
}
