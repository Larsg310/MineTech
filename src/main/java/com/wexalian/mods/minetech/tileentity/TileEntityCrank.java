package com.wexalian.mods.minetech.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityCrank extends TileEntityRotating implements ITickable
{
    public static final int MAX_CRANK_TIME = 36;
    public static final int MIN_CRANK_TIME = 20;
    
    public static TileEntityType<TileEntityCrank> TYPE;
    
    private int maxCrankTime = 0;
    private int crankTime = 0;
    
    public TileEntityCrank()
    {
        super(TYPE);
    }
    
    @Override
    public void tick()
    {
        if (crankTime > 0)
        {
            System.out.println("Crank Time: " + crankTime + (world.isRemote ? " (CLIENT)" : " (SERVER)"));
            crankTime--;
        }
    }
    
    public boolean tryCrank()
    {
        if (!world.isRemote)
        {
            if (canCrank())
            {
                crank();
                return true;
            }
        }
        return true;
    }
    
    private boolean canCrank()
    {
        if (crankTime != 0) return false;
        TileEntity tile = world.getTileEntity(pos.down());
        if (tile instanceof TileEntityGrindstone)
        {
            return ((TileEntityGrindstone) tile).canGrind();
        }
        return false;
    }
    
    private void crank()
    {
        maxCrankTime = world.rand.nextInt(MAX_CRANK_TIME - MIN_CRANK_TIME) + MIN_CRANK_TIME + 1;
        crankTime = maxCrankTime;
        System.out.println("Max Crank Time: " + maxCrankTime);
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
    }
    
    @Override
    public float getAngle(float partialTicks)
    {
        if (isCranking()) return (360F / maxCrankTime) * crankTime - (360F / maxCrankTime) * partialTicks;
        return 0F;
    }
    
    public boolean isCranking()
    {
        return crankTime > 0;
    }
    
    @Nonnull
    @Override
    public EnumFacing.Axis getRotationAxis()
    {
        return EnumFacing.Axis.Y;
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        read(pkt.getNbtCompound());
    }
    
    @Override
    public void read(NBTTagCompound compound)
    {
        super.read(compound);
        maxCrankTime = compound.getInt("maxCrankTime");
        crankTime = maxCrankTime;
    }
    
    @Nonnull
    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        compound.putInt("maxCrankTime", maxCrankTime);
        return compound;
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
}
