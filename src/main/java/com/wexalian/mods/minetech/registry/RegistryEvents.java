package com.wexalian.mods.minetech.registry;

import com.wexalian.mods.minetech.MineTechMod;
import com.wexalian.mods.minetech.api.block.MineTechBlocks;
import com.wexalian.mods.minetech.block.*;
import com.wexalian.mods.minetech.item.ItemCleanOreDust;
import com.wexalian.mods.minetech.item.ItemDirtyOreDust;
import com.wexalian.mods.minetech.item.ItemPan;
import com.wexalian.mods.minetech.item.block.ItemBlockGear;
import com.wexalian.mods.minetech.item.block.ItemBlockGroup;
import com.wexalian.mods.minetech.lib.BlockNames;
import com.wexalian.mods.minetech.lib.Materials;
import com.wexalian.mods.minetech.lib.Reference;
import com.wexalian.mods.minetech.tileentity.TileEntityBelt;
import com.wexalian.mods.minetech.tileentity.TileEntityCrank;
import com.wexalian.mods.minetech.tileentity.TileEntityGrindstone;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityGear;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityShaft;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityWaterWheel;
import com.wexalian.mods.minetech.tileentity.multiblock.TileEntityOreMill;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class RegistryEvents
{
    static
    {
        OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(new BlockGrindstone());
        event.getRegistry().register(new BlockCrank());
        event.getRegistry().register(new BlockBelt());
        event.getRegistry().register(new BlockWaterWheel());
        event.getRegistry().register(new BlockShaft());
        event.getRegistry().register(new BlockGear());
        event.getRegistry().register(new BlockOreMill());
    }
    
    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        //MATERIALS
        event.getRegistry().register(new ItemDirtyOreDust(Materials.IRON));
        event.getRegistry().register(new ItemDirtyOreDust(Materials.GOLD));
        event.getRegistry().register(new ItemCleanOreDust(Materials.IRON));
        event.getRegistry().register(new ItemCleanOreDust(Materials.GOLD));
        
        //TOOLS
        event.getRegistry().register(new ItemPan());
        
        //ITEM BLOCKS
        event.getRegistry().register(new ItemBlockGroup(MineTechBlocks.GRINDSTONE, MineTechMod.ITEM_GROUP));
        event.getRegistry().register(new ItemBlockGroup(MineTechBlocks.CRANK, MineTechMod.ITEM_GROUP));
        event.getRegistry().register(new ItemBlockGroup(MineTechBlocks.LEATHER_BELT, MineTechMod.ITEM_GROUP));
        event.getRegistry().register(new ItemBlockGroup(MineTechBlocks.WATER_WHEEL, MineTechMod.ITEM_GROUP));
        event.getRegistry().register(new ItemBlockGroup(MineTechBlocks.SHAFT, MineTechMod.ITEM_GROUP));
        event.getRegistry().register(new ItemBlockGear(MineTechBlocks.GEAR, MineTechMod.ITEM_GROUP));
        event.getRegistry().register(new ItemBlockGroup(MineTechBlocks.ORE_MILL, MineTechMod.ITEM_GROUP));
    }
    
    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event)
    {
        event.getRegistry().register(TileEntityGrindstone.TYPE = create(BlockNames.GRINDSTONE, TileEntityGrindstone::new));
        event.getRegistry().register(TileEntityCrank.TYPE = create(BlockNames.CRANK, TileEntityCrank::new));
        event.getRegistry().register(TileEntityBelt.TYPE = create(BlockNames.LEATHER_BELT, TileEntityBelt::new));
        event.getRegistry().register(TileEntityWaterWheel.TYPE = create(BlockNames.WATER_WHEEL, TileEntityWaterWheel::new));
        event.getRegistry().register(TileEntityShaft.TYPE = create(BlockNames.SHAFT, TileEntityShaft::new));
        event.getRegistry().register(TileEntityGear.TYPE = create(BlockNames.GEAR, TileEntityGear::new));
        event.getRegistry().register(TileEntityOreMill.TYPE = create(BlockNames.ORE_MILL, TileEntityOreMill::new));
    }
    
    @SuppressWarnings("ConstantConditions")
    private static <T extends TileEntity> TileEntityType<T> create(@Nonnull ResourceLocation name, @Nonnull Supplier<T> supplier)
    {
        TileEntityType<T> type = new TileEntityType<>(supplier, null);
        type.setRegistryName(name);
        return type;
    }
    
}
