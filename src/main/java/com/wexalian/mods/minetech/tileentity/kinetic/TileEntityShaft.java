package com.wexalian.mods.minetech.tileentity.kinetic;

import com.wexalian.mods.minetech.api.MineTechAPI;
import com.wexalian.mods.minetech.api.energy.kinetic.IKineticNode;
import com.wexalian.mods.minetech.api.energy.kinetic.IShaftAttachable;
import com.wexalian.mods.minetech.block.BlockShaft;
import com.wexalian.mods.minetech.energy.kinetic.KineticNode;
import com.wexalian.mods.minetech.util.KinesisUtil;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class TileEntityShaft extends TileEntityHost<IKineticNode, Float>
{
    public static TileEntityType<TileEntityShaft> TYPE;
    
    private KineticNode node = new KineticNode(this, 0.5F);
    
    public TileEntityShaft()
    {
        super(TYPE);
    }
    
    @SuppressWarnings({"unchecked", "Duplicates"})
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side)
    {
        if (cap == MineTechAPI.Kinesis.SHAFT_CAPABILITY)
        {
            Objects.requireNonNull(side);
            if (side.getAxis() == getBlockState().get(BlockShaft.AXIS))
            {
                return LazyOptional.of(() -> (T) (IShaftAttachable) () -> node);
            }
        }
        return super.getCapability(cap, side);
    }
    
    @SuppressWarnings("ConstantConditions")
    @Override
    public void addNeighbours(BiConsumer<IKineticNode, Float> nodeConsumer, BiPredicate<World, BlockPos> posValidator)
    {
        KinesisUtil.findAndAddShafts(world, pos, getBlockState().get(BlockShaft.AXIS), node.getValue(), nodeConsumer, posValidator);
        KinesisUtil.findAndAddGears(world, pos, getBlockState().get(BlockShaft.AXIS), node.getValue(), nodeConsumer, posValidator);
    }
    
    @Override
    public IKineticNode getNode()
    {
        return node;
    }
    
    @Nonnull
    public EnumFacing getRotationFacing()
    {
        return EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, getBlockState().get(BlockShaft.AXIS));
    }
    
    @Override
    public String toString()
    {
        return Objects.toString(node);
    }
}
