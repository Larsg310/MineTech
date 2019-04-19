package com.wexalian.mods.minetech.block;

import com.wexalian.mods.minetech.lib.BlockNames;
import com.wexalian.mods.minetech.tileentity.multiblock.TileEntityOreMill;
import com.wexalian.mods.minetech.util.VoxelShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockOreMill extends Block
{
    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, 1);
    
    public static final VoxelShape BASE = Block.makeCuboidShape(-16, 0, -16, 32, 12, 32);
    public static final VoxelShape BASE_WHEEL = VoxelShapes.or(Block.makeCuboidShape(1, 12, -11, 15, 20, -10), VoxelShapes.or(Block.makeCuboidShape(-2, 12, -10, 18, 20, -9), VoxelShapes.or(Block.makeCuboidShape(-4, 12, -9, 20, 20, -8), VoxelShapes.or(Block.makeCuboidShape(-5, 12, -8, 21, 20, -7), VoxelShapes.or(Block.makeCuboidShape(-6, 12, -7, 22, 20, -6), VoxelShapes.or(Block.makeCuboidShape(-7, 12, -6, 23, 20, -5), VoxelShapes.or(Block.makeCuboidShape(-8, 12, -5, 24, 20, -4), VoxelShapes.or(Block.makeCuboidShape(-9, 12, -4, 25, 20, -2), VoxelShapes.or(Block.makeCuboidShape(-10, 12, -2, 26, 20, 1), VoxelShapes.or(Block.makeCuboidShape(-11, 12, 1, 27, 20, 4), VoxelShapes.or(Block.makeCuboidShape(4, 12, 27, 12, 20, 28), VoxelShapes.or(Block.makeCuboidShape(1, 12, 26, 15, 20, 27), VoxelShapes.or(Block.makeCuboidShape(-2, 12, 25, 18, 20, 26), VoxelShapes.or(Block.makeCuboidShape(-4, 12, 24, 20, 20, 25), VoxelShapes.or(Block.makeCuboidShape(-5, 12, 23, 21, 20, 24), VoxelShapes.or(Block.makeCuboidShape(-6, 12, 22, 22, 20, 23), VoxelShapes
        .or(Block.makeCuboidShape(-7, 12, 21, 23, 20, 22), VoxelShapes.or(Block.makeCuboidShape(-8, 12, 20, 24, 20, 21), VoxelShapes.or(Block.makeCuboidShape(-9, 12, 18, 25, 20, 20), VoxelShapes.or(Block.makeCuboidShape(-10, 12, 15, 26, 20, 18), VoxelShapes.or(Block.makeCuboidShape(-11, 12, 12, 27, 20, 15), Block.makeCuboidShape(-12, 12, 4, 28, 20, 12))))))))))))))))))))));
    public static final VoxelShape BASE_FULL = VoxelShapes.or(BASE, BASE_WHEEL);
    
    public static final VoxelShape WHEEL = VoxelShapes.or(Block.makeCuboidShape(3, 20, -8, 12, 28, -7), VoxelShapes.or(Block.makeCuboidShape(1, 20, -7, 15, 28, -6), VoxelShapes.or(Block.makeCuboidShape(-1, 20, -6, 17, 28, -5), VoxelShapes.or(Block.makeCuboidShape(-2, 20, -5, 18, 28, -4), VoxelShapes.or(Block.makeCuboidShape(-3, 20, -4, 19, 28, -3), VoxelShapes.or(Block.makeCuboidShape(-4, 20, -3, 20, 28, -2), VoxelShapes.or(Block.makeCuboidShape(-5, 20, -2, 21, 28, -1), VoxelShapes.or(Block.makeCuboidShape(-6, 20, -1, 22, 28, 1), VoxelShapes.or(Block.makeCuboidShape(-7, 20, 1, 23, 28, 4), VoxelShapes.or(Block.makeCuboidShape(3, 20, 23, 12, 28, 24), VoxelShapes.or(Block.makeCuboidShape(1, 20, 22, 15, 28, 23), VoxelShapes.or(Block.makeCuboidShape(-1, 20, 21, 17, 28, 22), VoxelShapes.or(Block.makeCuboidShape(-2, 20, 20, 18, 28, 21), VoxelShapes.or(Block.makeCuboidShape(-3, 20, 19, 19, 28, 20), VoxelShapes.or(Block.makeCuboidShape(-4, 20, 18, 20, 28, 19), VoxelShapes.or(Block.makeCuboidShape(-5, 20, 17, 21, 28, 18), VoxelShapes
        .or(Block.makeCuboidShape(-6, 20, 15, 22, 28, 17), VoxelShapes.or(Block.makeCuboidShape(-7, 20, 12, 23, 28, 15), VoxelShapes.or(Block.makeCuboidShape(-8, 20, 4, 24, 28, 12), Block.makeCuboidShape(6, 28, 6, 10, 32, 10))))))))))))))))))));
    
    public static final VoxelShape FULL = VoxelShapes.or(BASE_FULL, WHEEL);
    
    public static final VoxelShape[][][] BLOCK_SHAPES = new VoxelShape[3][2][3];
    
    static
    {
        for (int x = 0; x < BLOCK_SHAPES.length; x++)
        {
            for (int y = 0; y < BLOCK_SHAPES[x].length; y++)
            {
                for (int z = 0; z < BLOCK_SHAPES[x][y].length; z++)
                {
                    VoxelShape shape = FULL;
                    if (x != 1) shape = VoxelShapeUtils.translate(shape, EnumFacing.Axis.X, x > 1 ? -16 : 16);
                    if (y != 0) shape = VoxelShapeUtils.translate(shape, EnumFacing.Axis.Y, -16);
                    if (z != 1) shape = VoxelShapeUtils.translate(shape, EnumFacing.Axis.Z, z > 1 ? -16 : 16);
                    
                    BLOCK_SHAPES[x][y][z] = shape;
                }
            }
        }
    }
    
    public BlockOreMill()
    {
        super(Properties.create(Material.ROCK));
        setRegistryName(BlockNames.ORE_MILL);
        setDefaultState(getStateContainer().getBaseState().with(STATE, 0));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isNormalCube(IBlockState state)
    {
        return false;
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
    
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world)
    {
        return new TileEntityOreMill();
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
        builder.add(STATE);
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos)
    {
        TileEntityOreMill tile = (TileEntityOreMill) world.getTileEntity(pos);
        if (tile != null)
        {
            BlockPos relativePos = tile.getRelativePos();
            if (relativePos != null)
            {
                return BLOCK_SHAPES[relativePos.getX()][relativePos.getY()][relativePos.getZ()];
            }
        }
        return VoxelShapes.fullCube();
    }
}
