package com.wexalian.mods.minetech.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class MultiblockManager
{
    public static final MultiblockManager INSTANCE = new MultiblockManager();
    
    private Map<ResourceLocation, Multiblock> multiblockMap = new HashMap<>();
    
    private MultiblockManager() {}
    
    public Multiblock get(ResourceLocation id)
    {
        return multiblockMap.get(id);
    }
    
    public void clear()
    {
        multiblockMap.clear();
    }
    
    public void addMultiblocks(Map<ResourceLocation, Multiblock> newMultiblocks)
    {
        multiblockMap.putAll(newMultiblocks);
    }
    
    public boolean tryTrigger(IBlockState state, World world, BlockPos pos)
    {
        for (Multiblock multiblock : multiblockMap.values())
        {
            if (multiblock.doesTrigger(state))
            {
                BlockPos offset = multiblock.getTriggerOffset();
                multiblock.trigger(world, pos.add(-offset.getX(), -offset.getY(), -offset.getZ()));
                return true;
            }
        }
        return false;
    }
}
