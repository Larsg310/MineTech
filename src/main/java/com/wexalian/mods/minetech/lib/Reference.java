package com.wexalian.mods.minetech.lib;

import com.wexalian.mods.minetech.proxy.ClientProxy;
import com.wexalian.mods.minetech.proxy.IProxy;
import com.wexalian.mods.minetech.proxy.ServerProxy;

import java.util.function.Supplier;

public class Reference
{
    public static final String MOD_ID = "minetech";
    
    @SuppressWarnings("Convert2MethodRef") // don't use a method ref, it will cause classloading issues
    public static final Supplier<Supplier<IProxy>> CLIENT_PROXY = () -> () -> new ClientProxy();
    @SuppressWarnings("Convert2MethodRef") // same as above
    public static final Supplier<Supplier<IProxy>> SERVER_PROXY = () -> () -> new ServerProxy();
}
