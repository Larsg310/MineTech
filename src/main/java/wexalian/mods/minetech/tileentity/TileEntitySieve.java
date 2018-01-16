package wexalian.mods.minetech.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wexalian.mods.minetech.lib.Reference;

import javax.annotation.Nonnull;

public class TileEntitySieve extends TileEntity
{
    public static final String NBT_ITEMSTACK = Reference.MOD_ID + ":itemstack";
    
    private ItemStack itemStackSieved = ItemStack.EMPTY;
    
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setTag(NBT_ITEMSTACK, itemStackSieved.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(compound);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        itemStackSieved = new ItemStack(compound.getCompoundTag(NBT_ITEMSTACK));
        super.readFromNBT(compound);
    }
    
    public ItemStack getItemStackSieved()
    {
        return itemStackSieved.copy();
    }
    
    public void setItemStackSieved(ItemStack itemstack)
    {
        itemStackSieved = itemstack;
    }
    
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}
