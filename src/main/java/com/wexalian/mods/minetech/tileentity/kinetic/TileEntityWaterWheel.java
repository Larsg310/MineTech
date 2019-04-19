package com.wexalian.mods.minetech.tileentity.kinetic;

import com.wexalian.mods.minetech.api.MineTechAPI;
import com.wexalian.mods.minetech.api.energy.kinetic.IKineticNode;
import com.wexalian.mods.minetech.api.energy.kinetic.IShaftAttachable;
import com.wexalian.mods.minetech.block.BlockWaterWheel;
import com.wexalian.mods.minetech.energy.kinetic.KineticNode;
import com.wexalian.mods.minetech.util.KinesisUtil;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class TileEntityWaterWheel extends TileEntityHost<IKineticNode, Float> implements ITickable
{
    private static final float MAX_SPEED_TIME = 100; //ticks
    private static final float MAX_GENERATED_POWER = 20; //watt
    public static TileEntityType<TileEntityWaterWheel> TYPE;
    private KineticNode node = new KineticNode(this, 5F);
    private int speed = 0; //ticks
    private float ratio = 1F;
    
    public TileEntityWaterWheel()
    {
        super(TYPE);
    }
    
    @Override
    public void tick()
    {
        if (!world.isRemote)
        {
            if (node.getVelocity() > 15F)
            {
                node.setAppliedPower(node.getInertia());
            }
            else
            {
                EnumFacing.Axis axis = getBlockState().get(BlockWaterWheel.AXIS) == EnumFacing.Axis.X ? EnumFacing.Axis.Z : EnumFacing.Axis.X;
                
                BlockPos waterPos1 = getPos().down().down();
                BlockPos waterPos2 = waterPos1.offset(EnumFacing.getFacingFromAxisDirection(axis, EnumFacing.AxisDirection.POSITIVE));
                BlockPos waterPos3 = waterPos1.offset(EnumFacing.getFacingFromAxisDirection(axis, EnumFacing.AxisDirection.NEGATIVE));
                
                Vec3d waterFlow1 = world.getFluidState(waterPos1).getFlow(world, waterPos1).normalize();
                Vec3d waterFlow2 = world.getFluidState(waterPos2).getFlow(world, waterPos2).normalize();
                Vec3d waterFlow3 = world.getFluidState(waterPos3).getFlow(world, waterPos3).normalize();
                
                double axisFlow = 0D;
                axisFlow += axis.getCoordinate(waterFlow1.x, waterFlow1.y, waterFlow1.z);
                axisFlow += axis.getCoordinate(waterFlow2.x, waterFlow2.y, waterFlow2.z);
                axisFlow += axis.getCoordinate(waterFlow2.x, waterFlow3.y, waterFlow3.z);
                
                int newSpeed = speed + (int) axisFlow;
                if (newSpeed > 0) newSpeed -= 1;
                if (newSpeed < 0) newSpeed += 1;
                ratio = newSpeed < 0 ? 1F : -1F;
                if (newSpeed < MAX_SPEED_TIME && newSpeed > -MAX_SPEED_TIME)
                {
                    speed = newSpeed;
                    node.setAppliedPower(Math.abs(speed) / MAX_SPEED_TIME * MAX_GENERATED_POWER);
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side)
    {
        if (cap == MineTechAPI.Kinesis.SHAFT_CAPABILITY)
        {
            Objects.requireNonNull(side);
            if (side.getAxis() == getBlockState().get(BlockWaterWheel.AXIS))
            {
                return LazyOptional.of(() -> (T) (IShaftAttachable) () -> node);
            }
        }
        return super.getCapability(cap, side);
    }
    
    @Override
    public void addNeighbours(BiConsumer<IKineticNode, Float> nodeConsumer, BiPredicate<World, BlockPos> posValidator)
    {
        KinesisUtil.findAndAddShafts(world, pos, getBlockState().get(BlockWaterWheel.AXIS), ratio, nodeConsumer, posValidator);
        KinesisUtil.findAndAddGears(world, pos, getBlockState().get(BlockWaterWheel.AXIS), ratio, nodeConsumer, posValidator);
    }
    
    @Override
    public IKineticNode getNode()
    {
        return node;
    }
    
    @Nonnull
    public EnumFacing getRotationFacing()
    {
        return EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, getBlockState().get(BlockWaterWheel.AXIS));
    }
    
    @Override
    public String toString()
    {
        return Objects.toString(node);
    }
    
}
