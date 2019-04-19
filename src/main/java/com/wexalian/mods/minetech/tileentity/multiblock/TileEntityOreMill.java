package com.wexalian.mods.minetech.tileentity.multiblock;

import net.minecraft.tileentity.TileEntityType;

public class TileEntityOreMill extends TileEntityMultiblock
{
    public static TileEntityType<TileEntityOreMill> TYPE;
    
    public TileEntityOreMill()
    {
        super(TYPE);
    }
}
