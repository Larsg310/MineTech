package com.wexalian.mods.minetech.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class MultiblockValidator
{
    private final Predicate<IBlockState>[][][] predicates;
    private final Predicate<IBlockState> triggerPredicate;
    
    public MultiblockValidator(@Nonnull Predicate<IBlockState>[][][] predicates, @Nonnull Predicate<IBlockState> triggerPredicate)
    {
        this.predicates = predicates;
        this.triggerPredicate = triggerPredicate;
    }
    
    public boolean isStateValid(@Nonnull IBlockState state, @Nonnull BlockPos offset)
    {
        return predicates[offset.getX()][offset.getY()][offset.getZ()].test(state);
    }
    
    public boolean doesTrigger(@Nonnull IBlockState state)
    {
        return triggerPredicate.test(state);
    }
    
    public boolean isValid(World world, BlockPos pos)
    {
        for (int x = 0; x < predicates.length; x++)
        {
            for (int y = 0; y < predicates[x].length; y++)
            {
                for (int z = 0; z < predicates[x][y].length; z++)
                {
                    BlockPos checkPos = pos.add(x, y, z);
                    IBlockState state = world.getBlockState(checkPos);
                    
                    if (!isStateValid(state, new BlockPos(x, y, z))) return false;
                }
            }
        }
        return true;
    }
}
