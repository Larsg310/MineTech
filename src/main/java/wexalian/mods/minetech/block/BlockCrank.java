package wexalian.mods.minetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wexalian.mods.minetech.init.ModBlocks;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.tileentity.TileEntityCrank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCrank extends Block
{
    public BlockCrank()
    {
        super(Material.WOOD);
        setRegistryName(BlockNames.CRANK);
        setUnlocalizedName(BlockNames.CRANK);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    /**
     * Called throughout the code as a replacement for block instanceof BlockContainer
     * Moving this to the Block base class allows for mods that wish to extend vanilla
     * blocks, and also want to have a tile entity on that block, may.
     * <p>
     * Return true from this function to specify this block has a tile entity.
     *
     * @param state State of the current block
     * @return True if block has a tile entity, false otherwise
     */
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
    
    /**
     * Called throughout the code as a replacement for ITileEntityProvider.createNewTileEntity
     * Return the same thing you would from that function.
     * This will fall back to ITileEntityProvider.createNewTileEntity(World) if this block is a ITileEntityProvider
     *
     * @param state The state of the current block
     * @return A instance of a class extending TileEntity
     */
    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
    {
        return new TileEntityCrank();
    }
    
    /**
     *  Called when the block is right clicked by a player.
     *
     * @param world The World the Block exists in
     * @param pos The position of the Block
     * @param state The state of the current Block
     * @param player The player interacting in the Block
     * @param hand The hand used by the player
     * @param facing The side of the Block hit
     * @param hitX The x value of the hitVec on the Block (range: 0F-1F)
     * @param hitY The y value of the hitVec on the Block (range: 0F-1F)
     * @param hitZ The z value of the hitVec on the Block (range: 0F-1F)
     * @return True if the interaction was successful, false otherwise
     */
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityCrank)
            {
                ((TileEntityCrank) tile).tryCrank();
                return true;
            }
        }
        return true;
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos)
    {
        return world.getBlockState(pos.down()).getBlock() == ModBlocks.GRINDSTONE;
    }
}
