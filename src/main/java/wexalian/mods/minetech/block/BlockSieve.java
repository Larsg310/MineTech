package wexalian.mods.minetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wexalian.mods.minetech.lib.BlockNames;

import javax.annotation.Nonnull;

public class BlockSieve extends Block
{
    public static PropertyEnum<EnumMaterial> MATERIAL = PropertyEnum.create("material", EnumMaterial.class);
    public static PropertyInteger PROGRESS = PropertyInteger.create("progress", 0, 8);
    
    public BlockSieve()
    {
        super(Material.WOOD);
        setRegistryName(BlockNames.SIEVE);
        setUnlocalizedName(BlockNames.SIEVE);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setDefaultState(getBlockState().getBaseState().withProperty(MATERIAL, EnumMaterial.EMPTY).withProperty(PROGRESS, 0));
    }
    
    //    @Override
    //    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    //    {
    //        ItemStack stack = player.getHeldItem(hand);
    //        TileEntitySieve tile = (TileEntitySieve) world.getTileEntity(pos);
    //        if (tile != null)
    //        {
    //            if (tile.onActivated(stack))
    //            {
    //                world.notifyBlockUpdate(pos, state, getActualState(state, world, pos), 3);
    //                return true;
    //            }
    //        }
    //        return false;
    //    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;//state.getValue(PROGRESS);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState();//.withProperty(PROGRESS, meta);
    }
    
    @Nonnull
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, MATERIAL, PROGRESS);
    }
    
    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos)
    {
        //        TileEntitySieve tile = (TileEntitySieve) world.getTileEntity(pos);
        //        if (tile != null) return state.withProperty(MATERIAL, EnumMaterial.getFromStack(tile.getStack())).withProperty(PROGRESS, tile.getProgress());
        return state;
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        world.setBlockState(pos, state.cycleProperty(PROGRESS), 3);
        return true;
    }
    
    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     *
     * @param state the state of the block
     * @return True if the Block is opaque
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    /**
     * Used to if the Block is a full block
     *
     * @param state the state of the block
     * @return True if the Block is a full block
     */
    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }
    
    /**
     * Used to determine if the Block is a full cube
     *
     * @param state the state of the block
     * @return True if the Block is a full cube
     */
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    public enum EnumMaterial implements IStringSerializable
    {
        EMPTY,
        IRON,
        GOLD;
        
        @Override
        @Nonnull
        public String getName()
        {
            return toString().toLowerCase();
        }
    }
}
