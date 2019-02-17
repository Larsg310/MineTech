package com.wexalian.mods.minetech.proxy;

public interface IProxy
{
    default void registerTileEntityRenderers() {}
    
    default void registerGuis() {}
}
