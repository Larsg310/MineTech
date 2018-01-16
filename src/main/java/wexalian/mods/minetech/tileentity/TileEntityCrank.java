package wexalian.mods.minetech.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import wexalian.mods.minetech.api.capabilities.mechanical.IMechanicalEnergy;
import wexalian.mods.minetech.api.capabilities.mechanical.MechanicalEnergyHandler;
import wexalian.mods.minetech.capability.Capabilities;
import wexalian.mods.minetech.recipe.GrindstoneRecipes;
import wexalian.mods.minetech.util.IWorldUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class TileEntityCrank extends TileEntity implements ITickable, IWorldUtils
{
    public static final int MAX_CRANK_TIME = 20;
    
    private IMechanicalEnergy energy = new MechanicalEnergyHandler();
    
    private int crankTime = 0;
    
    @Override
    public void update()
    {
        if (crankTime > 0)
        {
            crankTime--;
            energy.setRPM(1);
        }
        else energy.setRPM(0);
    }
    
    public void tryCrank()
    {
        if (canCrank())
        {
            crank();
        }
    }
    
    private boolean canCrank()
    {
        if (crankTime != 0) return false;
        TileEntityGrindstone tile = getTileEntity(world, pos.down());
        //@formatter:off
        return IntStream.range(0, tile.inventory.getSlots())
                        .anyMatch(slot -> !GrindstoneRecipes.instance().getGrindingResult(tile.inventory.getStackInSlot(slot)).isEmpty());
        //@formatter:on
    }
    
    private void crank()
    {
        crankTime += MAX_CRANK_TIME;
    }
    
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == Capabilities.MECHANICAL_ENERGY || super.hasCapability(capability, facing);
    }
    
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == Capabilities.MECHANICAL_ENERGY) return Capabilities.MECHANICAL_ENERGY.cast(energy);
        return super.getCapability(capability, facing);
    }
    
    public int getCrankTime()
    {
        return crankTime;
    }
}
