package com.wexalian.mods.minetech.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.function.Predicate;

public final class InventoryUtil
{
    public static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler handler)
    {
        for (int i = 0; i < handler.getSlots(); ++i)
        {
            ItemStack itemstack = handler.getStackInSlot(i);
            
            if (!itemstack.isEmpty())
            {
                WorldUtil.spawnItemStack(worldIn, pos, itemstack);
            }
        }
    }
    
    public static ItemStack findItemStack(IInventory inventory, Predicate<ItemStack> predicate)
    {
        return findItemStack(new InvWrapper(inventory), predicate);
    }
    
    public static ItemStack findItemStack(IItemHandler inventory, Predicate<ItemStack> predicate)
    {
        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (predicate.test(stack))
            {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
