package wexalian.mods.minetech.tileentity.mechanical;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import wexalian.mods.minetech.kinesis.IKineticNode;
import wexalian.mods.minetech.kinesis.IShaftAttachable;
import wexalian.mods.minetech.kinesis.KineticNode;
import wexalian.mods.minetech.tileentity.TileEntityRotating;
import wexalian.mods.minetech.util.ObjFloatConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiPredicate;

public class TileEntityShaft extends TileEntityRotatingNode implements IKineticNode.Host
{
    private KineticNode node = new KineticNode(this);
    
    @Override
    public IKineticNode getNode()
    {
        return node;
    }
    
    @Override
    public float getInertia()
    {
        return 1;
    }
    
    @Override
    public float getAppliedPower()
    {
        return 0;
    }
    
    @Override
    public void addNeighbours(ObjFloatConsumer<IKineticNode> neighbours, BiPredicate<World, BlockPos> posValidator)
    {
        EnumFacing.Axis axis = getWorld().getBlockState(getPos()).getValue(BlockRotatedPillar.AXIS);
        KineticNode.findShaft(getWorld(), getPos(), EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis), 1F, neighbours, posValidator);
        KineticNode.findShaft(getWorld(), getPos(), EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis), 1F, neighbours, posValidator);
    }
    
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
    {
        EnumFacing.Axis axis = getWorld().getBlockState(getPos()).getValue(BlockRotatedPillar.AXIS);
        
        if (capability == IShaftAttachable.CAPABILITY && facing.getAxis() == axis) return true;
        return super.hasCapability(capability, facing);
    }
    
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
    {
        EnumFacing.Axis axis = getWorld().getBlockState(getPos()).getValue(BlockRotatedPillar.AXIS);
        
        if (capability == IShaftAttachable.CAPABILITY && facing.getAxis() == axis) return (T) (IShaftAttachable) () -> node;
        return super.getCapability(capability, facing);
    }
    
    @Override
    public float getAngle(float partialTicks)
    {
        EnumFacing negative = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, getWorld().getBlockState(getPos()).getValue(BlockRotatedPillar.AXIS));
        TileEntity tile = getWorld().getTileEntity(pos.offset(negative));
        if (tile instanceof TileEntityShaft) return ((TileEntityShaft) tile).getAngle(partialTicks);
        return node.getAngle(partialTicks);
    }
    
    @Nonnull
    @Override
    public EnumFacing.Axis getRotationAxis()
    {
        return getWorld().getBlockState(getPos()).getValue(BlockRotatedPillar.AXIS);
    }
}
