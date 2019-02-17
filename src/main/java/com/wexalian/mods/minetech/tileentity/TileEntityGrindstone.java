package com.wexalian.mods.minetech.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;
import com.wexalian.mods.minetech.crafting.GrindstoneRecipes;

import javax.annotation.Nonnull;

public class TileEntityGrindstone extends TileEntity implements ITickable
{
    public static final int MAX_PROGRESS = 160;
    public static final String NBT_KEY_PROGRESS = "minetech:progress";
    public static final String NBT_KEY_INVENTORY = "minetech:inventory";
    public static TileEntityType<TileEntityGrindstone> TYPE;
    private ItemStackHandler inventory = new ItemStackHandler(8)
    {
        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }
    };
    private int progress = 0;
    
    public TileEntityGrindstone()
    {
        super(TYPE);
    }
    
    @Override
    public void tick()
    {
        processTick();
        if (canFinish())
        {
            processFinish();
        }
    }
    
    private void processTick()
    {
        if (getWorld() != null)
        {
            // TileEntity tile = getWorld().getTileEntity(pos.up());
            // if (tile instanceof TileEntityCrank) //TODO
            // {
            //     if (((TileEntityCrank) tile).isCranking()) progress += 1;
            // }
        }
    }
    
    private boolean canFinish()
    {
        return progress >= MAX_PROGRESS;
    }
    
    private void processFinish()
    {
        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            ItemStack result = GrindstoneRecipes.instance().getGrindingResult(inventory.getStackInSlot(slot)).copy();
            if (!result.isEmpty()) inventory.setStackInSlot(slot, result);
        }
        progress -= MAX_PROGRESS;
    }
    
    @Override
    public void read(NBTTagCompound nbt)
    {
        super.read(nbt);
        inventory.deserializeNBT(nbt.getCompound(NBT_KEY_INVENTORY));
        progress = nbt.getInt(NBT_KEY_PROGRESS);
    }
    
    @Nonnull
    @Override
    public NBTTagCompound write(NBTTagCompound nbt)
    {
        super.write(nbt);
        nbt.put(NBT_KEY_INVENTORY, inventory.serializeNBT());
        nbt.putInt(NBT_KEY_PROGRESS, progress);
        return nbt;
    }
    
    public boolean canGrind()
    {
        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            if (GrindstoneRecipes.instance().hasGrindingResult(inventory.getStackInSlot(slot)))
            {
                return true;
            }
        }
        return false;
    }
    
    public ItemStackHandler getInventory()
    {
        return inventory;
    }
    
    public int getProgress()
    {
        return progress;
    }
    
    public void setProgress(int progress)
    {
        this.progress = progress;
    }
}
