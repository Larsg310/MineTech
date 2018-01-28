package wexalian.mods.minetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wexalian.mods.minetech.MineTech;
import wexalian.mods.minetech.init.GuiHandler;
import wexalian.mods.minetech.init.ModBlocks;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.tileentity.TileEntityGrindstone;
import wexalian.mods.minetech.util.WorldUtil;
import wexalian.mods.minetech.util.InventoryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockGrindstone extends Block
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    
    public BlockGrindstone()
    {
        super(Material.ROCK);
        setRegistryName(BlockNames.GRINDSTONE);
        setUnlocalizedName(BlockNames.GRINDSTONE);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setDefaultState(getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }
    
    /**
     * Gets the {@link IBlockState} to place
     *
     * @param world  The world the block is being placed in
     * @param pos    The position the block is being placed at
     * @param facing The side the block is being placed on
     * @param hitX   The X coordinate of the hit vector
     * @param hitY   The Y coordinate of the hit vector
     * @param hitZ   The Z coordinate of the hit vector
     * @param meta   The metadata of {@link ItemStack} as processed by {@link Item#getMetadata(int)}
     * @param placer The entity placing the block
     * @param hand   The player hand used to place this block
     * @return The state to be placed in the world
     */
    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    /**
     * Convert the given metadata into an {@link IBlockState} for this {@link Block}
     *
     * @param meta the metadata to convert to an {@link IBlockState}
     * @return The IBlockState corresponding to the meta
     */
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }
    
    @Nonnull
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
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
        return new TileEntityGrindstone();
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote) player.openGui(MineTech.instance, GuiHandler.IDs.GRINDSTONE, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
    
    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state)
    {
        TileEntityGrindstone tile = WorldUtil.getTileEntity(world, pos);
        assert tile != null;
        InventoryUtil.dropInventoryItems(world, pos, tile.inventory);
        if (world.getBlockState(pos.up()).getBlock() == ModBlocks.CRANK)
        {
            ModBlocks.CRANK.dropBlockAsItem(world, pos.up(), state, 0);
            world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 3);
        }
        super.breakBlock(world, pos, state);
    }
}
