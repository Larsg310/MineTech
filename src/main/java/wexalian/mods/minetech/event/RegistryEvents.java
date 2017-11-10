package wexalian.mods.minetech.event;

import wexalian.mods.minetech.block.BlockGrindstone;
import wexalian.mods.minetech.init.ModBlocks;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.tileentity.TileEntityGrindstone;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class RegistryEvents
{
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        ModBlocks.GRINDSTONE = new BlockGrindstone();
        event.getRegistry().register(ModBlocks.GRINDSTONE);
        GameRegistry.registerTileEntity(TileEntityGrindstone.class, BlockNames.GRINDSTONE);
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        ItemBlock GRINDSTONE = new ItemBlock(ModBlocks.GRINDSTONE);
        GRINDSTONE.setRegistryName(BlockNames.GRINDSTONE);
        event.getRegistry().register(GRINDSTONE);
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.GRINDSTONE), 0, new ModelResourceLocation(BlockNames.GRINDSTONE, "inventory"));
    }
}
