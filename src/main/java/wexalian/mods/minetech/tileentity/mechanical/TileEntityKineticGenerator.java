package wexalian.mods.minetech.tileentity.mechanical;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.commons.lang3.tuple.Pair;
import wexalian.mods.minetech.kinesis.IKineticNode;
import wexalian.mods.minetech.kinesis.IShaftAttachable;
import wexalian.mods.minetech.kinesis.KineticNode;
import wexalian.mods.minetech.util.ObjFloatConsumer;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.BiPredicate;

public class TileEntityKineticGenerator extends TileEntityNode
{
    private KineticNode node = new KineticNode(this);
    
    @Override
    public IKineticNode getNode()
    {
        return node;
    }
    
    @Override
    public float getInertia()
    {
        return 4;
    }
    
    @Override
    public float getAppliedPower()
    {
        boolean shouldProducePower = Arrays.stream(EnumFacing.values())//
                                           .map(f -> Pair.of(f, getWorld().getTileEntity(getPos().offset(f))))//
                                           .filter(f -> f.getRight() != null)//
                                           .anyMatch(f -> f.getRight().hasCapability(IShaftAttachable.CAPABILITY, f.getLeft().getOpposite()));
        return shouldProducePower ? 50 : 0;
    }
    
    @Override
    public void addNeighbours(ObjFloatConsumer<IKineticNode> neighbours, BiPredicate<World, BlockPos> posValidator)
    {
        Arrays.stream(EnumFacing.values()).forEach(face -> KineticNode.findShaft(getWorld(), getPos(), face, 1F, neighbours, posValidator));
    }
    
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
    {
        if (capability == IShaftAttachable.CAPABILITY) return true;
        return super.hasCapability(capability, facing);
    }
    
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
    {
        if (capability == IShaftAttachable.CAPABILITY) return (T) (IShaftAttachable) () -> node;
        return super.getCapability(capability, facing);
    }
}
