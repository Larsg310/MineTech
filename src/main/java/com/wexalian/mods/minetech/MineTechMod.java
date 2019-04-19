package com.wexalian.mods.minetech;

import com.wexalian.mods.minetech.api.block.MineTechBlocks;
import com.wexalian.mods.minetech.energy.kinetic.KineticManager;
import com.wexalian.mods.minetech.event.*;
import com.wexalian.mods.minetech.gui.GuiHandler;
import com.wexalian.mods.minetech.init.MineTechCapabilities;
import com.wexalian.mods.minetech.init.MineTechRecipes;
import com.wexalian.mods.minetech.lib.Reference;
import com.wexalian.mods.minetech.network.NetworkHandler;
import com.wexalian.mods.minetech.proxy.IProxy;
import com.wexalian.mods.minetech.registry.RegistryEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class MineTechMod
{
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static final IProxy PROXY = DistExecutor.runForDist(Reference.CLIENT_PROXY, Reference.SERVER_PROXY);
    
    public static final ItemGroup ITEM_GROUP = new ItemGroup(Reference.MOD_ID)
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(MineTechBlocks.GRINDSTONE);
        }
    };
    
    public MineTechMod()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        
        FMLJavaModLoadingContext.get().getModEventBus().register(RegistryEvents.class);
        
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::openGui);
        
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        MinecraftForge.EVENT_BUS.register(ClientRegistryEvents.class);
        
        MinecraftForge.EVENT_BUS.register(PlayerEvents.class);
        MinecraftForge.EVENT_BUS.register(ServerEvents.class);
        
        MinecraftForge.EVENT_BUS.register(KineticManager.INSTANCE);
    }
    
    private void commonSetup(FMLCommonSetupEvent event)
    {
        NetworkHandler.register();
        MineTechCapabilities.register();
        MineTechRecipes.register();
    }
    
    private void clientSetup(FMLClientSetupEvent event)
    {
        PROXY.registerRenderers();
    }
    
    private void serverSetup(FMLDedicatedServerSetupEvent event)
    {
    }
}
