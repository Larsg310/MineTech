package wexalian.mods.minetech.recipe;

import wexalian.mods.minetech.MineTech;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class GrindstoneRecipes
{
    private static final GrindstoneRecipes INSTANCE = new GrindstoneRecipes();
    
    private final Map<ItemStack, ItemStack> grindMap = new HashMap<>();
    
    private GrindstoneRecipes()
    {
        addGrindingRecipe(Blocks.STONE, new ItemStack(Blocks.COBBLESTONE));
        addGrindingRecipe(Blocks.COBBLESTONE, new ItemStack(Blocks.GRAVEL));
        addGrindingRecipe(Blocks.GRAVEL, new ItemStack(Blocks.SAND));
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
                       .filter(entry -> compareItemStacks(stack, entry.getKey()))
                       .findFirst()
                       .map(Map.Entry::getValue)
                       .orElse(ItemStack.EMPTY);
        // @formatter:on
    }
    
    public boolean hasGrindingResult(@Nonnull ItemStack stack)
    {
        return !getGrindingResult(stack).isEmpty();
    }
    
    private boolean compareItemStacks(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == OreDictionary.WILDCARD_VALUE || stack2.getMetadata() == stack1.getMetadata());
    }
    
    public static GrindstoneRecipes instance()
    {
        return INSTANCE;
    }
}
