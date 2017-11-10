package wexalian.mods.minetech.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.ItemStackHandler;
import wexalian.mods.minetech.api.capabilities.mechanical.MechanicalEnergyHandler;
import wexalian.mods.minetech.capability.CapabilityMechanicalEnergy;
import wexalian.mods.minetech.recipe.GrindstoneRecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityGrindstone extends TileEntity implements ITickable
{
    public static final int MAX_PROGRESS = 160;
    
    public ItemStackHandler inventory = new ItemStackHandler(8)
    {
        @Override
        public int getSlotLimit(int slot)
        {
            return 1;
        }
    };
    
    private MechanicalEnergyHandler energy = new MechanicalEnergyHandler();
    
    private int progress = 0;
    
    public TileEntityGrindstone()
    {
        energy.setRMP(1);
    }
    
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
        progress += energy.getRPM();
    }
    
    private boolean canFinish()
    {
        return progress >= MAX_PROGRESS;
    }
    
    private void processFinish()
    {
        progress -= MAX_PROGRESS;
        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            ItemStack stack = GrindstoneRecipes.instance().getGrindingResult(inventory.getStackInSlot(slot)).copy();
            if (!stack.isEmpty()) inventory.setStackInSlot(slot, stack);
        }
    }
    
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setTag("inventory", inventory.serializeNBT());
        nbt.setInteger("progress", progress);
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
        progress = nbt.getInteger("progress");
    }
    
    public int getProgress()
    {
        return progress;
    }
    
    public void setProgress(int progress)
    {
        this.progress = progress;
    }
    
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityMechanicalEnergy.MECHANICAL_ENERGY_CAPABILITY || super.hasCapability(capability, facing);
    }
    
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityMechanicalEnergy.MECHANICAL_ENERGY_CAPABILITY) return CapabilityMechanicalEnergy.MECHANICAL_ENERGY_CAPABILITY.cast(energy);
        return super.getCapability(capability, facing);
    }
}
