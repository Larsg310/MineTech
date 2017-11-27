package wexalian.mods.minetech.proxy;

import wexalian.mods.minetech.metal.Metals;

public class ClientProxy implements IProxy
{
    @Override
    public void init()
    {
        Metals.Reloader.register();
    }
}
