package wexalian.mods.minetech.tileentity;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import wexalian.mods.minetech.kinesis.IKineticNode;
import wexalian.mods.minetech.kinesis.IShaftAttachable;
import wexalian.mods.minetech.kinesis.KineticNode;
import wexalian.mods.minetech.util.ObjFloatConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiPredicate;

public class TileEntityShaft extends TileEntityRotating implements IKineticNode.Host
{
    private KineticNode node = new KineticNode(this);
    
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
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("node", node.serializeNBT());
        return compound;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        node.deserializeNBT(compound.getCompoundTag("node"));
    }
    
    @Override
    public ChunkPos getChunkPos()
    {
        return new ChunkPos(getPos());
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
    
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }
    
    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.getNbtCompound();
        handleUpdateTag(tag);
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
        return node.getAngle(partialTicks);
    }
    
    @Nonnull
    @Override
    public EnumFacing.Axis getRotationAxis()
    {
        return getWorld().getBlockState(getPos()).getValue(BlockRotatedPillar.AXIS);
    }
}
