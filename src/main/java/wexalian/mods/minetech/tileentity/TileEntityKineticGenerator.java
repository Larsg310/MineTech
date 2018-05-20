package wexalian.mods.minetech.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.commons.lang3.tuple.Pair;
import wexalian.mods.minetech.kinesis.IKineticNode;
import wexalian.mods.minetech.kinesis.IShaftAttachable;
import wexalian.mods.minetech.kinesis.KineticNode;
import wexalian.mods.minetech.util.ObjFloatConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class TileEntityKineticGenerator extends TileEntity implements IKineticNode.Host
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
        return 4;
    }
    
    @Override
    public float getAppliedPower()
    {
        boolean shouldProducePower = Arrays.stream(EnumFacing.values())//
                                           .map(f -> Pair.of(f, getWorld().getTileEntity(getPos().offset(f))))//
                                           .filter(f -> f.getRight() != null)//
                                           .anyMatch(f -> f.getRight().hasCapability(IShaftAttachable.CAPABILITY, f.getLeft().getOpposite()));
        return shouldProducePower ? 20 : 0;
    }
    
    @Override
    public void addNeighbours(ObjFloatConsumer<IKineticNode> neighbours, BiPredicate<World, BlockPos> posValidator)
    {
        Arrays.stream(EnumFacing.values()).forEach(face -> KineticNode.findShaft(getWorld(), getPos(), face, 1F, neighbours, posValidator));
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
        if (facing == EnumFacing.UP && capability == IShaftAttachable.CAPABILITY) return true;
        return super.hasCapability(capability, facing);
    }
    
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
    {
        if (facing == EnumFacing.UP && capability == IShaftAttachable.CAPABILITY) return (T) (IShaftAttachable) () -> node;
        return super.getCapability(capability, facing);
    }
}
