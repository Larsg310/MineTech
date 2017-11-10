package wexalian.mods.minetech.capability;

import wexalian.mods.minetech.api.capabilities.mechanical.IMechanicalEnergy;
import wexalian.mods.minetech.api.capabilities.mechanical.MechanicalEnergyHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityMechanicalEnergy
{
    @CapabilityInject(IMechanicalEnergy.class)
    public static Capability<IMechanicalEnergy> MECHANICAL_ENERGY_CAPABILITY = null;
    
    public static void register()
    {
        CapabilityManager.INSTANCE.register(IMechanicalEnergy.class, new Capability.IStorage<IMechanicalEnergy>()
        {
            @Override
            public NBTBase writeNBT(Capability<IMechanicalEnergy> capability, IMechanicalEnergy instance, EnumFacing side)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("rpm", instance.getRPM());
                tag.setInteger("torque", instance.getTorque());
                return tag;
            }
            
            @Override
            public void readNBT(Capability<IMechanicalEnergy> capability, IMechanicalEnergy instance, EnumFacing side, NBTBase nbt)
            {
                NBTTagCompound tag = (NBTTagCompound) nbt;
                instance.setRMP(tag.getInteger("rpm"));
                instance.setTorque(tag.getInteger("torque"));
            }
        }, MechanicalEnergyHandler::new);
    }
}
