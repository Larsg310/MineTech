package com.wexalian.mods.minetech.item;

import com.wexalian.mods.minetech.MineTechMod;
import com.wexalian.mods.minetech.container.IDummyInventory;
import com.wexalian.mods.minetech.crafting.WashingRecipe;
import com.wexalian.mods.minetech.init.MineTechRecipes;
import com.wexalian.mods.minetech.lib.ItemNames;
import com.wexalian.mods.minetech.lib.Reference;
import com.wexalian.mods.minetech.util.InventoryUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPan extends Item implements IItemHighlighter
{
    
    private static final String NBT_KEY_RECIPE_ID = Reference.MOD_ID + ":type";
    private static final String NBT_KEY_PROGRESS = Reference.MOD_ID + ":progress";
    private static final String NBT_KEY_NAME = Reference.MOD_ID + ":name";
    
    private static final String TOOLTIP_KEY_PROGRESS = "tooltip.minetech.progress";
    private static final String TOOLTIP_KEY_PANNING = "tooltip.minetech.panning";
    
    private static int START_PROGRESS = 4;
    
    public ItemPan()
    {
        super(new Properties().group(MineTechMod.ITEM_GROUP));
        setRegistryName(ItemNames.PAN);
    }
    
    @SuppressWarnings("ConstantConditions")
    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
    {
        RayTraceResult trace = rayTrace(world, player, true);
        
        ItemStack heldStack = player.getHeldItem(hand);
        if (trace != null && trace.type == RayTraceResult.Type.BLOCK)
        {
            if (world.getFluidState(trace.getBlockPos()).getFluid() instanceof WaterFluid)
            {
                NBTTagCompound tag = heldStack.getTag();
                if (tag != null && tag.contains(NBT_KEY_PROGRESS) && tag.contains(NBT_KEY_RECIPE_ID))
                {
                    int progress = tag.getInt(NBT_KEY_PROGRESS);
                    
                    if (progress > 0)
                    {
                        progress--;
                        if (progress == 0)
                        {
                            ResourceLocation id = new ResourceLocation(tag.getString(NBT_KEY_RECIPE_ID));
                            
                            IRecipe recipe = world.getRecipeManager().getRecipe(id);
                            if (recipe != null)
                            {
                                ItemStack result = recipe.getCraftingResult(null);
                                
                                if (!world.isRemote)
                                {
                                    ItemHandlerHelper.giveItemToPlayer(player, result);
                                }
                                tag.remove(NBT_KEY_RECIPE_ID);
                                tag.remove(NBT_KEY_PROGRESS);
                                tag.remove(NBT_KEY_NAME);
                            }
                            
                        }
                        else tag.putInt(NBT_KEY_PROGRESS, progress);
                        
                        heldStack.setTag(tag);
                        return ActionResult.newResult(EnumActionResult.SUCCESS, heldStack);
                    }
                }
                else
                {
                    ItemStack stack = InventoryUtil.findItemStack(player.inventory, WashingRecipe.IS_INGREDIENT);
                    if (!stack.isEmpty())
                    {
                        if (tag == null) tag = new NBTTagCompound();
                        
                        IRecipe recipe = world.getRecipeManager().getRecipe(new DummyInventory(stack), world, MineTechRecipes.WASHING);
                        if (recipe != null)
                        {
                            tag.putString(NBT_KEY_RECIPE_ID, recipe.getId().toString());
                            tag.putString(NBT_KEY_NAME, stack.getDisplayName().getFormattedText());
                            tag.putInt(NBT_KEY_PROGRESS, START_PROGRESS);
                            
                            if (!player.isCreative()) stack.shrink(1);
                            
                            heldStack.setTag(tag);
                            
                            return ActionResult.newResult(EnumActionResult.SUCCESS, heldStack);
                        }
                    }
                }
            }
        }
        
        return ActionResult.newResult(EnumActionResult.PASS, heldStack);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        NBTTagCompound tag = stack.getTag();
        if (tag != null && tag.contains(NBT_KEY_RECIPE_ID) && tag.contains(NBT_KEY_PROGRESS) && tag.contains(NBT_KEY_NAME))
        {
            tooltip.add(new TextComponentString(I18n.format(TOOLTIP_KEY_PANNING, tag.getString(NBT_KEY_NAME))));
            
            int progress = tag.getInt(NBT_KEY_PROGRESS);
            tooltip.add(new TextComponentString(I18n.format(TOOLTIP_KEY_PROGRESS, (START_PROGRESS - progress) * 100 / START_PROGRESS)));
        }
    }
    
    @Override
    public boolean shouldHighlight(ItemStack stack)
    {
        return WashingRecipe.IS_INGREDIENT.test(stack);
    }
    
    private class DummyInventory implements IDummyInventory
    {
        private ItemStack stack;
        
        private DummyInventory(ItemStack stack)
        {
            this.stack = stack;
        }
        
        @Nonnull
        @Override
        public ITextComponent getName()
        {
            return new TextComponentString("Pan");
        }
        
        @Override
        public int getSizeInventory()
        {
            return 1;
        }
        
        @Override
        public boolean isEmpty()
        {
            return stack.isEmpty();
        }
        
        @Nonnull
        @Override
        public ItemStack getStackInSlot(int index)
        {
            return stack;
        }
        
        @Nonnull
        @Override
        public ItemStack decrStackSize(int index, int count)
        {
            return stack;
        }
        
        @Nonnull
        @Override
        public ItemStack removeStackFromSlot(int index)
        {
            return stack;
        }
        
        @Override
        public void setInventorySlotContents(int index, @Nonnull ItemStack stack)
        {
        }
        
        @Override
        public int getInventoryStackLimit()
        {
            return 1;
        }
        
        @Override
        public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack)
        {
            return true;
        }
    }
}
