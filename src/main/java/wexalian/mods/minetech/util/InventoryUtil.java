package wexalian.mods.minetech.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.Random;

public class InventoryUtil
{
    private static final Random RANDOM = new Random();
    
    private static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler handler)
    {
        for (int i = 0; i < handler.getSlots(); ++i)
        {
            ItemStack itemstack = handler.getStackInSlot(i);
            
            if (!itemstack.isEmpty())
            {
                spawnItemStack(worldIn, pos, itemstack);
            }
        }
    }
    
    public static void spawnItemStack(World worldIn, BlockPos pos, @Nonnull ItemStack stack)
    {
        spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
    }
    
    public static void spawnItemStack(World worldIn, double x, double y, double z, @Nonnull ItemStack stack)
    {
        float randX = RANDOM.nextFloat() * 0.8F + 0.1F;
        float randY = RANDOM.nextFloat() * 0.8F + 0.1F;
        float randZ = RANDOM.nextFloat() * 0.8F + 0.1F;
        
        while (stack.getCount() > 0)
        {
            int i = RANDOM.nextInt(21) + 10;
            
            EntityItem entityitem = new EntityItem(worldIn, x + randX, y + randY, z + randZ, stack.splitStack(i));
            
            entityitem.motionX = RANDOM.nextGaussian() * 0.05D;
            entityitem.motionY = RANDOM.nextGaussian() * 0.05D + 0.2D;
            entityitem.motionZ = RANDOM.nextGaussian() * 0.05D;
            worldIn.spawnEntity(entityitem);
        }
    }
    
    public static boolean compareItemStacks(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == OreDictionary.WILDCARD_VALUE || stack2.getMetadata() == stack1.getMetadata());
    }
    
}
