package wexalian.mods.minetech.tileentity;

import net.minecraft.tileentity.TileEntity;

public abstract class TileRotating extends TileEntity
{
    
    public abstract float getAngle(float partialTicks);
    
    public abstract float getScale();
    
}
