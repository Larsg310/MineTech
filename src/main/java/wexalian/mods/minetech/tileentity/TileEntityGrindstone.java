package wexalian.mods.minetech.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;
import wexalian.mods.minetech.recipe.GrindstoneRecipes;
import wexalian.mods.minetech.util.WorldUtil;

import javax.annotation.Nonnull;

public class TileEntityGrindstone extends TileEntity implements ITickable
{
    public static final int MAX_PROGRESS = 160;
    
    public static final String NBT_KEY_PROGRESS = "minetech:progress";
    public static final String NBT_KEY_INVENTORY = "minetech:inventory";
    
    public ItemStackHandler inventory = new ItemStackHandler(8)
    {
        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }
    };
    
    private int progress = 0;
    
    @Override
    public void update()
    {
        processTick();
        if (canFinish())
        {
            processFinish();
        }
    }
    
    private void processTick()
    {
        TileEntity tile = WorldUtil.getTileEntity(world, pos.up());
        if (tile instanceof TileEntityCrank)
        {
            if (((TileEntityCrank) tile).isCranking()) progress += 1;
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
    
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setTag(NBT_KEY_INVENTORY, inventory.serializeNBT());
        nbt.setInteger(NBT_KEY_PROGRESS, progress);
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        inventory.deserializeNBT(nbt.getCompoundTag(NBT_KEY_INVENTORY));
        progress = nbt.getInteger(NBT_KEY_PROGRESS);
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
