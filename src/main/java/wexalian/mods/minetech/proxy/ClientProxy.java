package wexalian.mods.minetech.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import wexalian.mods.minetech.render.tileentity.TileEntityCrankRenderer;
import wexalian.mods.minetech.tileentity.TileEntityCrank;

public class ClientProxy implements IProxy
{
    @Override
    public void init()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrank.class, new TileEntityCrankRenderer());
    }
    
    @Override
    public void schedule(Side side, Runnable task)
    {
        if (side == Side.SERVER) FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(task);
        if (side == Side.CLIENT) Minecraft.getMinecraft().addScheduledTask(task);
    }
}
