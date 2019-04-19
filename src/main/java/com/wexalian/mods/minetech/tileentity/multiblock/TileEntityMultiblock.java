package com.wexalian.mods.minetech.tileentity.multiblock;

import com.wexalian.mods.minetech.multiblock.Multiblock;
import com.wexalian.mods.minetech.multiblock.MultiblockManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityMultiblock extends TileEntity
{
    private Multiblock multiblock;
    private BlockPos relativePos;
    private boolean isCenter = false;
    
    public TileEntityMultiblock(TileEntityType<?> type)
    {
        super(type);
    }
    
    public void setMultiblock(Multiblock multiblock, BlockPos relativePos)
    {
        this.multiblock = multiblock;
        this.relativePos = relativePos;
        this.isCenter = relativePos.getX() == multiblock.getCenter().getX() && relativePos.getY() == multiblock.getCenter().getY() && relativePos.getZ() == multiblock.getCenter().getX();
        world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 2);
        markDirty();
    }
    
    public BlockPos getRelativePos()
    {
        return relativePos;
    }
    
    public Multiblock getMultiblock()
    {
        return multiblock;
    }
    
    public boolean isCenter()
    {
        return isCenter;
    }
    
    @Nonnull
    @Override
    public NBTTagCompound write(NBTTagCompound tag)
    {
        super.write(tag);
        if (multiblock != null && relativePos != null)
        {
            tag.putBoolean("isCenter", isCenter);
            tag.putInt("relativePosX", relativePos.getX());
            tag.putInt("relativePosY", relativePos.getY());
            tag.putInt("relativePosZ", relativePos.getZ());
            tag.putString("multiblock", multiblock.getId().toString());
        }
        return tag;
    }
    
    @Override
    public void read(NBTTagCompound tag)
    {
        super.read(tag);
        isCenter = tag.getBoolean("isCenter");
        relativePos = new BlockPos(tag.getInt("relativePosX"), tag.getInt("relativePosY"), tag.getInt("relativePosZ"));
        multiblock = MultiblockManager.INSTANCE.get(new ResourceLocation(tag.getString("multiblock")));
    }
    
    @Nullable
    @Override
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
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        read(pkt.getNbtCompound());
    }
    
}
