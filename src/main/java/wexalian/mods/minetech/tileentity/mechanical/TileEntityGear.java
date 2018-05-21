package wexalian.mods.minetech.tileentity.mechanical;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import wexalian.mods.minetech.block.BlockGear;
import wexalian.mods.minetech.kinesis.IGearAttachable;
import wexalian.mods.minetech.kinesis.IKineticNode;
import wexalian.mods.minetech.kinesis.IShaftAttachable;
import wexalian.mods.minetech.kinesis.KineticNode;
import wexalian.mods.minetech.util.ObjFloatConsumer;

import javax.annotation.Nonnull;
import java.util.function.BiPredicate;

public class TileEntityGear extends TileEntityRotatingNode
{
    private IKineticNode node = new KineticNode(this);
    
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
    public void addNeighbours(ObjFloatConsumer<IKineticNode> neighbours, BiPredicate<World, BlockPos> posValidator)
    {
        EnumFacing face = getWorld().getBlockState(getPos()).getValue(BlockGear.FACING);
        KineticNode.findShaft(getWorld(), getPos(), face, 1F, neighbours, posValidator);
        for (EnumFacing side : EnumFacing.VALUES)
        {
            if (side.getAxis() == face.getAxis()) continue;
            KineticNode.findGear(getWorld(), getPos(), face, side, -1, neighbours, posValidator);
        }
    }
    
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
    {
        EnumFacing face = getWorld().getBlockState(getPos()).getValue(BlockGear.FACING);
        
        if (capability == IShaftAttachable.CAPABILITY && facing == face) return true;
        else if (capability == IGearAttachable.CAPABILITY && facing.getAxis() != face.getAxis()) return true;// TODO: Make pretty
        return super.hasCapability(capability, facing);
    }
    
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
    {
        EnumFacing face = getWorld().getBlockState(getPos()).getValue(BlockGear.FACING);
        
        if (capability == IShaftAttachable.CAPABILITY && facing == face) return (T) (IShaftAttachable) () -> node;
        else if (capability == IGearAttachable.CAPABILITY && facing.getAxis() != face.getAxis()) return (T) (IGearAttachable) f -> f == face ? node : null;
        
        return super.getCapability(capability, facing);
    }
    
    @Override
    public float getAngle(float partialTicks)
    {
        return node.getAngle(partialTicks);
    }
    
    @Nonnull
    @Override
    public EnumFacing.Axis getRotationAxis()
    {
        return getWorld().getBlockState(getPos()).getValue(BlockGear.FACING).getAxis();
    }
}
