package com.wexalian.mods.minetech.event;

import com.wexalian.mods.minetech.multiblock.MultiblockResourceManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class ServerEvents
{
    @SubscribeEvent
    public static void serverStartedEvent(FMLServerStartingEvent event){
        MultiblockResourceManager.register(event.getServer());
    }
}
