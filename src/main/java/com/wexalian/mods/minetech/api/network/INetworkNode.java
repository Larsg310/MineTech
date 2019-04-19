package com.wexalian.mods.minetech.api.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.UUID;

public interface INetworkNode<T extends INetworkNode<T, X>, X> extends INBTSerializable<NBTTagCompound>
{
    @Override
    default NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.putUniqueId("uuid", getNodeId());
        return tag;
    }
    
    @Override
    default void deserializeNBT(NBTTagCompound tag)
    {
        setNodeId(tag.getUniqueId("uuid"));
    }
    
    UUID getNodeId();
    
    void setNodeId(UUID nodeId);
    
    void markVisited();
    
    void resetVisitState();
    
    INetworkNodeHost<T, X> getHost();
    
    @Nullable
    X getValue();
    
    void setValue(@Nullable X value);
    
    boolean isRemote();
    
    void setRemote(boolean isRemote);
    
    boolean isVisited();
}
