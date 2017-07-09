package larsg310.mods.minetech.util;

import larsg310.mods.minetech.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RegistryUtil
{
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final List<Item> ITEMS = new ArrayList<>();
    
    public static void registerBlock(@Nonnull Block block)
    {
        registerBlock(block, new ItemBlock(block));
    }
    
    public static void registerBlock(@Nonnull Block block, ItemBlock item)
    {
        BLOCKS.add(block);
        if (item.getRegistryName() == null)
        {
            item.setRegistryName(block.getRegistryName());
        }
        registerItemBlock(item);
    }
    
    public static void registerItemBlock(@Nonnull ItemBlock item)
    {
        ITEMS.add(item);
    }
    
    public static void registerItem(@Nonnull Item item)
    {
        ITEMS.add(item);
    }
    
    public static void registerTileEntity(Class<? extends TileEntity> tile)
    {
        GameRegistry.registerTileEntity(tile, Reference.MOD_ID + ":" + tile.getSimpleName());
    }
}
