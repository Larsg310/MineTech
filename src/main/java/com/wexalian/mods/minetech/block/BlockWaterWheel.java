package com.wexalian.mods.minetech.block;

import com.wexalian.mods.minetech.api.block.IRotatedBlockHighlight;
import com.wexalian.mods.minetech.lib.BlockNames;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityWaterWheel;
import com.wexalian.mods.minetech.util.VoxelShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowingFluid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.minecraft.util.EnumFacing.Axis.Y;
import static net.minecraft.util.EnumFacing.Axis.Z;

@SuppressWarnings("Duplicates")
public class BlockWaterWheel extends Block implements IRotatedBlockHighlight
{
    public static final EnumProperty<EnumFacing.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final IntegerProperty DUMMY = IntegerProperty.create("id", 0, 9);
    public static final VoxelShape[][] BOXES = new VoxelShape[2][9];
    private static final VoxelShape SHAFT = Block.makeCuboidShape(5, 5, 0, 11, 11, 16);
    private static final VoxelShape SHAFT_90 = VoxelShapeUtils.rotate(SHAFT, Rotation.CLOCKWISE_90, Y);
    
    static
    {
        BOXES[0][0] = Block.makeCuboidShape(-16, -16, 1, 32, 32, 15);
        
        BOXES[0][1] = VoxelShapeUtils.translate(VoxelShapeUtils.translate(BOXES[0][0], EnumFacing.Axis.Y, 16), EnumFacing.Axis.X, 16);
        BOXES[0][2] = VoxelShapeUtils.translate(BOXES[0][0], EnumFacing.Axis.X, 16);
        BOXES[0][3] = VoxelShapeUtils.translate(VoxelShapeUtils.translate(BOXES[0][0], EnumFacing.Axis.Y, -16), EnumFacing.Axis.X, 16);
        
        BOXES[0][4] = VoxelShapeUtils.translate(BOXES[0][0], EnumFacing.Axis.Y, 16);
        BOXES[0][5] = VoxelShapeUtils.translate(BOXES[0][0], EnumFacing.Axis.Y, -16);
        
        BOXES[0][6] = VoxelShapeUtils.translate(VoxelShapeUtils.translate(BOXES[0][0], EnumFacing.Axis.Y, 16), EnumFacing.Axis.X, -16);
        BOXES[0][7] = VoxelShapeUtils.translate(BOXES[0][0], EnumFacing.Axis.X, -16);
        BOXES[0][8] = VoxelShapeUtils.translate(VoxelShapeUtils.translate(BOXES[0][0], EnumFacing.Axis.Y, -16), EnumFacing.Axis.X, -16);
        
        for (int i = 0; i < BOXES[0].length; i++)
        {
            BOXES[1][i] = VoxelShapeUtils.rotate(BOXES[0][i], Rotation.COUNTERCLOCKWISE_90, EnumFacing.Axis.Y);
        }
        
    }
    
    public BlockWaterWheel()
    {
        super(Properties.create(Material.WOOD));
        setRegistryName(BlockNames.WATER_WHEEL);
        setDefaultState(getStateContainer().getBaseState().with(AXIS, EnumFacing.Axis.X).with(DUMMY, 0));
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return state.get(DUMMY) == 0;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world)
    {
        if (state.get(DUMMY) > 0) return null;
        return new TileEntityWaterWheel();
    }
    
    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest, IFluidState fluid)
    {
        if (state.get(DUMMY) > 0) return false;
        
        EnumFacing.Axis axis = state.get(AXIS) == EnumFacing.Axis.X ? Z : EnumFacing.Axis.X;
        for (int h = -1; h <= 1; h++)
        {
            for (int v = -1; v <= 1; v++)
            {
                if (h == 0 && v == 0) {continue;}
                BlockPos placePos = pos.add(axis.getCoordinate(h, 0, 0), v, axis.getCoordinate(0, 0, h));
                world.setBlockState(placePos, Blocks.AIR.getDefaultState());
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
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
    public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos)
    {
        EnumFacing.Axis axis = state.get(AXIS);
        int dummy = state.get(DUMMY);
        return BOXES[axis == Z ? 0 : 1][dummy];
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull IBlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos)
    {
        return VoxelShapes.fullCube();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isValidPosition(IBlockState state, IWorldReaderBase world, BlockPos pos)
    {
        EnumFacing.Axis axis = state.get(AXIS) == EnumFacing.Axis.X ? Z : EnumFacing.Axis.X;
        for (int h = -1; h <= 1; h++)
        {
            for (int v = -1; v <= 1; v++)
            {
                if (h == 0 && v == 0) {continue;}
                BlockPos testPos = pos.add(axis.getCoordinate(h, 0, 0), v, axis.getCoordinate(0, 0, h));
                if (!world.isAirBlock(testPos) || (world.getBlockState(testPos).getBlock() instanceof BlockFlowingFluid && world.getFluidState(testPos).isTagged(FluidTags.WATER)))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote == player.isSneaking())
        {
            player.sendMessage(new TextComponentString(Objects.requireNonNull((TileEntityWaterWheel) world.getTileEntity(pos)).toString()));
        }
        return true;
    }
    
    @Nullable
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context)
    {
        if (context.getFace().getAxis() != EnumFacing.Axis.Y)
        {
            return getDefaultState().with(AXIS, context.getFace().getAxis()).with(DUMMY, 0);
        }
        return null;
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, @Nullable EntityLivingBase placer, ItemStack stack)
    {
        EnumFacing.Axis axis = state.get(AXIS) == EnumFacing.Axis.X ? Z : EnumFacing.Axis.X;
        int index = 0;
        for (int h = -1; h <= 1; h++)
        {
            for (int v = -1; v <= 1; v++)
            {
                if (h == 0 && v == 0) {continue;}
                BlockPos placePos = pos.add(axis.getCoordinate(h, 0, 0), v, axis.getCoordinate(0, 0, h));
                worldIn.setBlockState(placePos, state.with(DUMMY, ++index));
            }
        }
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
        builder.add(AXIS, DUMMY);
    }
    
    @Override
    public List<Pair<VoxelShape, Pair<EnumFacing.Axis, Float>>> getBlockHighlightShapes(IBlockState state, World world, BlockPos pos, float partialTicks)
    {
        List<Pair<VoxelShape, Pair<EnumFacing.Axis, Float>>> list = new ArrayList<>();
        
        list.add(Pair.of(getShape(state, world, pos), null));
        
        if (state.get(DUMMY) == 0)
        {
            EnumFacing.Axis axis = state.get(AXIS);
            
            TileEntityWaterWheel tile = (TileEntityWaterWheel) Objects.requireNonNull(world.getTileEntity(pos));
            list.add(Pair.of(axis == Z ? SHAFT : SHAFT_90, Pair.of(axis, tile.getNode().getAngle(partialTicks))));
        }
        
        return list;
    }
}
