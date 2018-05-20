package wexalian.mods.minetech.kinesis;

import gnu.trove.map.TObjectFloatMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;

public interface KineticManager
{
    void add(IKineticNode node);
    
    void remove(IKineticNode node);
    
    default void visitNetwork(IKineticNode start, BiPredicate<World, BlockPos> posValidator)
    {
        Queue<IKineticNode> activeQueue = new ArrayDeque<>();
        activeQueue.add(start);
        while (!activeQueue.isEmpty())
        {
            IKineticNode current = activeQueue.poll();
            if (current != null)
            {
                current.markVisited();
                current.addNeighbours((node, ratio) -> {
                    if (node != null && !(node).isVisited())
                    {
                        activeQueue.add(node);
                    }
                }, posValidator);
            }
        }
    }
    
    default boolean findNetwork(TObjectFloatMap<IKineticNode> nodes, IKineticNode activeNode, BiPredicate<World, BlockPos> posValidator)
    {
        AtomicBoolean failed = new AtomicBoolean(false);
        Queue<Pair<IKineticNode, Float>> activeQueue = new ArrayDeque<>();
        
        activeQueue.add(Pair.of(activeNode, 1F));
        
        while (!activeQueue.isEmpty())
        {
            Pair<IKineticNode, Float> pair = activeQueue.poll();
            if (pair != null)
            {
                float prevRatio = nodes.put(pair.getKey(), pair.getValue());
                
                if (Float.isNaN(prevRatio))
                {
                    pair.getKey().setRatio(pair.getValue());
                    pair.getKey().addNeighbours((node, ratio) -> activeQueue.add(Pair.of(node, ratio * pair.getValue())), posValidator);
                }
                else if (prevRatio != pair.getValue())
                {
                    pair.getKey().setRatio(0);
                    failed.set(true);
                }
                pair.getKey().markVisited();
            }
        }
        
        return !failed.get();
    }
    
    static void init()
    {
        MinecraftForge.EVENT_BUS.register(ServerKineticManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ClientKineticManager.INSTANCE);
    }
}
