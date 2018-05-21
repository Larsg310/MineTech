package wexalian.mods.minetech.kinesis;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import wexalian.mods.minetech.capability.SimpleCapability;

public interface IGearAttachable
{
    @SimpleCapability
    @CapabilityInject(IGearAttachable.class)
    Capability<IGearAttachable> CAPABILITY = null;
    
    IKineticNode getNode(EnumFacing face);
}
