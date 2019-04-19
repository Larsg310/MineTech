package com.wexalian.mods.minetech.tileentity.kinetic;

import com.wexalian.mods.minetech.api.MineTechAPI;
import com.wexalian.mods.minetech.api.energy.kinetic.IGearAttachable;
import com.wexalian.mods.minetech.api.energy.kinetic.IKineticNode;
import com.wexalian.mods.minetech.api.energy.kinetic.IShaftAttachable;
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

public class TileEntityGear extends TileEntityMultiHost<IKineticNode, Float>
{
    public static TileEntityType<TileEntityGear> TYPE;
    
    public TileEntityGear()
    {
        super(TYPE, 6, 0.5F);
    }
    
    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing face)
    {
        if (cap == MineTechAPI.Kinesis.SHAFT_CAPABILITY)
        {
            Objects.requireNonNull(face);
            if (isEnabled(face.ordinal()))
            {
                return LazyOptional.of(() -> (T) (IShaftAttachable) () -> getNode(face.ordinal()));
            }
        }
        if (cap == MineTechAPI.Kinesis.GEAR_CAPABILITY)
        {
            Objects.requireNonNull(face);
            if (isEnabled(face.ordinal()))
            {
                return LazyOptional.of(() -> (T) (IGearAttachable) (f) -> nodes[f.ordinal()]);
            }
        }
        return super.getCapability(cap, face);
    }
    
    @SuppressWarnings("ConstantConditions")
    @Override
    public void addNeighbours(BiConsumer<IKineticNode, Float> nodeConsumer, BiPredicate<World, BlockPos> posValidator, int index)
    {
        EnumFacing face = EnumFacing.values()[index];
        IKineticNode faceNode = getNode(face.ordinal());
        float faceRatio = faceNode.getValue();
        
        for (EnumFacing facing : EnumFacing.values())
        {
            if (face.getAxis() != facing.getAxis())
            {
                if (isEnabled(facing.ordinal()))
                {
                    float ratio = -1F;
                    ratio *= face.getAxisDirection().getOffset();
                    ratio *= facing.getAxisDirection().getOffset();
                    ratio *= (faceRatio < 0 ? -1F : 1F);
                    
                    nodeConsumer.accept(getNode(facing.ordinal()), ratio);
                }
            }
        }
        KinesisUtil.findAndAddShaft(world, pos, face, faceRatio, nodeConsumer, posValidator);
        KinesisUtil.findAndAddGear(world, pos, face, faceRatio, nodeConsumer, posValidator);
    }
}
