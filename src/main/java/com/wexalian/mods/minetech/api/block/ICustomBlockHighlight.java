package com.wexalian.mods.minetech.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public interface ICustomBlockHighlight
{
    VoxelShape getBlockHighlight(World world, BlockPos pos, IBlockState state, EntityPlayer player);
}
