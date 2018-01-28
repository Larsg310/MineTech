package wexalian.mods.minetech.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import wexalian.mods.minetech.lib.FluidNames;

public class FluidMoltenIron extends Fluid
{
    public FluidMoltenIron()
    {
        super(FluidNames.MOLTEN_IRON, new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flowing"), 0xFF0000);
        setUnlocalizedName(FluidNames.MOLTEN_IRON);
    }
}
