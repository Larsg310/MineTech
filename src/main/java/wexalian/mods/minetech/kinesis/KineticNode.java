package wexalian.mods.minetech.kinesis;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import wexalian.mods.minetech.util.ObjFloatConsumer;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BiPredicate;

public class KineticNode implements IKineticNode
{
    private final IKineticNode.Host host;
    
    private UUID nodeID = UUID.randomUUID();
    
    private boolean visited = false;
    private boolean isRemote;
    
    private float torque;
    private float velocity;
    private float power;
    private float angle;
    private float ratio;
    
    public KineticNode(IKineticNode.Host host)
    {
        this.host = host;
    }
    
    @Override
    public void validate(boolean isRemote)
    {
        this.isRemote = isRemote;
        if (isRemote) ClientKineticManager.INSTANCE.add(this);
        else ServerKineticManager.INSTANCE.add(this);
    }
    
    @Override
    public void invalidate()
    {
        if (isRemote) ClientKineticManager.INSTANCE.remove(this);
        else ServerKineticManager.INSTANCE.remove(this);
    }
    
    @Override
    public float getTorque()
    {
        return torque;
    }
    
    @Override
    public float getVelocity()
    {
        return velocity;
    }
    
    @Override
    public float getPower()
    {
        return power;
    }
    
    @Override
    public float getAngle()
    {
        return angle;
    }
    
    @Override
    public float getInertia()
    {
        return host.getInertia();
    }
    
    @Override
    public float getAppliedPower()
    {
        return host.getAppliedPower();
    }
    
    @Override
    public float getConsumedPower()
    {
        return host.getConsumedPower();
    }
    
    @Override
    public World getWorld()
    {
        return host.getWorld();
    }
    
    @Override
    public ChunkPos getChunkPos()
    {
        return host.getChunkPos();
    }
    
    @Override
    public void update(float torque, float velocity, float power)
    {
        this.torque = torque;
        this.velocity = velocity;
        this.power = power;
        this.angle = this.angle + velocity;
    }
    
    @Override
    public void markVisited()
    {
        visited = true;
    }
    
    @Override
    public boolean isVisited()
    {
        return visited;
    }
    
    @Override
    public void resetVisitState()
    {
        visited = false;
        
    }
    
    @Override
    public void addNeighbours(ObjFloatConsumer<IKineticNode> neighbours, BiPredicate<World, BlockPos> posValidator)
    {
        host.addNeighbours(neighbours, posValidator);
    }
    
    @Override
    public void setRatio(float ratio)
    {
        this.ratio = ratio;
    }
    
    @Override
    public UUID getNodeID()
    {
        return nodeID;
    }
    
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setUniqueId("id", nodeID);
        return tag;
    }
    
    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        nodeID = tag.getUniqueId("id");
        System.out.println(nodeID);
    }
    
    @Override
    public String toString()
    {
        return "KineticNode(torque=" + torque + ", velocity=" + velocity + ", power=" + power + ", angle=" + angle + ")";
    }
    
    public static void findShaft(World world, BlockPos pos, EnumFacing side, float ratio, ObjFloatConsumer<IKineticNode> neighbours, BiPredicate<World, BlockPos> posValidator)
    {
        pos = pos.offset(side);
        if (posValidator.test(world, pos))
        {
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te.hasCapability(IShaftAttachable.CAPABILITY, side.getOpposite()))
            {
                neighbours.accept(Objects.requireNonNull(te.getCapability(IShaftAttachable.CAPABILITY, side.getOpposite())).getNode(), ratio);
            }
        }
    }
}
