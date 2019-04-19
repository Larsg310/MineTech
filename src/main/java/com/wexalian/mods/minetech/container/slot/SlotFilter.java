package com.wexalian.mods.minetech.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class SlotFilter extends SlotItemHandler
{
    private final Predicate<ItemStack> filter;
    
    public SlotFilter(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> filter)
    {
        super(itemHandler, index, xPosition, yPosition);
        this.filter = filter;
    }
    
    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        return filter.test(stack) && super.isItemValid(stack);
    }
}
