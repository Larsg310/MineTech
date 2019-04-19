package com.wexalian.mods.minetech.crafting;

import com.google.gson.JsonObject;
import com.wexalian.mods.minetech.init.MineTechRecipes;
import com.wexalian.mods.minetech.lib.Reference;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.RecipeType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class GrindingRecipe implements IRecipe
{
    public static final ResourceLocation NAME = new ResourceLocation(Reference.MOD_ID, "grinding");
    public static final IRecipeSerializer<GrindingRecipe> SERIALIZER = RecipeSerializers.register(new Serializer());
    
    public static Predicate<ItemStack> IS_INGREDIENT = s -> false;
    private final Ingredient ingredient;
    private final ItemStack result;
    private ResourceLocation recipeId;
    
    private GrindingRecipe(ResourceLocation recipeId, Ingredient ingredient, ItemStack result)
    {
        this.recipeId = recipeId;
        this.ingredient = ingredient;
        this.result = result;
        
        IS_INGREDIENT = IS_INGREDIENT.or(ingredient);
    }
    
    public boolean matches(@Nonnull IInventory inv, @Nonnull World worldIn)
    {
        return this.ingredient.test(inv.getStackInSlot(0));
    }
    
    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull IInventory inv)
    {
        return result.copy();
    }
    
    @Override
    public boolean canFit(int width, int height)
    {
        return true;
    }
    
    @Nonnull
    @Override
    public ItemStack getRecipeOutput()
    {
        return result;
    }
    
    @Nonnull
    @Override
    public ResourceLocation getId()
    {
        return recipeId;
    }
    
    @Nonnull
    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }
    
    @Nullable
    @Override
    public RecipeType<? extends IRecipe> getType()
    {
        return MineTechRecipes.GRINDING;
    }
    
    public static class Serializer implements IRecipeSerializer<GrindingRecipe>
    {
        @Nonnull
        public GrindingRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
        {
            Ingredient ingredient = Ingredient.deserialize(JsonUtils.getJsonObject(json, "ingredient"));
            
            ItemStack result = ShapedRecipe.deserializeItem(JsonUtils.getJsonObject(json, "result"));
            return new GrindingRecipe(recipeId, ingredient, result);
        }
        
        @Nonnull
        public GrindingRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
        {
            Ingredient ingredient = Ingredient.read(buffer);
            ItemStack itemstack = buffer.readItemStack();
            return new GrindingRecipe(recipeId, ingredient, itemstack);
        }
        
        public void write(@Nonnull PacketBuffer buffer, @Nonnull GrindingRecipe recipe)
        {
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
        
        @Nonnull
        @Override
        public ResourceLocation getName()
        {
            return NAME;
        }
    }
}
