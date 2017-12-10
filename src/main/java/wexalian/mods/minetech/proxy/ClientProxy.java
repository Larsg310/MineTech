package wexalian.mods.minetech.proxy;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import wexalian.mods.minetech.render.tileentity.TileEntityCrankRenderer;
import wexalian.mods.minetech.tileentity.TileEntityCrank;

public class ClientProxy implements IProxy
{
    @Override
    public void init()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrank.class, new TileEntityCrankRenderer());
    }
}
