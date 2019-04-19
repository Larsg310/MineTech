package com.wexalian.mods.minetech.multiblock;

import com.wexalian.mods.minetech.tileentity.multiblock.TileEntityMultiblock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class Multiblock
{
    private ResourceLocation id;
    private String name;
    private BlockPos size;
    private BlockPos center;
    private BlockPos triggerOffset;
    private IBlockState block;
    private IBlockState trigger;
    private IBlockState[][][] structure;
    
    private MultiblockValidator validator;
    
    public Multiblock(ResourceLocation id, String name, BlockPos size, BlockPos triggerOffset, IBlockState block, IBlockState trigger, IBlockState[][][] structure, BlockPos center, MultiblockValidator validator)
    {
        this.id = id;
        this.name = name;
        this.size = size;
        this.block = block;
        this.triggerOffset = triggerOffset;
        this.trigger = trigger;
        this.structure = structure;
        this.center = center;
        this.validator = validator;
    }
    
    public boolean doesTrigger(IBlockState state)
    {
        return validator.doesTrigger(state);
    }
    
    public void trigger(World world, BlockPos startPos)
    {
        if (isValid(world, startPos))
        {
            complete(world, startPos);
            System.out.println("complete");
        }
    }
    
    public void complete(World world, BlockPos startPos)
    {
        for (int x = 0; x < size.getX(); x++)
        {
            for (int y = 0; y < size.getY(); y++)
            {
                for (int z = 0; z < size.getZ(); z++)
                {
                    BlockPos pos = startPos.add(x, y, z);
                    world.setBlockState(pos, block);
                    ((TileEntityMultiblock) Objects.requireNonNull(world.getTileEntity(pos))).setMultiblock(this, new BlockPos(x, y, z));
                }
            }
        }
    }
    
    private boolean isValid(World world, BlockPos startPos)
    {
        return validator.isValid(world, startPos);
    }
    
    public BlockPos getTriggerOffset()
    {
        return triggerOffset;
    }
    
    public BlockPos getCenter()
    {
        return center;
    }
    
    public ResourceLocation getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public BlockPos getSize()
    {
        return size;
    }
    
    public IBlockState[][][] getStructure()
    {
        return structure;
    }
    
    public IBlockState getTriggerState()
    {
        return trigger;
    }
}
