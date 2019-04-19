package com.wexalian.mods.minetech.item.block;

import com.wexalian.mods.minetech.block.BlockGear;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityGear;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemBlockGear extends ItemBlock
{
    public ItemBlockGear(Block block, ItemGroup itemGroup)
    {
        super(block, new Properties().group(itemGroup));
        setRegistryName(Objects.requireNonNull(block.getRegistryName()));
    }
    
    @Nonnull
    @Override
    public EnumActionResult tryPlace(BlockItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        TileEntity tileEntity = world.getTileEntity(pos);
        
        if (tileEntity instanceof TileEntityGear)
        {
            TileEntityGear tile = (TileEntityGear) tileEntity;
            tile.setWorld(world);
            if (!tile.isEnabled(context.getFace().getOpposite().ordinal()))
            {
                tile.setEnabled(context.getFace().getOpposite().ordinal());
                
                if (context.getPlayer() instanceof EntityPlayerMP)
                {
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) context.getPlayer(), pos, context.getItem());
                }
                
                IBlockState state = world.getBlockState(pos);
                
                SoundType soundtype = state.getSoundType(world, pos, context.getPlayer());
                world.playSound(context.getPlayer(), context.getPos(), soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                context.getItem().shrink(1);
                
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.FAIL;
        }
        else return super.tryPlace(context);
    }
    
    @Override
    protected boolean canPlace(BlockItemUseContext context, IBlockState state)
    {
        return state.isValidPosition(context.getWorld(), context.getPos()) && checkNoEntityCollision(context.getWorld(), state, context.getPos(), context.getFace().getOpposite());
    }
    
    @Override
    protected boolean placeBlock(BlockItemUseContext context, @Nonnull IBlockState state)
    {
        if (super.placeBlock(context, state))
        {
            World world = context.getWorld();
            BlockPos pos = context.getPos();
            TileEntityGear tile = Objects.requireNonNull((TileEntityGear) world.getTileEntity(pos));
            
            tile.setEnabled(context.getFace().getOpposite().ordinal());
            
            return true;
        }
        return false;
    }
    
    private boolean checkNoEntityCollision(World world, IBlockState state, BlockPos pos, EnumFacing face)
    {
        VoxelShape voxelshape = BlockGear.SHAPES[face.ordinal()];
        return voxelshape.isEmpty() || world.checkNoEntityCollision(null, voxelshape.withOffset(pos.getX(), pos.getY(), pos.getZ()));
    }
}
