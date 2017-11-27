package wexalian.mods.minetech.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import wexalian.mods.minetech.api.capabilities.mechanical.IMechanicalEnergy;
import wexalian.mods.minetech.api.capabilities.mechanical.MechanicalEnergyHandler;
import wexalian.mods.minetech.capability.Capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityCrank extends TileEntity implements ITickable
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
        return crankTime == 0;
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
}
