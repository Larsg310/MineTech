package com.wexalian.mods.minetech.event;

import com.wexalian.mods.minetech.block.BlockCrank;
import com.wexalian.mods.minetech.block.BlockGrindstone;
import com.wexalian.mods.minetech.init.MineTechItems;
import com.wexalian.mods.minetech.item.block.ItemBlockGroup;
import com.wexalian.mods.minetech.lib.Materials;
import com.wexalian.mods.minetech.tileentity.TileEntityGrindstone;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.wexalian.mods.minetech.init.MineTechBlocks;
import com.wexalian.mods.minetech.item.ItemDirtyOreDust;
import com.wexalian.mods.minetech.lib.BlockNames;
import com.wexalian.mods.minetech.tileentity.TileEntityCrank;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class RegistryEvents
{
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        MineTechBlocks.GRINDSTONE = new BlockGrindstone();
        event.getRegistry().register(MineTechBlocks.GRINDSTONE);
        
        MineTechBlocks.CRANK = new BlockCrank();
        event.getRegistry().register(MineTechBlocks.CRANK);
        
    }
    
    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event)
    {
        TileEntityGrindstone.TYPE = create(BlockNames.GRINDSTONE, TileEntityGrindstone::new);
        event.getRegistry().register(TileEntityGrindstone.TYPE);
        
        TileEntityCrank.TYPE = create(BlockNames.CRANK, TileEntityCrank::new);
        event.getRegistry().register(TileEntityCrank.TYPE);
    }
    
    @SuppressWarnings("ConstantConditions")
    private static <T extends TileEntity> TileEntityType<T> create(@Nonnull String name, @Nonnull Supplier<T> supplier)
    {
        TileEntityType<T> type = new TileEntityType<>(supplier, null);
        type.setRegistryName(name);
        return type;
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        //ITEMS
        MineTechItems.DIRTY_ORE_DUST_IRON = new ItemDirtyOreDust(Materials.IRON);
        event.getRegistry().register(MineTechItems.DIRTY_ORE_DUST_IRON);
        
        MineTechItems.DIRTY_ORE_DUST_GOLD = new ItemDirtyOreDust(Materials.GOLD);
        event.getRegistry().register(MineTechItems.DIRTY_ORE_DUST_GOLD);
        
        //ITEM BLOCKS
        ItemBlock GRINDSTONE_ITEM = new ItemBlockGroup(MineTechBlocks.GRINDSTONE, ItemGroup.BUILDING_BLOCKS);
        event.getRegistry().register(GRINDSTONE_ITEM);
    
        ItemBlock CRANK_ITEM = new ItemBlockGroup(MineTechBlocks.CRANK, ItemGroup.BUILDING_BLOCKS);
        event.getRegistry().register(CRANK_ITEM);
    }
}
