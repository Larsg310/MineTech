package com.wexalian.mods.minetech.init;

import com.wexalian.mods.minetech.crafting.GrindingRecipe;
import com.wexalian.mods.minetech.crafting.WashingRecipe;
import net.minecraftforge.common.crafting.RecipeType;

public class MineTechRecipes
{
    public static final RecipeType<GrindingRecipe> GRINDING = RecipeType.get(GrindingRecipe.NAME, GrindingRecipe.class);
    
    public static final RecipeType<WashingRecipe> WASHING = RecipeType.get(WashingRecipe.NAME, WashingRecipe.class);
    
    public static void register() {}
}
