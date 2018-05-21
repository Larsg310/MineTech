package wexalian.mods.minetech.tileentity.mechanical;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import wexalian.mods.minetech.kinesis.IKineticNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityNode extends TileEntity implements IKineticNode.Host
{
    @Override
    public void validate()
    {
        getNode().validate(getWorld().isRemote);
        super.validate();
    }
    
    @Override
    public void onLoad()
    {
        getNode().validate(getWorld().isRemote);
        super.onLoad();
    }
    
    @Override
    public void invalidate()
    {
        getNode().invalidate();
        super.invalidate();
    }
    
    @Override
    public void onChunkUnload()
    {
        getNode().invalidate();
        super.onChunkUnload();
    }
    
    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setTag("node", getNode().serializeNBT());
        return compound;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        getNode().deserializeNBT(compound.getCompoundTag("node"));
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
    
    @Override
    public ChunkPos getChunkPos()
    {
        return new ChunkPos(getPos());
    }
    
    public abstract IKineticNode getNode();
}
