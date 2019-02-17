package com.wexalian.mods.minetech;

import com.wexalian.mods.minetech.event.ClientEvents;
import com.wexalian.mods.minetech.event.RegistryEvents;
import com.wexalian.mods.minetech.lib.Reference;
import com.wexalian.mods.minetech.proxy.ClientProxy;
import com.wexalian.mods.minetech.proxy.IProxy;
import com.wexalian.mods.minetech.proxy.ServerProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class MineTech
{
    public static final Logger LOGGER = LogManager.getLogger();
    
    @SuppressWarnings("Convert2MethodRef")
    public static final IProxy PROXY = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
    
    public MineTech()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        
        FMLJavaModLoadingContext.get().getModEventBus().register(RegistryEvents.class);
        
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        // DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> FMLModLoadingContext.get().getModEventBus().register(ClientEvents.class));
    }
    
    private void commonSetup(FMLCommonSetupEvent event)
    {
    
    }
    
    private void clientSetup(FMLClientSetupEvent event)
    {
        PROXY.registerTileEntityRenderers();
        PROXY.registerGuis();
    }
    
    private void serverSetup(FMLDedicatedServerSetupEvent event)
    {
    
    }
}
