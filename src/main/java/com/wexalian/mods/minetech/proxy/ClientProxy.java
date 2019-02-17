package com.wexalian.mods.minetech.proxy;

import com.wexalian.mods.minetech.gui.GuiGrindstone;
import com.wexalian.mods.minetech.tesr.TileEntityRotatingRenderer;
import com.wexalian.mods.minetech.tileentity.TileEntityCrank;
import com.wexalian.mods.minetech.tileentity.TileEntityGrindstone;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements IProxy
{
    @Override
    public void registerTileEntityRenderers()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrank.class, new TileEntityRotatingRenderer());
    }
    
    @Override
    public void registerGuis()
    {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> p -> {
            PacketBuffer buffer = p.getAdditionalData();
            BlockPos pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
            return new GuiGrindstone((TileEntityGrindstone) Minecraft.getInstance().world.getTileEntity(pos), Minecraft.getInstance().player.inventory);
        });
    }
    
}
