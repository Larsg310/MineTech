package wexalian.mods.minetech;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wexalian.mods.minetech.init.GuiHandler;
import wexalian.mods.minetech.kinesis.KineticManager;
import wexalian.mods.minetech.lib.Reference;
import wexalian.mods.minetech.network.PacketKineticUpdate;
import wexalian.mods.minetech.proxy.IProxy;
import wexalian.mods.minetech.recipe.SmeltingRecipes;
import wexalian.mods.minetech.simple.SimpleCapabilityManager;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = Reference.MOD_DEPENDENCIES)
public class MineTech
{
    @Mod.Instance(Reference.MOD_ID)
    public static MineTech instance;
    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static IProxy proxy;
    
    public static final Logger log = LogManager.getLogger(Reference.MOD_NAME);
    
    public static SimpleNetworkWrapper networkManager = new SimpleNetworkWrapper(Reference.MOD_ID);
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        SimpleCapabilityManager.INSTANCE.init(event.getAsmData());
        KineticManager.init();
        proxy.preInit();
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
        
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        
        networkManager.registerMessage(PacketKineticUpdate.Handler.class, PacketKineticUpdate.class, 0, Side.CLIENT);
        
        SmeltingRecipes.init();
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }
}
