package com.wexalian.mods.minetech.event;

import com.wexalian.mods.minetech.tileentity.TileEntityRotating;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ClientEvents
{
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onBlockDrawHighlight(DrawBlockHighlightEvent event)
    {
        if (event.getTarget() != null && event.getTarget().type == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = event.getTarget().getBlockPos();
            EntityPlayer player = event.getPlayer();
            World world = player.getEntityWorld();
            TileEntity te = world.getTileEntity(pos);
            
            if (te instanceof TileEntityRotating && world.getWorldBorder().contains(pos))
            {
                event.setCanceled(true);
                
                TileEntityRotating tile = (TileEntityRotating) te;
                
                GlStateManager.enableBlend();
                GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.lineWidth(Math.max(2.5F, (float) Minecraft.getInstance().mainWindow.getFramebufferWidth() / 1920.0F * 2.5F));
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                // GlStateManager.matrixMode(GL11.GL_PROJECTION);
                GlStateManager.pushMatrix();
                GlStateManager.scalef(1.0F, 1.0F, 0.999F);
                
                double xOff = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.getPartialTicks() - pos.getX();
                double yOff = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.getPartialTicks() - pos.getY();
                double zOff = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.getPartialTicks() - pos.getZ();
                
                GlStateManager.translated(0.5 - xOff, 0.5 - yOff, 0.5 - zOff);
                
                Vec3i axis = tile.getRotationFacing().getDirectionVec();
                GlStateManager.rotatef(tile.getAngle(event.getPartialTicks()), Math.abs(axis.getX()), Math.abs(axis.getY()), Math.abs(axis.getZ()));
                
                GlStateManager.translated(-0.5, -0.5, -0.5);
    
                IBlockState state = world.getBlockState(pos);
                WorldRenderer.drawShape(state.getShape(world, pos), 0, 0, 0, 0.0F, 0.0F, 0.0F, 0.4F);
                
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
            }
        }
    }
}
