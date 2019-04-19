package com.wexalian.mods.minetech.block;

import com.wexalian.mods.minetech.api.block.ICustomBlockHighlight;
import com.wexalian.mods.minetech.lib.BlockNames;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityGear;
import com.wexalian.mods.minetech.util.RayTraceUtil;
import com.wexalian.mods.minetech.util.VoxelShapeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class BlockGear extends Block implements ICustomBlockHighlight
{
    public static final VoxelShape DOWN = makeCuboidShape(0, 0.25, 0, 16, 1.75, 16);
    public static final VoxelShape UP = VoxelShapeUtils.rotate(DOWN, Rotation.CLOCKWISE_180, EnumFacing.Axis.X);
    public static final VoxelShape NORTH = VoxelShapeUtils.rotate(DOWN, Rotation.COUNTERCLOCKWISE_90, EnumFacing.Axis.X);
    public static final VoxelShape WEST = VoxelShapeUtils.rotate(NORTH, Rotation.CLOCKWISE_90, EnumFacing.Axis.Y);
    public static final VoxelShape SOUTH = VoxelShapeUtils.rotate(NORTH, Rotation.CLOCKWISE_180, EnumFacing.Axis.Y);
    public static final VoxelShape EAST = VoxelShapeUtils.rotate(NORTH, Rotation.COUNTERCLOCKWISE_90, EnumFacing.Axis.Y);
    public static final VoxelShape[] SHAPES = new VoxelShape[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
    public static EnumProperty<EnumFacing> FACING = BlockStateProperties.FACING;
    
    public BlockGear()
    {
        super(Properties.create(Material.WOOD).variableOpacity());
        setRegistryName(BlockNames.GEAR);
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
        return new TileEntityGear();
    }
    
    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest, IFluidState fluid)
    {
        Pair<Vec3d, Vec3d> vectors = RayTraceUtil.getRayTraceVectors(player);
        RayTraceResult hit = collisionRayTrace(state, world, pos, vectors.getLeft(), vectors.getRight());
        
        if (hit != null)
        {
            TileEntityGear tile = Objects.requireNonNull((TileEntityGear) world.getTileEntity(pos));
            
            int faceIndex = hit.subHit;
            
            tile.setDisabled(faceIndex);
            
            onBlockHarvested(world, pos, state, player);
            
            if (!tile.isAnyEnabled())
            {
                return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
            }
        }
        
        return false;
    }
    
    @Nullable
    @Override
    public RayTraceResult getRayTraceResult(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end, RayTraceResult original)
    {
        TileEntityGear tile = Objects.requireNonNull((TileEntityGear) world.getTileEntity(pos));
        RayTraceResult closest = null;
        for (EnumFacing face : EnumFacing.values())
        {
            if (tile.isEnabled(face.ordinal()))
            {
                if (closest == null)
                {
                    closest = SHAPES[face.ordinal()].rayTrace(start, end, pos);
                    if (closest != null)
                    {
                        closest.subHit = face.ordinal();
                    }
                }
                else
                {
                    RayTraceResult result2 = SHAPES[face.ordinal()].rayTrace(start, end, pos);
                    if (result2 != null && result2.hitVec.subtract(start).lengthSquared() < closest.hitVec.subtract(start).lengthSquared())
                    {
                        result2.subHit = face.ordinal();
                        closest = result2;
                    }
                }
            }
        }
        return closest;
    }
    
    @Override
    public VoxelShape getBlockHighlight(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        Pair<Vec3d, Vec3d> vectors = RayTraceUtil.getRayTraceVectors(player);
        RayTraceResult hit = collisionRayTrace(state, world, pos, vectors.getLeft(), vectors.getRight());
        
        if (hit != null)
        {
            TileEntityGear tile = Objects.requireNonNull((TileEntityGear) world.getTileEntity(pos));
            
            int faceIndex = hit.subHit;
            
            if (tile.isEnabled(faceIndex))
            {
                return SHAPES[faceIndex];
            }
        }
        
        return VoxelShapes.fullCube();
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
        TileEntity tileEntity = world.getTileEntity(pos);
        
        VoxelShape result = null;
        
        if (tileEntity instanceof TileEntityGear)
        {
            TileEntityGear tile = (TileEntityGear) tileEntity;
            
            for (EnumFacing face : EnumFacing.values())
            {
                if (tile.isEnabled(face.ordinal()))
                {
                    if (result == null) result = SHAPES[face.ordinal()];
                    else result = VoxelShapes.or(result, SHAPES[face.ordinal()]);
                }
            }
        }
        return result == null ? VoxelShapes.fullCube() : result;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote() == player.isSneaking())
        {
            Pair<Vec3d, Vec3d> vectors = RayTraceUtil.getRayTraceVectors(player);
            RayTraceResult hit = collisionRayTrace(state, world, pos, vectors.getLeft(), vectors.getRight());
            
            if (hit != null)
            {
                TileEntityGear tile = Objects.requireNonNull((TileEntityGear) world.getTileEntity(pos));
                
                int faceIndex = hit.subHit;
                
                if (tile.isEnabled(faceIndex))
                {
                    player.sendMessage(new TextComponentString(tile.getNodeString(faceIndex)));
                }
            }
        }
        return true;
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
        builder.add(FACING);
    }
}
