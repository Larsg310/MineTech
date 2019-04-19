package com.wexalian.mods.minetech.multiblock;

import com.wexalian.mods.minetech.MineTechMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.function.Predicate;

public class MultiblockMatcher
{
    Predicate<IBlockState>[][][] structure;
    Predicate<IBlockState> trigger;
    BlockPos triggerPos;
    
    @SuppressWarnings("unchecked")
    public MultiblockMatcher(IBlockState trigger, IBlockState[][][] structure)
    {
        this.structure = new Predicate[structure.length][structure[0].length][structure[0][0].length];
        this.trigger = getStateMatcher(trigger);
        generatePredicateStructure(structure);
        if (triggerPos == null) MineTechMod.LOGGER.error("Error loading multiblock matcher: no trigger position found");
    }
    
    private void generatePredicateStructure(IBlockState[][][] blockStructure)
    {
        for (int x = 0; x < blockStructure.length; x++)
        {
            for (int y = 0; y < blockStructure[0].length; y++)
            {
                for (int z = 0; z < blockStructure[0][0].length; z++)
                {
                    structure[x][y][z] = getStateMatcher(blockStructure[x][y][z]);
                    if (trigger.test(blockStructure[x][y][z]))
                    {
                        triggerPos = new BlockPos(x, y, z);
                    }
                }
            }
        }
    }
    
    private static Predicate<IBlockState> getStateMatcher(IBlockState state)
    {
        return s -> {
            if (s.getBlock() != state.getBlock()) return false;
            StateContainer<Block, IBlockState> container = state.getBlock().getStateContainer();
            
            for (IProperty<?> property : container.getProperties())
            {
                if (!s.has(property)) return false;
                if (s.get(property) != state.get(property)) return false;
            }
            return true;
        };
    }
    
    public boolean doesTrigger(IBlockState state)
    {
        return trigger.test(state);
    }
    
    public boolean isMultiblockComplete(IBlockReader world, BlockPos pos)
    {
        BlockPos base = pos.add(-triggerPos.getX(), -triggerPos.getY(), -triggerPos.getZ());
        
        for (int x = 0; x < structure.length; x++)
        {
            for (int y = 0; y < structure[0].length; y++)
            {
                for (int z = 0; z < structure[0][0].length; z++)
                {
                    IBlockState state = world.getBlockState(base.add(x, y, z));
                    
                    if (!structure[x][y][z].test(state))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
