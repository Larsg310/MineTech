package wexalian.mods.minetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wexalian.mods.minetech.init.ModItems;
import wexalian.mods.minetech.item.ItemDirtyOreDust;
import wexalian.mods.minetech.item.ItemOreDust;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.metal.Metals;
import wexalian.mods.minetech.tileentity.TileEntitySieve;
import wexalian.mods.minetech.util.IWorldUtils;
import wexalian.mods.minetech.util.InventoryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockSieve extends Block implements IWorldUtils
{
    public static PropertyEnum<Metals> MATERIAL = PropertyEnum.create("material", Metals.class, ItemDirtyOreDust.TYPES::contains);
    public static PropertyInteger PROGRESS = PropertyInteger.create("progress", 0, 8);
    
    public BlockSieve()
    {
        super(Material.WOOD);
        setRegistryName(BlockNames.SIEVE);
        setUnlocalizedName(BlockNames.SIEVE);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setDefaultState(getBlockState().getBaseState().withProperty(MATERIAL, Metals.IRON).withProperty(PROGRESS, 0));
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
    {
        return new TileEntitySieve();
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(PROGRESS);
    }
    
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(PROGRESS, meta);
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
        TileEntitySieve tile = getTileEntity(world, pos);
        assert tile != null : "tile == null: should not be possible";
        return state.withProperty(MATERIAL, ItemDirtyOreDust.getMetalFromStack(tile.getItemStackSieved()));
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntitySieve tile = getTileEntity(world, pos);
        
        if (tile != null)
        {
            if (state.getValue(PROGRESS) == 0)
            {
                ItemStack stack = player.getHeldItem(hand).copy();
                
                if (stack.getItem() == ModItems.DIRTY_ORE_DUST) tile.setItemStackSieved(stack);
                else tile.setItemStackSieved(ItemStack.EMPTY);
            }
            
            if (!tile.getItemStackSieved().isEmpty())
            {
                IBlockState newState = state.cycleProperty(PROGRESS).withProperty(MATERIAL, ItemDirtyOreDust.getMetalFromStack(tile.getItemStackSieved()));
                world.setBlockState(pos, newState, 3);
                
                if (newState.getValue(PROGRESS) == 0)
                {
                    if (!world.isRemote)
                    {
                        ItemStack result = ItemOreDust.getFromMetal(ItemDirtyOreDust.getMetalFromStack(tile.getItemStackSieved()));
                        InventoryUtil.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), result);
                        tile.setItemStackSieved(ItemStack.EMPTY);
                    }
                }
            }
            
        }
        
        //        if (state.getValue(PROGRESS) != 0) world.setBlockState(pos, state.cycleProperty(PROGRESS), 3);
        //        else if (player.getHeldItem(hand).getItem() == ModItems.DIRTY_ORE_DUST)
        //        {
        //            Metals metal = ItemDirtyOreDust.getMetalFromStack(player.getHeldItem(hand));
        //            world.setBlockState(pos, state.cycleProperty(PROGRESS), 3);
        //        }
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
}
