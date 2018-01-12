package wexalian.mods.minetech.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TileEntitySieve extends TileEntity
{
    public static final String NBT_KEY_PROGRESS = "minetech:progress";
    public static final String NBT_KEY_ITEMSTACK = "minetech:itemstack";
    
    private int progress;
    private ItemStack stack = ItemStack.EMPTY;
    
    public boolean onActivated(ItemStack input)
    {
        if (progress > 0)
        {
            progress--;
            if(progress == 0)
            {
                //spit items
            }
            return true;
        }
        else if (true) //item can be sieved
        {
            stack = input.copy();
            input.shrink(1);
            progress = 7;
            return true;
        }
        return false;
    }
    
    public int getProgress()
    {
        return progress;
    }
    
    public ItemStack getStack()
    {
        return stack;
    }
    
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }
    
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger(NBT_KEY_PROGRESS, progress);
        NBTTagCompound stackTag = new NBTTagCompound();
        stack.writeToNBT(stackTag);
        compound.setTag(NBT_KEY_ITEMSTACK, stackTag);
        return compound;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        progress = compound.getInteger(NBT_KEY_PROGRESS);
        stack = new ItemStack(compound.getCompoundTag(NBT_KEY_ITEMSTACK));
    }
}
