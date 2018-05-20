package wexalian.mods.minetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import wexalian.mods.minetech.init.ModBlocks;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.tileentity.TileEntityCrank;
import wexalian.mods.minetech.util.WorldUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCrank extends Block
{
    public static final float PIXEL = 0.0625F;
    
    public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(PIXEL * 2, 0, PIXEL * 2, PIXEL * 14, PIXEL * 12, PIXEL * 14);
    
    public BlockCrank()
    {
        super(Material.WOOD);
        setRegistryName(BlockNames.CRANK);
        setUnlocalizedName(BlockNames.CRANK);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        
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
        return new TileEntityCrank();
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityCrank tile = WorldUtil.getTileEntity(world, pos);
        return tile != null && tile.tryCrank();
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos)
    {
        return world.getBlockState(pos.down()).getBlock() == ModBlocks.GRINDSTONE;
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDING_BOX;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
}
