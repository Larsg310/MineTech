package wexalian.mods.minetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.tileentity.TileKineticTest;

public class BlockKineticTest extends Block implements ITileEntityProvider
{
    
    public BlockKineticTest()
    {
        super(Material.ROCK);
        setRegistryName(BlockNames.KINETIC_TEST);
        setUnlocalizedName(BlockNames.KINETIC_TEST);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileKineticTest();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote && facing.getAxis() != EnumFacing.Axis.Y)
        {
            ((TileKineticTest) worldIn.getTileEntity(pos)).debug();
        }
        return facing.getAxis() != EnumFacing.Axis.Y;
    }
}
