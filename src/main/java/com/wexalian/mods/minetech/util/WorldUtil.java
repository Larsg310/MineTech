package com.wexalian.mods.minetech.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public final class WorldUtil
{
    public static void spawnItemStack(World world, BlockPos pos, @Nonnull ItemStack stack)
    {
        spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    }
    
    public static void spawnItemStack(World world, double x, double y, double z, @Nonnull ItemStack stack)
    {
        float randX = world.rand.nextFloat() * 0.8F + 0.1F;
        float randY = world.rand.nextFloat() * 0.8F + 0.1F;
        float randZ = world.rand.nextFloat() * 0.8F + 0.1F;
        
        while (stack.getCount() > 0)
        {
            int i = world.rand.nextInt(21) + 10;
            
            EntityItem entityitem = new EntityItem(world, x + randX, y + randY, z + randZ, stack.split(i));
            
            entityitem.motionX = world.rand.nextGaussian() * 0.05D;
            entityitem.motionY = world.rand.nextGaussian() * 0.05D + 0.2D;
            entityitem.motionZ = world.rand.nextGaussian() * 0.05D;
            world.spawnEntity(entityitem);
        }
    }
}
