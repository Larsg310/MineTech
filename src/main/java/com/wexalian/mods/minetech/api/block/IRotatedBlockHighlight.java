package com.wexalian.mods.minetech.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IRotatedBlockHighlight
{
    List<Pair<VoxelShape, Pair<EnumFacing.Axis, Float>>> getBlockHighlightShapes(IBlockState state, World world, BlockPos pos, float partialTicks);
}
