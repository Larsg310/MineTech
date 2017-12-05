package wexalian.mods.minetech.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;
import wexalian.mods.minetech.api.capabilities.mechanical.IMechanicalEnergy;
import wexalian.mods.minetech.capability.Capabilities;
import wexalian.mods.minetech.recipe.GrindstoneRecipes;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.IntStream;

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
        TileEntity tile = world.getTileEntity(pos.up());
        if (tile != null && tile.hasCapability(Capabilities.MECHANICAL_ENERGY, EnumFacing.DOWN))
        {
            IMechanicalEnergy energy = tile.getCapability(Capabilities.MECHANICAL_ENERGY, EnumFacing.DOWN);
            assert energy != null : "retrieved null capability when hasCapability returned true. this is not good";
            for (int slot = 0; slot < inventory.getSlots(); slot++)
            {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (GrindstoneRecipes.instance().hasGrindingResult(stack))
                {
                    NBTTagCompound tag = Optional.ofNullable(stack.getTagCompound()).orElse(new NBTTagCompound());
                    int progress = tag.getInteger("minetech:progress");
                    progress += energy.getRPM();
                    tag.setInteger("minetech:progress", progress);
                    stack.setTagCompound(tag);
                }
            }
            progress += energy.getRPM();
            if (progress >= MAX_PROGRESS) progress -= MAX_PROGRESS;
            
        }
    }
    
    private boolean canFinish()
    {
        //@formatter:off
        return IntStream.range(0, inventory.getSlots()).mapToObj(slot -> inventory.getStackInSlot(slot))
                        .map(stack -> Optional.ofNullable(stack.getTagCompound()).orElse(new NBTTagCompound()))
                        .mapToInt(tag -> tag.getInteger("minetech:progress"))
                        .anyMatch(progress -> progress >= MAX_PROGRESS);
        //@formatter:on
    }
    
    private void processFinish()
    {
        for (int slot = 0; slot < inventory.getSlots(); slot++)
        {
            ItemStack stack = inventory.getStackInSlot(slot);
            NBTTagCompound tag = Optional.ofNullable(stack.getTagCompound()).orElse(new NBTTagCompound());
            int progress = tag.getInteger("minetech:progress");
            if (progress >= MAX_PROGRESS)
            {
                ItemStack result = GrindstoneRecipes.instance().getGrindingResult(stack).copy();
                if (!result.isEmpty()) inventory.setStackInSlot(slot, result);
            }
            
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
}
