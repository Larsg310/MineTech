package wexalian.mods.minetech.kinesis;

import gnu.trove.map.TObjectFloatMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

public enum ClientKineticManager implements KineticManager
{
    INSTANCE;
    
    private final Set<IKineticNode> nodes = new HashSet<>();
    public final Object2FloatMap<UUID> clientUpdates = new Object2FloatOpenHashMap<>();
    
    public void add(IKineticNode node)
    {
        nodes.add(node);
    }
    
    public void remove(IKineticNode node)
    {
        nodes.remove(node);
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        
        nodes.forEach(IKineticNode::resetVisitState);
        
        Queue<IKineticNode> queue = new ArrayDeque<>(nodes);
        while (!queue.isEmpty())
        {
            IKineticNode activeNode = Objects.requireNonNull(queue.poll());
            if (activeNode.isVisited()) continue;
            
            float velocity = clientUpdates.getOrDefault(activeNode.getNodeID(), Float.NaN);
            
            if (Float.isNaN(velocity)) continue;
            
            TObjectFloatMap<IKineticNode> nodes = new TObjectFloatHashMap<>(16, 0.75F, Float.NaN);
            
            if (findNetwork(nodes, activeNode, World::isBlockLoaded)) updateNetwork(nodes, velocity);
            else nodes.keySet().forEach(node -> node.update(Float.NaN, 0, Float.NaN));
        }
        
        clientUpdates.clear();
    }
    
    private void updateNetwork(TObjectFloatMap<IKineticNode> nodes, float totalVelocity)
    {
        nodes.forEachEntry((node, ratio) -> {
            node.update(Float.NaN, totalVelocity / ratio, Float.NaN);
            return true;
        });
    }
}
