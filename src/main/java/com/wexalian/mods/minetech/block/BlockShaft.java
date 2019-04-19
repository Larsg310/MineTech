package com.wexalian.mods.minetech.block;

import com.wexalian.mods.minetech.api.block.IRotatedBlockHighlight;
import com.wexalian.mods.minetech.lib.BlockNames;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityShaft;
import com.wexalian.mods.minetech.util.VoxelShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BlockShaft extends Block implements IRotatedBlockHighlight
{
    public static final EnumProperty<EnumFacing.Axis> AXIS = BlockStateProperties.AXIS;
    
    private static final VoxelShape SHAFT_Z = Block.makeCuboidShape(6, 6, 0, 10, 10, 16);
    private static final VoxelShape SHAFT_X = VoxelShapeUtils.rotate(SHAFT_Z, Rotation.CLOCKWISE_90, EnumFacing.Axis.Y);
    private static final VoxelShape SHAFT_Y = VoxelShapeUtils.rotate(SHAFT_Z, Rotation.CLOCKWISE_90, EnumFacing.Axis.X);
    
    public BlockShaft()
    {
        super(Properties.create(Material.WOOD));
        setDefaultState(getStateContainer().getBaseState().with(AXIS, EnumFacing.Axis.Y));
        setRegistryName(BlockNames.SHAFT);
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
        return new TileEntityShaft();
    }
    
    @Override
    public List<Pair<VoxelShape, Pair<EnumFacing.Axis, Float>>> getBlockHighlightShapes(IBlockState state, World world, BlockPos pos, float partialTicks)
    {
        TileEntityShaft tile = (TileEntityShaft) Objects.requireNonNull(world.getTileEntity(pos));
        return Collections.singletonList(Pair.of(getShape(state, world, pos), Pair.of(state.get(AXIS), tile.getNode().getAngle(partialTicks))));
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
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos)
    {
        switch (state.get(AXIS))
        {
            case X:
                return SHAFT_X;
            case Y:
                return SHAFT_Y;
            case Z:
                return SHAFT_Z;
            default:
                return SHAFT_Y;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote == player.isSneaking())
        {
            player.sendMessage(new TextComponentString(Objects.requireNonNull(world.getTileEntity(pos)).toString()));
        }
        return true;
    }
    
    @Nullable
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(AXIS, context.getFace().getAxis());
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
        builder.add(AXIS);
    }
}
