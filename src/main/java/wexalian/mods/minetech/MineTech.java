package wexalian.mods.minetech;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
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
import wexalian.mods.minetech.kinesis.*;
import wexalian.mods.minetech.lib.Reference;
import wexalian.mods.minetech.network.PacketKineticUpdate;
import wexalian.mods.minetech.proxy.IProxy;
import wexalian.mods.minetech.recipe.SmeltingRecipes;

import javax.annotation.Nullable;
import java.util.function.Function;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, dependencies = Reference.MOD_DEPENDENCIES)
public class MineTech
{
    @Mod.Instance(Reference.MOD_ID)
    public static MineTech instance;
    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static IProxy proxy;
    
    public static final Logger log = LogManager.getLogger(Reference.MOD_NAME);
    
    public static Function<IKineticNode.Host, IKineticNode> kineticNodeProvider = KineticNode::new;
    public static SimpleNetworkWrapper networkManager = new SimpleNetworkWrapper(Reference.MOD_ID);
    
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
        KineticManager.init();
        CapabilityManager.INSTANCE.register(IGearAttachable.class, new Capability.IStorage<IGearAttachable>()
        {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<IGearAttachable> capability, IGearAttachable instance, EnumFacing side)
            {
                return null;
            }
            
            @Override
            public void readNBT(Capability<IGearAttachable> capability, IGearAttachable instance, EnumFacing side, NBTBase nbt)
            {
            
            }
        }, () -> null);
        CapabilityManager.INSTANCE.register(IShaftAttachable.class, new Capability.IStorage<IShaftAttachable>()
        {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<IShaftAttachable> capability, IShaftAttachable instance, EnumFacing side)
            {
                return null;
            }
    
            @Override
            public void readNBT(Capability<IShaftAttachable> capability, IShaftAttachable instance, EnumFacing side, NBTBase nbt)
            {
        
            }
        }, () -> null);
        SmeltingRecipes.init();
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
        
        networkManager.registerMessage(PacketKineticUpdate.class, PacketKineticUpdate.class, 0, Side.CLIENT);
    }
}
