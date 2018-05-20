package wexalian.mods.minetech.kinesis;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import wexalian.mods.minetech.capability.SimpleCapability;

public interface IShaftAttachable
{
    @SimpleCapability
    @CapabilityInject(IShaftAttachable.class)
    Capability<IShaftAttachable> CAPABILITY = null;
    
    IKineticNode getNode();
}
