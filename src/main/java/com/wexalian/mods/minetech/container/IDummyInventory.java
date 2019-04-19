package com.wexalian.mods.minetech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IDummyInventory extends IInventory
{
    
    @Override
    default boolean hasCustomName()
    {
        return false;
    }
    
    @Nullable
    @Override
    default ITextComponent getCustomName()
    {
        return null;
    }
    
    @Override
    default void markDirty()
    {
    
    }
    
    @Override
    default boolean isUsableByPlayer(@Nonnull EntityPlayer player)
    {
        return true;
    }
    
    @Override
    default void openInventory(@Nonnull EntityPlayer player)
    {
    
    }
    
    @Override
    default void closeInventory(@Nonnull EntityPlayer player)
    {
    
    }
    
    @Override
    default int getField(int id)
    {
        return 0;
    }
    
    @Override
    default void setField(int id, int value)
    {
    
    }
    
    @Override
    default int getFieldCount()
    {
        return 0;
    }
    
    @Override
    default void clear()
    {
    
    }
}
