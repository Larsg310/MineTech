package com.wexalian.mods.minetech.render;

import com.wexalian.mods.minetech.block.BlockOreMill;
import com.wexalian.mods.minetech.tileentity.multiblock.TileEntityOreMill;
import com.wexalian.mods.minetech.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

import java.util.Objects;

public class TileEntityOreMillRenderer extends TileEntityRenderer<TileEntityOreMill>
{
    @Override
    public void render(TileEntityOreMill tile, double x, double y, double z, float partialTicks, int breakStage)
    {
        if (tile.isCenter())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translated(x,y,z);
            RenderUtil.renderBlockStateInWorld(tile.getBlockState().with(BlockOreMill.STATE, 0), Objects.requireNonNull(tile.getWorld()), tile.getPos(), 1F);
            RenderUtil.renderBlockStateInWorld(tile.getBlockState().with(BlockOreMill.STATE, 1), Objects.requireNonNull(tile.getWorld()), tile.getPos(), 1F);
            GlStateManager.popMatrix();
        }
    }
}
