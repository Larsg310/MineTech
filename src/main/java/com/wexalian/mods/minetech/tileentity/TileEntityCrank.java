package com.wexalian.mods.minetech.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import javax.annotation.Nonnull;

public class TileEntityCrank extends TileEntityRotating implements ITickable
{
    public static final int MAX_CRANK_TIME = 20;
    public static final float DEG_PER_TICK = 360F/MAX_CRANK_TIME;
    
    public static TileEntityType<TileEntityCrank> TYPE;
    private int crankTime = 0;
    
    
    public TileEntityCrank()
    {
        super(TYPE);
    }
    
    @Override
    public void tick()
    {
        if (crankTime > 0) crankTime--;
    }
    
    public boolean tryCrank()
    {
        if (canCrank())
        {
            crank();
            return true;
        }
        return false;
    }
    
    private boolean canCrank()
    {
        if (crankTime != 0) return false;
        // TileEntity tile = world.getTileEntity(pos.down());
        // if (tile instanceof TileEntityGrindstone)
        // {
        //     return ((TileEntityGrindstone) tile).canGrind();
        // }
        // return false;
        return true;
    }
    
    private void crank()
    {
        crankTime += MAX_CRANK_TIME;
    }
    
    @Override
    public float getAngle(float partialTicks)
    {
        if (isCranking()) return DEG_PER_TICK * crankTime - DEG_PER_TICK * partialTicks;
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
}
