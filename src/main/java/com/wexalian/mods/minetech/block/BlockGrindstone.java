package com.wexalian.mods.minetech.block;

import com.wexalian.mods.minetech.container.ContainerGrindstone;
import com.wexalian.mods.minetech.lib.BlockNames;
import com.wexalian.mods.minetech.tileentity.TileEntityGrindstone;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockGrindstone extends Block
{
    public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
    
    public BlockGrindstone()
    {
        super(Properties.create(Material.ROCK));
        setRegistryName(BlockNames.GRINDSTONE);
        setDefaultState(getStateContainer().getBaseState().with(FACING, EnumFacing.NORTH));
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
        return new TileEntityGrindstone();
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            NetworkHooks.openGui((EntityPlayerMP) player, new Grindstone(world, pos), buf -> buf.writeBlockPos(pos));
        }
        return true;
    }
    
    @Nullable
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
        builder.add(FACING);
    }
    
    private class Grindstone implements IInteractionObject
    {
        private World world;
        private BlockPos pos;
        
        Grindstone(World world, BlockPos pos)
        {
            this.world = world;
            this.pos = pos;
        }
        
        @Nonnull
        @Override
        public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer player)
        {
            return new ContainerGrindstone(world, pos, player);
        }
        
        @Nonnull
        @Override
        public String getGuiID()
        {
            return BlockNames.GRINDSTONE.toString();
        }
        
        @Nonnull
        @Override
        public ITextComponent getName()
        {
            return new TextComponentString("Grindstone");
        }
        
        @Override
        public boolean hasCustomName()
        {
            return false;
        }
        
        @Nullable
        @Override
        public ITextComponent getCustomName()
        {
            return null;
        }
        
    }
}
