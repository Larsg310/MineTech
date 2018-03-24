package wexalian.mods.minetech.proxy;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ServerProxy implements IProxy
{
    @Override
    public void schedule(Side side, Runnable task)
    {
        if (side == Side.SERVER) FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(task);
    }
}
