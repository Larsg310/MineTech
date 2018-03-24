package wexalian.mods.minetech.proxy;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public interface IProxy
{
    default void preInit() {}
    
    default void init() {}
    
    default void postInit() {}
    
    void schedule(Side side, Runnable task);
}
