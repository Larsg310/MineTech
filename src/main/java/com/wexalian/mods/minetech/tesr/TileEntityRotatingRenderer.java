package com.wexalian.mods.minetech.tesr;

import com.wexalian.mods.minetech.tileentity.TileEntityRotating;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class TileEntityRotatingRenderer extends TileEntityRenderer<TileEntityRotating>
{
    @Override
    public void render(TileEntityRotating te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x + 0.5, y + 0.5, z + 0.5);
        // System.out.println(x);
        Vec3i axis = te.getRotationFacing().getDirectionVec();
        GlStateManager.rotatef(te.getAngle(partialTicks), Math.abs(axis.getX()), Math.abs(axis.getY()), Math.abs(axis.getZ()));
        float scale = te.getScale();
        GlStateManager.scaled(scale, scale, scale);
        GlStateManager.translated(-0.5 - (scale - 1) / 2F * axis.getX(), -0.5 - (scale - 1) / 2F * axis.getY(), -0.5 - (scale - 1) / 2F * axis.getZ());
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        World world = te.getWorld();
        BlockPos pos = te.getPos();
        IBlockState state = world.getBlockState(pos);
        IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state);
        
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        
        if (Minecraft.isAmbientOcclusionEnabled()) GlStateManager.shadeModel(GL11.GL_SMOOTH);
        else GlStateManager.shadeModel(GL11.GL_FLAT);
        
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        buffer.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, model, state, pos, buffer, true, world.getRandom(), 42);
        buffer.setTranslation(0, 0, 0);
        tessellator.draw();
        
        RenderHelper.enableStandardItemLighting();
        
        GlStateManager.popMatrix();
    }
}
