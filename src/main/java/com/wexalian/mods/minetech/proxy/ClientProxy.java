package com.wexalian.mods.minetech.proxy;

import com.wexalian.mods.minetech.lib.Reference;
import com.wexalian.mods.minetech.render.*;
import com.wexalian.mods.minetech.tileentity.*;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityGear;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityShaft;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityWaterWheel;
import com.wexalian.mods.minetech.tileentity.multiblock.TileEntityOreMill;
import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements IProxy
{
    @Override
    public void registerRenderers()
    {
        OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRotating.class, new TileEntityRotatingRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBelt.class, new TileEntityBeltRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGear.class, new TileEntityGearRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShaft.class, new TileEntityShaftRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWaterWheel.class, new TileEntityWaterWheelRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOreMill.class, new TileEntityOreMillRenderer());
    }
    
    @Override
    public int getWaterColor()
    {
        return BiomeColors.getWaterColor(Minecraft.getInstance().world, Minecraft.getInstance().player.getPosition());
    }
    
}
