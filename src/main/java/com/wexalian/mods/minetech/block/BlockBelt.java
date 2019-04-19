package com.wexalian.mods.minetech.block;

import com.wexalian.mods.minetech.api.block.properties.BeltConnection;
import com.wexalian.mods.minetech.api.block.properties.MineTechProperties;
import com.wexalian.mods.minetech.lib.BlockNames;
import com.wexalian.mods.minetech.tileentity.TileEntityBelt;
import com.wexalian.mods.minetech.util.VoxelShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockBelt extends Block
{
    public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
    public static final EnumProperty<BeltConnection> BELT_CONNECTION = MineTechProperties.BELT_CONNECTION;
    
    private static VoxelShape SHAPE_STRAIGHT = VoxelShapes.or(makeCuboidShape(15, 0, 0, 16, 4, 16), VoxelShapes.or(makeCuboidShape(0, 0, 0, 1, 4, 16), makeCuboidShape(1, 0, 0, 15, 3, 16)));
    private static VoxelShape SHAPE_STRAIGHT_90 = VoxelShapeUtils.rotate(SHAPE_STRAIGHT, Rotation.CLOCKWISE_90, EnumFacing.Axis.Y);
    
    private static VoxelShape SHAPE_NORTH_EAST = VoxelShapes.or(makeCuboidShape(15, 0, 1, 16, 3, 15), VoxelShapes.or(makeCuboidShape(9, 0, 13, 12, 3, 14), VoxelShapes.or(makeCuboidShape(12, 0, 13, 15, 3, 15), VoxelShapes.or(makeCuboidShape(5, 0, 10, 6, 3, 12), VoxelShapes.or(makeCuboidShape(4, 0, 10, 5, 3, 11), VoxelShapes.or(makeCuboidShape(6, 0, 10, 15, 3, 13), VoxelShapes.or(makeCuboidShape(3, 0, 7, 15, 3, 10), VoxelShapes.or(makeCuboidShape(2, 0, 4, 15, 3, 7), VoxelShapes.or(makeCuboidShape(1, 0, 0, 15, 3, 4), VoxelShapes.or(makeCuboidShape(15, 0, 0, 16, 4, 1), VoxelShapes.or(makeCuboidShape(11, 0, 15, 16, 4, 16), VoxelShapes.or(makeCuboidShape(8, 0, 14, 12, 4, 15), VoxelShapes.or(makeCuboidShape(6, 0, 13, 9, 4, 14), VoxelShapes.or(makeCuboidShape(5, 0, 12, 6, 4, 14), VoxelShapes.or(makeCuboidShape(4, 0, 11, 5, 4, 13), VoxelShapes.or(makeCuboidShape(3, 0, 10, 4, 4, 12), VoxelShapes.or(makeCuboidShape(2, 0, 7, 3, 4, 11), VoxelShapes.or(makeCuboidShape(1, 0, 4, 2, 4, 8), makeCuboidShape(0, 0, 0, 1, 4, 5)))))))))))))))))));
    private static VoxelShape SHAPE_SOUTH_EAST = VoxelShapeUtils.rotate(SHAPE_NORTH_EAST, Rotation.CLOCKWISE_90, EnumFacing.Axis.Y);
    private static VoxelShape SHAPE_SOUTH_WEST = VoxelShapeUtils.rotate(SHAPE_NORTH_EAST, Rotation.CLOCKWISE_180, EnumFacing.Axis.Y);
    private static VoxelShape SHAPE_NORTH_WEST = VoxelShapeUtils.rotate(SHAPE_NORTH_EAST, Rotation.COUNTERCLOCKWISE_90, EnumFacing.Axis.Y);
    
    private static VoxelShape DEFAULT = Block.makeCuboidShape(0, 0, 0, 16, 4, 16);
    
    public BlockBelt()
    {
        super(Properties.create(Material.WOOD));
        setRegistryName(BlockNames.LEATHER_BELT);
        setDefaultState(getStateContainer().getBaseState().with(FACING, EnumFacing.NORTH).with(BELT_CONNECTION, BeltConnection.STRAIGHT));
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState updatePostPlacement(@Nonnull IBlockState state, EnumFacing face, IBlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos)
    {
        EnumFacing facing = state.get(FACING);
        BeltConnection connection = getBeltConnection(facing, pos, world);
        return state.with(BELT_CONNECTION, connection);
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
    public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
    
    @SuppressWarnings({"deprecation", "Duplicates"})
    @Nonnull
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader reader, BlockPos pos)
    {
        EnumFacing face = state.get(FACING);
        BeltConnection connection = state.get(BELT_CONNECTION);
        
        switch (connection)
        {
            case LEFT:
                switch (face)
                {
                    case NORTH:
                        return SHAPE_SOUTH_EAST;
                    case SOUTH:
                        return SHAPE_NORTH_WEST;
                    case WEST:
                        return SHAPE_SOUTH_WEST;
                    case EAST:
                        return SHAPE_NORTH_EAST;
                }
            case RIGHT:
                switch (face)
                {
                    case NORTH:
                        return SHAPE_NORTH_EAST;
                    case SOUTH:
                        return SHAPE_SOUTH_WEST;
                    case WEST:
                        return SHAPE_SOUTH_EAST;
                    case EAST:
                        return SHAPE_NORTH_WEST;
                }
            case STRAIGHT:
                switch (face)
                {
                    case NORTH:
                    case SOUTH:
                        return SHAPE_STRAIGHT;
                    case WEST:
                    case EAST:
                        return SHAPE_STRAIGHT_90;
                }
        }
        
        return DEFAULT;
    }
    
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context)
    {
        EnumFacing facing = context.getPlacementHorizontalFacing();
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        
        BeltConnection connection = getBeltConnection(facing, pos, world);
        
        return getDefaultState().with(FACING, facing).with(BELT_CONNECTION, connection);
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
        builder.add(FACING, BELT_CONNECTION);
    }
    
    private BeltConnection getBeltConnection(EnumFacing facing, BlockPos pos, IWorld world)
    {
        EnumFacing right = facing.rotateY();
        EnumFacing left = facing.rotateYCCW();
        EnumFacing back = facing.getOpposite();
        
        IBlockState rightState = world.getBlockState(pos.offset(right));
        IBlockState leftState = world.getBlockState(pos.offset(left));
        IBlockState backState = world.getBlockState(pos.offset(back));
        
        boolean beltRight = rightState.getBlock() instanceof BlockBelt && rightState.get(FACING) == right.getOpposite();
        boolean beltLeft = leftState.getBlock() instanceof BlockBelt && leftState.get(FACING) == left.getOpposite();
        boolean beltBack = backState.getBlock() instanceof BlockBelt && backState.get(FACING) == facing;
        
        return (beltBack || (beltRight == beltLeft)) ? BeltConnection.STRAIGHT : beltLeft ? BeltConnection.LEFT : BeltConnection.RIGHT;
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
        return new TileEntityBelt();
    }
}
