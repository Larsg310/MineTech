package wexalian.mods.minetech.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IWorldUtils
{
    default <T> T getTileEntity(IBlockAccess world, BlockPos pos)
    {
        return (T) world.getTileEntity(pos);
    }
}
