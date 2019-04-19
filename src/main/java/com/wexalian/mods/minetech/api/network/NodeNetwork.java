package com.wexalian.mods.minetech.api.network;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class NodeNetwork<T extends INetworkNode<T, X>, X>
{
    private final Set<T> nodes;
    private boolean failed;
    
    public NodeNetwork(Set<T> nodes, boolean failed)
    {
        this.nodes = ImmutableSet.<T>builder().addAll(nodes).build();
        this.failed = failed;
    }
    
    public boolean hasFailed()
    {
        return failed;
    }
    
    public void forEachNode(BiConsumer<T, X> consumer)
    {
        nodes.forEach(n -> consumer.accept((T) n, n.getValue()));
    }
    
    public void putNetworkChunks(Table<World, ChunkPos, T> networkChunks)
    {
        for (T node : nodes)
        {
            networkChunks.put(node.getHost().getWorld(), node.getHost().getChunkPos(), node);
        }
    }
    
    public static <T extends INetworkNode<T, X>, X> Set<NodeNetwork<T, X>> getNetworks(Set<T> nodes, BiPredicate<World, BlockPos> posValidator, X defaultValue)
    {
        Set<NodeNetwork<T, X>> networks = new HashSet<>();
        
        Queue<T> queue = new ArrayDeque<>(nodes);
        
        while (!queue.isEmpty())
        {
            T node = queue.poll();
            if (node.isVisited()) continue;
            
            NodeNetwork<T, X> network = getNetwork(node, posValidator, defaultValue);
            networks.add(network);
        }
        
        return networks;
    }
    
    public static <T extends INetworkNode<T, X>, X> NodeNetwork<T, X> getNetwork(T startNode, BiPredicate<World, BlockPos> posValidator, X defaultValue)
    {
        Object2ObjectMap<T, X> nodes = new Object2ObjectOpenHashMap<>();
        
        AtomicBoolean failed = new AtomicBoolean(false);
        
        Queue<Pair<T, X>> queue = new ArrayDeque<>();
        queue.add(Pair.of(startNode, defaultValue));
        
        while (!queue.isEmpty())
        {
            Pair<T, X> pair = queue.poll();
            if (pair != null)
            {
                X prevVal = nodes.put(pair.getLeft(), pair.getRight());
                
                if (prevVal == null)
                {
                    pair.getLeft().setValue(pair.getRight());
                    pair.getLeft().getHost().addNeighbours((node, val) -> queue.add(Pair.of(node, val)), posValidator);
                }
                else
                {
                    if (!(prevVal.equals(pair.getRight())))
                    {
                        pair.getLeft().setValue(null);
                        failed.set(true);
                    }
                }
                pair.getLeft().markVisited();
            }
        }
        return new NodeNetwork<>(nodes.keySet(), failed.get());
    }
    
    public static <T extends INetworkNode<T, X>, X> void visitNetwork(T startNode, BiPredicate<World, BlockPos> posValidator)
    {
        Queue<T> activeQueue = new ArrayDeque<>();
        activeQueue.add(startNode);
        while (!activeQueue.isEmpty())
        {
            T current = activeQueue.poll();
            if (current != null)
            {
                current.markVisited();
                current.getHost().addNeighbours((node, ratio) -> {
                    if (node != null && !(node).isVisited())
                    {
                        activeQueue.add(node);
                    }
                }, posValidator);
            }
        }
    }
}
