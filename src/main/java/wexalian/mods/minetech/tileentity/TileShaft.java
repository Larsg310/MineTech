package wexalian.mods.minetech.tileentity;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import wexalian.mods.minetech.kinesis.IKineticNode;
import wexalian.mods.minetech.kinesis.IShaftAttachable;
import wexalian.mods.minetech.util.ObjFloatConsumer;

import javax.annotation.Nonnull;
import java.util.function.BiPredicate;

public class TileShaft extends TileRotating implements IKineticNode.Host
{
    
    private final IKineticNode node = IKineticNode.create(this);
    public void debug()
    {
        System.out.println("--------------------------------------------");
        System.out.println("T > " + node);
    }
    @Override
    public World getKineticWorld()
    {
        return getWorld();
    }
    
    @Override
    public ChunkPos getKineticChunk()
    {
        return new ChunkPos(getPos());
    }
    
    @Override
    public void validate()
    {
        node.validate(getWorld().isRemote);
        super.validate();
    }
    
    @Override
    public void onLoad()
    {
        node.validate(getWorld().isRemote);
        super.onLoad();
    }
    
    @Override
    public void invalidate()
    {
        node.invalidate();
        super.invalidate();
    }
    
    @Override
    public void onChunkUnload()
    {
        node.invalidate();
        super.onChunkUnload();
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == IShaftAttachable.CAPABILITY && facing.getAxis().ordinal() == getBlockMetadata())
        {
            return true;
        }
        return super.hasCapability(capability, facing);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == IShaftAttachable.CAPABILITY && facing.getAxis().ordinal() == getBlockMetadata())
        {
            return (T) (IShaftAttachable) () -> node;
        }
        return super.getCapability(capability, facing);
    }
    
    @Override
    public float getAppliedPower()
    {
        return 0;
    }
    
    @Override
    public float getInertia()
    {
        return 1;
    }
    
    @Override
    public void addNeighbors(ObjFloatConsumer<IKineticNode> neighbors, BiPredicate<World, BlockPos> posValidator)
    {
        EnumFacing.Axis axis = getWorld().getBlockState(getPos()).getValue(BlockRotatedPillar.AXIS);
        EnumFacing dir = EnumFacing.getFacingFromAxis(AxisDirection.NEGATIVE, axis);
        IKineticNode.findShaft(getWorld(), getPos(), dir, 1, neighbors, posValidator);
        IKineticNode.findShaft(getWorld(), getPos(), dir.getOpposite(), 1, neighbors, posValidator);
    }
    
    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = super.getUpdateTag();
        tag.setTag("node", node.serializeNBT());
        return tag;
    }
    
    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity pkt)
    {
        node.deserializeNBT(pkt.getNbtCompound().getCompoundTag("node"));
    }
    
    public float getAngle(float partialTicks)
    {
        return node.getAngle(partialTicks);
    }
    
    @Override
    public float getScale()
    {
        return 1;
    }
}
