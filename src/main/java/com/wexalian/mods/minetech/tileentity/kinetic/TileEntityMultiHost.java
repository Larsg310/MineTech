package com.wexalian.mods.minetech.tileentity.kinetic;

import com.wexalian.mods.minetech.api.MineTechAPI;
import com.wexalian.mods.minetech.api.energy.kinetic.IKineticNode;
import com.wexalian.mods.minetech.api.network.INetworkNodeHost;
import com.wexalian.mods.minetech.api.network.INetworkNode;
import com.wexalian.mods.minetech.energy.kinetic.KineticNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public abstract class TileEntityMultiHost<T extends INetworkNode<T, X>, X> extends TileEntity
{
    protected final T[] nodes;
    private final boolean[] enabled;
    
    @SuppressWarnings("unchecked")
    public TileEntityMultiHost(TileEntityType<?> type, int nodeCount, float inertia)
    {
        super(type);
        enabled = new boolean[nodeCount];
        nodes = (T[]) new KineticNode[nodeCount];
        
        for (int i = 0; i < nodeCount; i++)
        {
            nodes[i] = (T) new KineticNode((INetworkNodeHost<IKineticNode, Float>) getHost(i), inertia);
        }
    }
    
    private INetworkNodeHost<T, X> getHost(int index)
    {
        return new IndexedHost<>(this, index, this::getWorld, () -> new ChunkPos(getPos()));
    }
    
    public abstract void addNeighbours(BiConsumer<T, X> nodeConsumer, BiPredicate<World, BlockPos> posValidator, int index);
    
    @Override
    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        
        for (int i = 0; i < nodes.length; i++)
        {
            nodes[i].deserializeNBT(compound.getCompound("node" + i));
            enabled[i] = compound.getBoolean("enabled" + i);
        }
    }
    
    @Override
    @Nonnull
    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        
        for (int i = 0; i < nodes.length; i++)
        {
            compound.put("node" + i, nodes[i].serializeNBT());
            compound.putBoolean("enabled" + i, enabled[i]);
        }
        
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
        for (int i = 0; i < nodes.length; i++)
        {
            if (enabled[i])
            {
                MineTechAPI.Kinesis.INSTANCE.remove((IKineticNode) nodes[i]);
            }
        }
        super.remove();
    }
    
    @Override
    public void validate()
    {
        for (int i = 0; i < nodes.length; i++)
        {
            if (enabled[i])
            {
                MineTechAPI.Kinesis.INSTANCE.add((IKineticNode) nodes[i], world.isRemote);
            }
        }
        super.validate();
    }
    
    public String getNodeString(int faceIndex)
    {
        return nodes[faceIndex].toString();
    }
    
    public T getNode(int index)
    {
        return nodes[index];
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.getNbtCompound();
        handleUpdateTag(tag);
    }
    
    @Override
    public void onChunkUnloaded()
    {
        for (int i = 0; i < nodes.length; i++)
        {
            if (enabled[i])
            {
                MineTechAPI.Kinesis.INSTANCE.remove((IKineticNode) nodes[i]);
            }
        }
        super.onChunkUnloaded();
    }
    
    @Override
    public void onLoad()
    {
        for (int i = 0; i < nodes.length; i++)
        {
            if (enabled[i])
            {
                MineTechAPI.Kinesis.INSTANCE.add((IKineticNode) nodes[i], world.isRemote);
            }
        }
        super.onLoad();
    }
    
    public void setEnabled(int index)
    {
        enabled[index] = true;
        MineTechAPI.Kinesis.INSTANCE.add((IKineticNode) nodes[index], world.isRemote);
    }
    
    public void setDisabled(int index)
    {
        enabled[index] = false;
        MineTechAPI.Kinesis.INSTANCE.remove((IKineticNode) nodes[index]);
    }
    
    public boolean isEnabled(int index)
    {
        return enabled[index];
    }
    
    public boolean isAnyEnabled()
    {
        for (boolean enable : enabled)
        {
            if (enable) return true;
        }
        return false;
    }
    
    public static class IndexedHost<T extends INetworkNode<T, X>, X> implements INetworkNodeHost<T, X>
    {
        private TileEntityMultiHost<T, X> tile;
        private int index;
        private Supplier<World> worldSupplier;
        private Supplier<ChunkPos> posSupplier;
        
        public IndexedHost(TileEntityMultiHost<T, X> tile, int index, @Nonnull Supplier<World> worldSupplier, Supplier<ChunkPos> posSupplier)
        {
            this.tile = tile;
            this.index = index;
            this.worldSupplier = worldSupplier;
            this.posSupplier = posSupplier;
        }
        
        @Override
        public void addNeighbours(BiConsumer<T, X> nodeConsumer, BiPredicate<World, BlockPos> posValidator)
        {
            tile.addNeighbours(nodeConsumer, posValidator, index);
        }
        
        @Nonnull
        @Override
        public World getWorld()
        {
            return worldSupplier.get();
        }
        
        @Nonnull
        @Override
        public ChunkPos getChunkPos()
        {
            return posSupplier.get();
        }
    }
}
