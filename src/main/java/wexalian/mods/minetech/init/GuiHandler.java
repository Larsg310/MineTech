package wexalian.mods.minetech.init;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wexalian.mods.minetech.container.ContainerGrindstone;
import wexalian.mods.minetech.gui.GuiGrindstone;
import wexalian.mods.minetech.util.WorldUtil;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler
{
    @Nullable
    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == IDs.GRINDSTONE) return new ContainerGrindstone(WorldUtil.getTileEntity(world, new BlockPos(x, y, z)), player);
        return null;
    }
    
    @Nullable
    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == IDs.GRINDSTONE) return new GuiGrindstone(WorldUtil.getTileEntity(world, new BlockPos(x, y, z)), player);
        return null;
    }
    
    public static class IDs
    {
        public static final int GRINDSTONE = 0;
    }
}
