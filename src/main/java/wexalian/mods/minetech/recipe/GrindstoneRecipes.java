package wexalian.mods.minetech.recipe;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import wexalian.mods.minetech.MineTech;
import wexalian.mods.minetech.item.ItemDirtyOreDust;
import wexalian.mods.minetech.metal.Metals;
import wexalian.mods.minetech.util.InventoryUtil;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class GrindstoneRecipes
{
    private static final GrindstoneRecipes INSTANCE = new GrindstoneRecipes();

    private final Map<ItemStack, ItemStack> grindMap = new HashMap<>();

    private GrindstoneRecipes()
    {
        addGrindingRecipe(Blocks.IRON_ORE, ItemDirtyOreDust.getFromMetal(Metals.IRON,2));
        addGrindingRecipe(Blocks.GOLD_ORE, ItemDirtyOreDust.getFromMetal(Metals.GOLD,2));
    }

    public void addGrindingRecipe(Block input, @Nonnull ItemStack output)
    {
        addGrindingRecipe(new ItemStack(input), output);
    }

    public void addGrindingRecipe(Item input, @Nonnull ItemStack output)
    {
        addGrindingRecipe(new ItemStack(input), output);
    }

    public void addGrindingRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output)
    {
        if (hasGrindingResult(input))
        {
            MineTech.log.info("Ignored grinding recipe with conflicting input: {} = {}", input, output);
            return;
        }
        grindMap.put(input, output);
    }

    public ItemStack getGrindingResult(@Nonnull ItemStack stack)
    {
        // @formatter:off
        return grindMap.entrySet()
                       .stream()
                       .filter(entry -> InventoryUtil.compareItemStacks(stack, entry.getKey()))
                       .findFirst()
                       .map(Map.Entry::getValue)
                       .orElse(ItemStack.EMPTY);
        // @formatter:on
    }

    public boolean hasGrindingResult(@Nonnull ItemStack stack)
    {
        return !getGrindingResult(stack).isEmpty();
    }
    
    public static GrindstoneRecipes instance()
    {
        return INSTANCE;
    }
}
