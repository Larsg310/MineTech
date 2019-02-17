package com.wexalian.mods.minetech.lib;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum Materials implements IStringSerializable
{
    IRON("iron"),
    GOLD("gold");
    
    private String name;
    
    Materials(String name)
    {
        this.name = name;
    }
    
    @Nonnull
    @Override
    public String getName()
    {
        return name;
    }
}
