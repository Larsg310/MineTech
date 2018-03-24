package wexalian.mods.minetech.kinesis;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IGearAttachable
{
    @CapabilityInject(IGearAttachable.class)
    public static final Capability<IGearAttachable> CAPABILITY = null;
    
    public IKineticNode getNode(EnumFacing faceSide);
    
}