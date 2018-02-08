package wexalian.mods.minetech.event;

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
import wexalian.mods.minetech.block.BlockCrank;
import wexalian.mods.minetech.block.BlockGrindstone;
import wexalian.mods.minetech.init.ModBlocks;
import wexalian.mods.minetech.init.ModItems;
import wexalian.mods.minetech.item.ItemDirtyOreDust;
import wexalian.mods.minetech.item.ItemOreDust;
import wexalian.mods.minetech.item.ItemPan;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.lib.ItemNames;
import wexalian.mods.minetech.tileentity.TileEntityCrank;
import wexalian.mods.minetech.tileentity.TileEntityGrindstone;

import java.util.stream.IntStream;

@Mod.EventBusSubscriber
public class RegistryEvents
{
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        //BLOCKS
        ModBlocks.GRINDSTONE = new BlockGrindstone();
        event.getRegistry().register(ModBlocks.GRINDSTONE);
        GameRegistry.registerTileEntity(TileEntityGrindstone.class, BlockNames.GRINDSTONE);
        
        ModBlocks.CRANK = new BlockCrank();
        event.getRegistry().register(ModBlocks.CRANK);
        GameRegistry.registerTileEntity(TileEntityCrank.class, BlockNames.CRANK);
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        //ITEMS
        ModItems.DIRTY_ORE_DUST = new ItemDirtyOreDust();
        event.getRegistry().register(ModItems.DIRTY_ORE_DUST);
        
        ModItems.ORE_DUST = new ItemOreDust();
        event.getRegistry().register(ModItems.ORE_DUST);
        
        ModItems.PAN = new ItemPan();
        event.getRegistry().register(ModItems.PAN);
        
        //ITEMBLOCKS
        ItemBlock grindstone = new ItemBlock(ModBlocks.GRINDSTONE);
        grindstone.setRegistryName(BlockNames.GRINDSTONE);
        event.getRegistry().register(grindstone);
        
        ItemBlock crank = new ItemBlock(ModBlocks.CRANK);
        crank.setRegistryName(BlockNames.CRANK);
        event.getRegistry().register(crank);
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event)
    {
        //BLOCK MODELS
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.GRINDSTONE), 0, new ModelResourceLocation(BlockNames.GRINDSTONE, "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.CRANK), 0, new ModelResourceLocation(BlockNames.CRANK, "inventory"));
        
        //ITEM MODELS
        IntStream.range(0, ItemDirtyOreDust.TYPES.size()).forEach(meta -> ModelLoader.setCustomModelResourceLocation(ModItems.DIRTY_ORE_DUST, meta, new ModelResourceLocation(ItemNames.DIRTY_ORE_DUST + "_" + ItemDirtyOreDust.TYPES.get(meta).getName(), "inventory")));
        IntStream.range(0, ItemOreDust.TYPES.size()).forEach(meta -> ModelLoader.setCustomModelResourceLocation(ModItems.ORE_DUST, meta, new ModelResourceLocation(ItemNames.ORE_DUST + "_" + ItemOreDust.TYPES.get(meta).getName(), "inventory")));
        
        ModelLoader.setCustomModelResourceLocation(ModItems.PAN, 0, new ModelResourceLocation(ItemNames.PAN));
    }
}
