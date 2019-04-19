package com.wexalian.mods.minetech.block;

import com.wexalian.mods.minetech.api.block.IRotatedBlockHighlight;
import com.wexalian.mods.minetech.api.block.MineTechBlocks;
import com.wexalian.mods.minetech.lib.BlockNames;
import com.wexalian.mods.minetech.tileentity.TileEntityCrank;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BlockCrank extends Block implements IRotatedBlockHighlight
{
    private static final VoxelShape STICK = makeCuboidShape(7, 0, 7, 9, 12, 9);
    private static final VoxelShape HANDLE = makeCuboidShape(7, 10, 9, 9, 12, 15);
    private static final VoxelShape SELECTION_BOX = VoxelShapes.or(STICK, HANDLE);
    private static final VoxelShape COLLISION_BOX = makeCuboidShape(2, 0, 2, 14, 12, 14);
    
    public BlockCrank()
    {
        super(Properties.create(Material.WOOD));
        setRegistryName(BlockNames.CRANK);
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world)
    {
        return new TileEntityCrank();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader reader, BlockPos pos)
    {
        return SELECTION_BOX;
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull IBlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos)
    {
        return COLLISION_BOX;
    }
    
    @SuppressWarnings({"deprecation", "ConstantConditions"})
    @Override
    public boolean isValidPosition(IBlockState state, IWorldReaderBase world, @Nonnull BlockPos pos)
    {
        return world.getBlockState(pos.down()).getBlock() == MineTechBlocks.GRINDSTONE;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileEntityCrank && ((TileEntityCrank) tile).tryCrank();
    }
    
    @Override
    public List<Pair<VoxelShape, Pair<EnumFacing.Axis, Float>>> getBlockHighlightShapes(IBlockState state, World world, BlockPos pos, float partialTicks)
    {
        TileEntityCrank crank = (TileEntityCrank) Objects.requireNonNull(world.getTileEntity(pos));
        return Collections.singletonList(Pair.of(SELECTION_BOX, Pair.of(EnumFacing.Axis.Y, crank.getAngle(partialTicks))));
    }
}
