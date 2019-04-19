package com.wexalian.mods.minetech.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;

public class TileEntityBelt extends TileEntity implements ITickable
{
    public static TileEntityType<TileEntityBelt> TYPE;
    private BeltContent[] leftLane = new BeltContent[4]; //0 is last item added to belt, 4 is earliest item added
    private BeltContent[] rightLane = new BeltContent[4];
    
    public TileEntityBelt()
    {
        super(TYPE);
        leftLane[0] = new BeltContent(new ItemStack(Blocks.DIAMOND_ORE));
        // leftLane[1] = new BeltContent(new ItemStack(Blocks.STONE));
        // leftLane[2] = new BeltContent(new ItemStack(Blocks.GRASS_BLOCK));
        // leftLane[3] = new BeltContent(new ItemStack(Blocks.DIAMOND_BLOCK));
    }
    
    @Override
    public void tick()
    {
        // float movePerTick = (float) getLaneSpeed() / 20F;
        // if (movePerTick < 1)
        // {
        //     if (world.getGameTime() % (1 / movePerTick) == 0)
        //     {
        //         moveContents(1);
        //     }
        // }
        // else
        // {
        //     moveContents(Math.round(movePerTick));
        // }
    }
    
    // private void moveContents(int steps)
    // {
    // for (int index = leftLane.length - 1; index >= 0; index--)
    // {
    //     if (leftLane[index] != null)
    //     {
    //         move(index, leftLane[index], steps);
    //     }
    // }
    
    // }
    
    // public boolean canMoveTo()
    // {
    //     return leftLane[0] == null;
    // }
    //
    // public void move(int startPos, BeltContent content, int step)
    // {
    //     if (startPos + step >= leftLane.length)
    //     {
    //         TileEntity nextTile = world.getTileEntity(pos.offset(getBlockState().get(BlockBelt.FACING)));
    //         if (nextTile instanceof TileEntityBelt && ((TileEntityBelt) nextTile).canMoveTo())
    //         {
    //             ((TileEntityBelt) nextTile).move(0, content, step);
    //             leftLane[startPos]=null;
    //         }
    //         else
    //         {
    //             leftLane[3] = null;
    //         }
    // }
    // else
    // {
    //     for (int index = startPos; index < startPos + step; index++)
    //     {
    //         if (index + 1 < leftLane.length)
    //         {
    //             if (leftLane[index + 1] != null)
    //             {
    //                 if(index != 0)
    //                 {
    //                     leftLane[index] = content;
    //                     leftLane[startPos] = null;
    //                 }
    //             }
    //         }
    //     }
    // }
    // }
    
    public int getLaneSpeed()
    {
        return 1;
    }
    
    public BeltContent[] getLeftLane()
    {
        return leftLane;
    }
    
    public class BeltContent
    {
        private ItemStack stack;
        
        public BeltContent(ItemStack stack)
        {
            this.stack = stack;
        }
        
        public ItemStack getStack()
        {
            return stack;
        }
    }
}
