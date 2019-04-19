package com.wexalian.mods.minetech.tileentity.kinetic;

import com.wexalian.mods.minetech.api.MineTechAPI;
import com.wexalian.mods.minetech.api.energy.kinetic.IKineticNode;
import com.wexalian.mods.minetech.api.network.INetworkNodeHost;
import com.wexalian.mods.minetech.api.network.INetworkNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.ChunkPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityHost<T extends INetworkNode<T, X>, X> extends TileEntity implements INetworkNodeHost<T, X>
{
    public TileEntityHost(TileEntityType<?> type)
    {
        super(type);
    }
    
    @Override
    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        getNode().deserializeNBT(compound.getCompound("node"));
    }
    
    @Override
    @Nonnull
    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        compound.put("node", getNode().serializeNBT());
        return compound;
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
        return this.write(new NBTTagCompound());
    }
    
    @Override
    public void remove()
    {
        MineTechAPI.Kinesis.INSTANCE.remove((IKineticNode) getNode());
        super.remove();
    }
    
    @Override
    public void validate()
    {
        MineTechAPI.Kinesis.INSTANCE.add((IKineticNode) getNode(), world.isRemote);
        super.validate();
    }
    
    public abstract T getNode();
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.getNbtCompound();
        handleUpdateTag(tag);
    }
    
    @Override
    public void onChunkUnloaded()
    {
        MineTechAPI.Kinesis.INSTANCE.remove((IKineticNode) getNode());
        super.onChunkUnloaded();
    }
    
    @Override
    public void onLoad()
    {
        MineTechAPI.Kinesis.INSTANCE.add((IKineticNode) getNode(), world.isRemote);
        super.onLoad();
    }
    
    @Override
    public ChunkPos getChunkPos()
    {
        return new ChunkPos(getPos());
    }
}
