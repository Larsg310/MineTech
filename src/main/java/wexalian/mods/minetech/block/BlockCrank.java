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
import wexalian.mods.minetech.MineTech;
import wexalian.mods.minetech.init.GuiHandler;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.tileentity.TileEntityCrank;
import wexalian.mods.minetech.tileentity.TileEntityGrindstone;

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
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileEntityCrank){
                ((TileEntityCrank)tile).tryCrank();
            }
        }
        return true;
    }
}
