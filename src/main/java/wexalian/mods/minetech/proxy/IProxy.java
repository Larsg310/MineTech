package wexalian.mods.minetech.proxy;

public interface IProxy
{
    default void preInit() {}
    
    default void init() {}
    
    default void postInit() {}
}
