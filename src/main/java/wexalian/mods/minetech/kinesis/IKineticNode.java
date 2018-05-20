package wexalian.mods.minetech.kinesis;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import wexalian.mods.minetech.util.ObjFloatConsumer;

import java.util.UUID;
import java.util.function.BiPredicate;

public interface IKineticNode extends INBTSerializable<NBTTagCompound>
{
    void validate(boolean isRemote);
    
    void invalidate();
    
    float getTorque();
    
    float getVelocity();
    
    float getPower();
    
    float getAngle();
    
    default float getAngle(float partialTicks)
    {
        return getAngle() + partialTicks * getVelocity();
    }
    
    float getInertia();
    
    float getAppliedPower();
    
    float getConsumedPower();
    
    World getWorld();
    
    ChunkPos getChunkPos();
    
    void update(float torque, float velocity, float power);
    
    void markVisited();
    
    void resetVisitState();
    
    boolean isVisited();
    
    void addNeighbours(ObjFloatConsumer<IKineticNode> neighbours, BiPredicate<World, BlockPos> posValidator);
    
    void setRatio(float ratio);
    
    UUID getNodeID();
    
    interface Host
    {
        World getWorld();
        
        ChunkPos getChunkPos();
        
        float getInertia();
        
        default float getAppliedPower()
        {
            return 0;
        }
        
        default float getConsumedPower()
        {
            return getInertia();
        }
        
        void addNeighbours(ObjFloatConsumer<IKineticNode> neighbours, BiPredicate<World, BlockPos> posValidator);
    }
    
}
