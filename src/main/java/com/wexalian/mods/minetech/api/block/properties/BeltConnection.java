package com.wexalian.mods.minetech.api.block.properties;

import net.minecraft.util.IStringSerializable;

public enum BeltConnection implements IStringSerializable
{
    LEFT("left"),
    RIGHT("right"),
    STRAIGHT("straight");
    
    private final String name;
    
    BeltConnection(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return this.name;
    }
}