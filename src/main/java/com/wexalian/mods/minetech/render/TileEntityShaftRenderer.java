package com.wexalian.mods.minetech.render;

import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityShaft;
import com.wexalian.mods.minetech.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Objects;

public class TileEntityShaftRenderer extends TileEntityRenderer<TileEntityShaft>
{
    @SuppressWarnings("Duplicates")
    @Override
    public void render(TileEntityShaft tile, double x, double y, double z, float partialTicks, int destroyStage)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
        EnumFacing face = tile.getRotationFacing();
        Vec3i axis = face.getDirectionVec();
        float angle = tile.getNode().getAngle(partialTicks);
        GlStateManager.rotatef(angle, Math.abs(axis.getX()), Math.abs(axis.getY()), Math.abs(axis.getZ()));
        float scale = (float) Math.sqrt(1F/*tile.getScale()*/);
        GlStateManager.scaled(scale, scale, scale);
        GlStateManager.translated(-0.5D - (scale - 1) / 2F, -0.5D - (scale - 1) / 2F, -0.5D - (scale - 1) / 2F);
        World world = Objects.requireNonNull(tile.getWorld());
        BlockPos pos = tile.getPos();
        IBlockState state = world.getBlockState(pos);
        
        RenderUtil.renderBlockStateInWorld(state, world, pos, scale);
        
        RenderHelper.enableStandardItemLighting();
        
        GlStateManager.popMatrix();
    }
}
