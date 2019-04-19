package com.wexalian.mods.minetech.util;

import com.wexalian.mods.minetech.api.MineTechAPI;
import com.wexalian.mods.minetech.api.energy.kinetic.IGearAttachable;
import com.wexalian.mods.minetech.api.energy.kinetic.IKineticNode;
import com.wexalian.mods.minetech.api.energy.kinetic.IShaftAttachable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public final class KinesisUtil
{
    public static void findAndAddShafts(World world, BlockPos pos, EnumFacing.Axis axis, float ratio, BiConsumer<IKineticNode, Float> neighbours, BiPredicate<World, BlockPos> posValidator)
    {
        EnumFacing front = EnumFacing.getFacingFromAxisDirection(axis, EnumFacing.AxisDirection.POSITIVE);
        EnumFacing back = EnumFacing.getFacingFromAxisDirection(axis, EnumFacing.AxisDirection.NEGATIVE);
        
        findAndAddShaft(world, pos, front, ratio, neighbours, posValidator);
        findAndAddShaft(world, pos, back, ratio, neighbours, posValidator);
    }
    
    public static void findAndAddShaft(World world, BlockPos pos, EnumFacing face, float ratio, BiConsumer<IKineticNode, Float> neighbours, BiPredicate<World, BlockPos> posValidator)
    {
        BlockPos posOff = pos.offset(face);
        if (posValidator.test(world, posOff))
        {
            TileEntity tile = world.getTileEntity(posOff);
            
            if (tile != null)
            {
                LazyOptional<IShaftAttachable> node = tile.getCapability(MineTechAPI.Kinesis.SHAFT_CAPABILITY, face.getOpposite());
                node.ifPresent(cap -> neighbours.accept(cap.getNode(), ratio));
            }
        }
    }
    
    public static void findAndAddGears(World world, BlockPos pos, EnumFacing.Axis axis, float ratio, BiConsumer<IKineticNode, Float> neighbours, BiPredicate<World, BlockPos> posValidator)
    {
        EnumFacing front = EnumFacing.getFacingFromAxisDirection(axis, EnumFacing.AxisDirection.POSITIVE);
        EnumFacing back = EnumFacing.getFacingFromAxisDirection(axis, EnumFacing.AxisDirection.NEGATIVE);
        
        findAndAddGear(world, pos, front, ratio, neighbours, posValidator);
        findAndAddGear(world, pos, back, ratio, neighbours, posValidator);
    }
    
    public static void findAndAddGear(World world, BlockPos pos, EnumFacing face, float ratio, BiConsumer<IKineticNode, Float> neighbours, BiPredicate<World, BlockPos> posValidator)
    {
        BlockPos posOff = pos.offset(face);
        if (posValidator.test(world, posOff))
        {
            TileEntity tile = world.getTileEntity(posOff);
            
            if (tile != null)
            {
                LazyOptional<IGearAttachable> node = tile.getCapability(MineTechAPI.Kinesis.GEAR_CAPABILITY, face.getOpposite());
                node.ifPresent(cap -> neighbours.accept(cap.getNode(face.getOpposite()), ratio));
            }
        }
    }
    
}
