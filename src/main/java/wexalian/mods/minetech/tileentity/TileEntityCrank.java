package wexalian.mods.minetech.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import wexalian.mods.minetech.recipe.GrindstoneRecipes;
import wexalian.mods.minetech.util.WorldUtil;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;

public class TileEntityCrank extends TileEntityRotating implements ITickable
{
    public static final int MAX_CRANK_TIME = 20;
    
    private int crankTime = 0;
    
    @Override
    public void update()
    {
        if (crankTime > 0) crankTime--;
    }
    
    public boolean tryCrank()
    {
        if (canCrank())
        {
            crank();
            return true;
        }
        return false;
    }
    
    private boolean canCrank()
    {
        if (crankTime != 0) return false;
        TileEntityGrindstone tile = WorldUtil.getTileEntity(world, pos.down());
        
        return IntStream.range(0, tile.inventory.getSlots())//
                        .anyMatch(slot -> !GrindstoneRecipes.instance().getGrindingResult(tile.inventory.getStackInSlot(slot)).isEmpty());//
    }
    
    private void crank()
    {
        crankTime += MAX_CRANK_TIME;
    }
    
    public boolean isCranking()
    {
        return crankTime > 0;
    }
    
    @Override
    public float getAngle(float partialTicks)
    {
        if (isCranking()) return 18F * crankTime - 18F * partialTicks;
        return 0F;
    }
    
    @Nonnull
    @Override
    public EnumFacing.Axis getRotationAxis()
    {
        return EnumFacing.Axis.Y;
    }
}
