package wexalian.mods.minetech.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import wexalian.mods.minetech.item.ItemOreDust;
import wexalian.mods.minetech.metal.Metals;

public class SmeltingRecipes
{
    @SuppressWarnings("MagicNumber")
    public static void init()
    {
        FurnaceRecipes.instance().addSmeltingRecipe(ItemOreDust.getFromMetal(Metals.IRON), new ItemStack(Items.IRON_INGOT), 0F);
        FurnaceRecipes.instance().addSmeltingRecipe(ItemOreDust.getFromMetal(Metals.GOLD), new ItemStack(Items.GOLD_INGOT), 0F);
    }
}
