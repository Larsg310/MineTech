package com.wexalian.mods.minetech.gui;

import com.wexalian.mods.minetech.lib.BlockNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import java.util.HashMap;
import java.util.Map;

public class GuiHandler
{
    private static final Map<ResourceLocation, GuiEntry> guiEntries = new HashMap<>();
    
    static
    {
        guiEntries.put(BlockNames.GRINDSTONE, GuiGrindstone::new);
    }
    
    public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer)
    {
        ResourceLocation id = openContainer.getId();
        BlockPos pos = openContainer.getAdditionalData().readBlockPos();
        
        return guiEntries.getOrDefault(id, (w, p, i) -> null).apply(Minecraft.getInstance().world, pos, Minecraft.getInstance().player);
    }
    
    @FunctionalInterface
    private interface GuiEntry
    {
        GuiScreen apply(World world, BlockPos pos, EntityPlayer player);
    }
}
