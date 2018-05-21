package wexalian.mods.minetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.tileentity.mechanical.TileEntityGear;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockGear extends Block
{
    public static IProperty<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    
    public BlockGear()
    {
        super(Material.ROCK);
        setRegistryName(BlockNames.GEAR);
        setUnlocalizedName(BlockNames.GEAR);
        setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.DOWN));
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
    {
        return new TileEntityGear();
    }
    
    @Nonnull
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer.Builder(this).add(FACING).build();
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).ordinal();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, @Nullable EnumHand hand)
    {
        return getDefaultState().withProperty(FACING, facing.getOpposite());
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
}
