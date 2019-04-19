package com.wexalian.mods.minetech.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public abstract class TileEntityRotating extends TileEntity
{
    TileEntityRotating(TileEntityType<?> type)
    {
        super(type);
    }
    
    public abstract float getAngle(float partialTicks);
    
    @Nonnull
    public EnumFacing getRotationFacing()
    {
        return EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, getRotationAxis());
    }
    
    @Nonnull
    public abstract EnumFacing.Axis getRotationAxis();
    
    public float getScale()
    {
        return 1F;
    }
    
    public int getTurnDirection()
    {
        return 1;
    }
    
}
