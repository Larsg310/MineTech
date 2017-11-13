package wexalian.mods.minetech.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import wexalian.mods.minetech.api.capabilities.mechanical.IMechanicalEnergy;

public class Capabilities
{
    @CapabilityInject(IMechanicalEnergy.class)
    public static Capability<IMechanicalEnergy> MECHANICAL_ENERGY = null;

    public static void register()
    {
        CapabilityMechanicalEnergy.register();
    }
}
