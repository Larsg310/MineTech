package wexalian.mods.minetech.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public abstract class TileEntityRotating extends TileEntity
{
    public abstract float getAngle(float partialTicks);
    
    public float getScale()
    {
        return 1.0f;
    }
    
    @Nonnull
    public abstract EnumFacing.Axis getRotationAxis();
    
    @Nonnull
    public EnumFacing getRotationFacing()
    {
        return EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, getRotationAxis());
    }
    
}
