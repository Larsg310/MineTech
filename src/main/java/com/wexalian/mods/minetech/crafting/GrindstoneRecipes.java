package com.wexalian.mods.minetech.crafting;

import com.wexalian.mods.minetech.init.MineTechItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import com.wexalian.mods.minetech.MineTech;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class GrindstoneRecipes
{
    private static final GrindstoneRecipes INSTANCE = new GrindstoneRecipes();
    
    private final Map<ItemStack, ItemStack> grindMap = new HashMap<>();
    
    private GrindstoneRecipes()
    {
        addGrindingRecipe(Blocks.IRON_ORE, new ItemStack(MineTechItems.DIRTY_ORE_DUST_IRON, 2));
        addGrindingRecipe(Blocks.GOLD_ORE, new ItemStack(MineTechItems.DIRTY_ORE_DUST_GOLD, 2));
    }
    
    public void addGrindingRecipe(Block input, @Nonnull ItemStack output)
    {
        addGrindingRecipe(new ItemStack(input), output);
    }
    
    public void addGrindingRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output)
    {
        if (hasGrindingResult(input))
        {
            MineTech.LOGGER.info("Ignored grinding recipe with conflicting input: {} = {}", input, output);
            return;
        }
        grindMap.put(input, output);
    }
    
    public boolean hasGrindingResult(@Nonnull ItemStack stack)
    {
        return !getGrindingResult(stack).isEmpty();
    }
    
    public ItemStack getGrindingResult(@Nonnull ItemStack stack)
    {
        return grindMap.entrySet()//
                       .stream()//
                       .filter(entry -> entry.getKey().getItem() == stack.getItem())//
                       .findFirst()//
                       .map(Map.Entry::getValue)//
                       .orElse(ItemStack.EMPTY);
    }
    
    public void addGrindingRecipe(Item input, @Nonnull ItemStack output)
    {
        addGrindingRecipe(new ItemStack(input), output);
    }
    
    public static GrindstoneRecipes instance()
    {
        return INSTANCE;
    }
}
