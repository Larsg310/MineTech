package wexalian.mods.minetech;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wexalian.mods.minetech.init.GuiHandler;
import wexalian.mods.minetech.lib.Reference;
import wexalian.mods.minetech.proxy.IProxy;
import wexalian.mods.minetech.recipe.SmeltingRecipes;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = Reference.MOD_DEPENDENCIES)
public class MineTech
{
    @Mod.Instance(Reference.MOD_ID)
    public static MineTech instance;
    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static IProxy proxy;
    
    static
    {
        FluidRegistry.enableUniversalBucket();
    }
    
    public static final Logger log = LogManager.getLogger(Reference.MOD_NAME);
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit();
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        SmeltingRecipes.init();
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }
}
