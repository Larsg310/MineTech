package wexalian.mods.minetech.metal;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum Metals implements IStringSerializable
{
    IRON("iron"),
    GOLD("gold");
    
    private String name;
    
    Metals(String name)
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
