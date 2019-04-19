package com.wexalian.mods.minetech.proxy;

public interface IProxy
{
    default void registerRenderers() {}
    
    default int getWaterColor() {return -1;}
}
