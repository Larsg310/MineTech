package com.wexalian.mods.minetech.tileentity;

import com.wexalian.mods.minetech.container.IDummyInventory;
import com.wexalian.mods.minetech.init.MineTechRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileEntityGrindstone extends TileEntity implements ITickable
{
    public static final int MAX_PROGRESS = 160;
    private static final String NBT_KEY_PROGRESS_ARRAY = "minetech:progress_array";
    private static final String NBT_KEY_PROGRESS_WHEEL = "minetech:progress_wheel";
    private static final String NBT_KEY_INVENTORY = "minetech:inventory";
    public static TileEntityType<TileEntityGrindstone> TYPE;
    
    private ItemStackHandler inventory = new ItemStackHandler(8)
    {
        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }
    };
    
    private IInventory[] recipeInventories = new IInventory[8];
    
    private int[] progress = new int[8];
    private int wheelProgress = 0;
    
    public TileEntityGrindstone()
    {
        super(TYPE);
        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            recipeInventories[slot] = new DummyInventory(slot);
        }
    }
    
    @Override
    public void tick()
    {
        if (getWorld() != null)
        {
            TileEntity tile = getWorld().getTileEntity(pos.up());
            if (tile instanceof TileEntityCrank)
            {
                if (((TileEntityCrank) tile).isCranking())
                {
                    for (int slot = 0; slot < inventory.getSlots(); slot++)
                    {
                        tickProcess(slot);
                        if (canFinish(slot))
                        {
                            finishProcess(slot);
                        }
                    }
                    wheelProgress = (wheelProgress + 1) % MAX_PROGRESS;
                }
            }
        }
    }
    
    private void tickProcess(int slot)
    {
        if (inventory.getStackInSlot(slot).isEmpty()) progress[slot] = 0;
        else progress[slot]++;
    }
    
    private boolean canFinish(int slot)
    {
        return progress[slot] >= MAX_PROGRESS;
    }
    
    private void finishProcess(int slot)
    {
        IRecipe recipe = this.world.getRecipeManager().getRecipe(recipeInventories[slot], this.world, MineTechRecipes.GRINDING);
        if (recipe != null)
        {
            ItemStack result = recipe.getCraftingResult(recipeInventories[slot]);
            
            if (!result.isEmpty()) inventory.setStackInSlot(slot, result);
            progress[slot] -= MAX_PROGRESS;
        }
    }
    
    @Override
    public void read(NBTTagCompound nbt)
    {
        super.read(nbt);
        inventory.deserializeNBT(nbt.getCompound(NBT_KEY_INVENTORY));
        progress = nbt.getIntArray(NBT_KEY_PROGRESS_ARRAY);
        if (progress.length == 0) progress = new int[8];
        wheelProgress = nbt.getInt(NBT_KEY_PROGRESS_WHEEL);
    }
    
    @Nonnull
    @Override
    public NBTTagCompound write(NBTTagCompound nbt)
    {
        super.write(nbt);
        nbt.put(NBT_KEY_INVENTORY, inventory.serializeNBT());
        nbt.putIntArray(NBT_KEY_PROGRESS_ARRAY, progress);
        nbt.putInt(NBT_KEY_PROGRESS_WHEEL, wheelProgress);
        return nbt;
    }
    
    boolean canGrind()
    {
        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            IRecipe recipe = this.world.getRecipeManager().getRecipe(recipeInventories[slot], this.world, MineTechRecipes.GRINDING);
            if (recipe != null)
            {
                ItemStack result = recipe.getCraftingResult(recipeInventories[slot]);
                
                if (!result.isEmpty()) return true;
            }
        }
        return false;
    }
    
    public void setProgress(int wheelProgress)
    {
        this.wheelProgress = wheelProgress;
    }
    
    public ItemStackHandler getInventory()
    {
        return inventory;
    }
    
    public int getWheelProgress()
    {
        return wheelProgress;
    }
    
    private class DummyInventory implements IDummyInventory
    {
        // private IItemHandler handler;
        private int slot;
        
        DummyInventory(int slot)
        {
            this.slot = slot;
        }
        
        @Nonnull
        @Override
        public ITextComponent getName()
        {
            return new TextComponentString("Grindstone");
        }
        
        @Override
        public int getSizeInventory()
        {
            return 1;
        }
        
        @Override
        public boolean isEmpty()
        {
            return inventory.getStackInSlot(slot).isEmpty();
        }
        
        @Nonnull
        @Override
        public ItemStack getStackInSlot(int index)
        {
            return inventory.getStackInSlot(slot);
        }
        
        @Nonnull
        @Override
        public ItemStack decrStackSize(int index, int count)
        {
            return inventory.extractItem(slot, count, false);
        }
        
        @Nonnull
        @Override
        public ItemStack removeStackFromSlot(int index)
        {
            ItemStack stack = inventory.getStackInSlot(slot);
            inventory.setStackInSlot(slot, ItemStack.EMPTY);
            return stack;
        }
        
        @Override
        public void setInventorySlotContents(int index, @Nonnull ItemStack stack)
        {
            inventory.setStackInSlot(slot, stack);
        }
        
        @Override
        public int getInventoryStackLimit()
        {
            return inventory.getSlotLimit(slot);
        }
        
        @Override
        public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack)
        {
            return inventory.isItemValid(slot, stack);
        }
        
    }
}