package com.wexalian.mods.minetech.event;

import com.wexalian.mods.minetech.api.block.ICustomBlockHighlight;
import com.wexalian.mods.minetech.api.block.IRotatedBlockHighlight;
import com.wexalian.mods.minetech.container.slot.SlotFilter;
import com.wexalian.mods.minetech.item.IItemHighlighter;
import com.wexalian.mods.minetech.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SuppressWarnings("Duplicates")
public class ClientEvents
{
    private static final ResourceLocation OVERLAY = new ResourceLocation(Reference.MOD_ID, "textures/gui/slot_overlay.png");
    
    @SubscribeEvent
    public static void onBlockDrawHighlight(DrawBlockHighlightEvent event)
    {
        if (event.getTarget() != null && event.getTarget().type == RayTraceResult.Type.BLOCK)
        {
            BlockPos pos = event.getTarget().getBlockPos();
            EntityPlayer player = event.getPlayer();
            World world = player.getEntityWorld();
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            
            if (block instanceof IRotatedBlockHighlight && world.getWorldBorder().contains(pos))
            {
                event.setCanceled(true);
                renderRotatedBlockHighlight(world, pos, state, player, (IRotatedBlockHighlight) block, event.getPartialTicks());
            }
            else if (block instanceof ICustomBlockHighlight && world.getWorldBorder().contains(pos))
            {
                event.setCanceled(true);
                renderCustomBlockHighlight(world, pos, state, player, (ICustomBlockHighlight) block, event.getPartialTicks());
            }
        }
    }
    
    private static void renderRotatedBlockHighlight(World world, BlockPos pos, IBlockState state, EntityPlayer player, IRotatedBlockHighlight block, float partialTicks)
    {
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.lineWidth(Math.max(2.5F, (float) Minecraft.getInstance().mainWindow.getFramebufferWidth() / 1920.0F * 2.5F));
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        // GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.scalef(1.0F, 1.0F, 0.999F);
        
        double xOff = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks - pos.getX();
        double yOff = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks - pos.getY();
        double zOff = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks - pos.getZ();
        
        List<Pair<VoxelShape, Pair<EnumFacing.Axis, Float>>> list = block.getBlockHighlightShapes(state, world, pos, partialTicks);
        
        for (Pair<VoxelShape, Pair<EnumFacing.Axis, Float>> shapeToAngle : list)
        {
            GlStateManager.pushMatrix();
            VoxelShape shape = shapeToAngle.getLeft();
            Pair<EnumFacing.Axis, Float> rotationData = shapeToAngle.getRight();
            
            if (rotationData != null)
            {
                GlStateManager.translated(0.5 - xOff, 0.5 - yOff, 0.5 - zOff);
                float rotateX = rotationData.getLeft().getCoordinate(1, 0, 0);
                float rotateY = rotationData.getLeft().getCoordinate(0, 1, 0);
                float rotateZ = rotationData.getLeft().getCoordinate(0, 0, 1);
                
                GlStateManager.rotatef(rotationData.getRight(), rotateX, rotateY, rotateZ);
                GlStateManager.translated(-0.5, -0.5, -0.5);
            }
            else GlStateManager.translated(-xOff, -yOff, -zOff);
            
            WorldRenderer.drawShape(shape, 0, 0, 0, 0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.popMatrix();
        }
        
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    private static void renderCustomBlockHighlight(World world, BlockPos pos, IBlockState state, EntityPlayer player, ICustomBlockHighlight block, float partialTicks)
    {
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.lineWidth(Math.max(2.5F, (float) Minecraft.getInstance().mainWindow.getFramebufferWidth() / 1920.0F * 2.5F));
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.scalef(1.0F, 1.0F, 0.999F);
        
        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
        
        VoxelShape shape = block.getBlockHighlight(world, pos, state, player);
        WorldRenderer.drawShape(shape, (double) pos.getX() - d0, (double) pos.getY() - d1, (double) pos.getZ() - d2, 0.0F, 0.0F, 0.0F, 0.4F);
        
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    @SubscribeEvent
    public static void onGuiContainerForegroundDraw(GuiContainerEvent.DrawBackground event)
    {
        GuiContainer guiContainer = event.getGuiContainer();
        Slot hoveredSlot = guiContainer.getSlotUnderMouse();
        if (hoveredSlot != null)
        {
            if (hoveredSlot instanceof SlotFilter)
            {
                for (Slot slot : guiContainer.inventorySlots.inventorySlots)
                {
                    if (slot.getHasStack())
                    {
                        if (hoveredSlot.isItemValid(slot.getStack()))
                        {
                            drawSlotOverlay(slot);
                        }
                    }
                }
            }
            else if (hoveredSlot.getStack().getItem() instanceof IItemHighlighter)
            {
                for (Slot slot : guiContainer.inventorySlots.inventorySlots)
                {
                    if (slot.getHasStack())
                    {
                        if (((IItemHighlighter) hoveredSlot.getStack().getItem()).shouldHighlight(slot.getStack()))
                        {
                            drawSlotOverlay(slot);
                        }
                    }
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void drawSlotOverlay(Slot slot)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(OVERLAY);
        drawModalRectWithCustomSizedTexture(slot.xPos - 1, slot.yPos - 1, 0, 0, 0, 18, 18, 18, 18);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void drawModalRectWithCustomSizedTexture(int x, int y, int z, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float pixelWidth = 1.0F / textureWidth;
        float pixelHeight = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, z).tex(u * pixelWidth, v + height * pixelHeight).endVertex();
        bufferbuilder.pos(x + width, y + height, z).tex(u + width * pixelWidth, v + height * pixelHeight).endVertex();
        bufferbuilder.pos(x + width, y, z).tex(u + width * pixelWidth, v * pixelHeight).endVertex();
        bufferbuilder.pos(x, y, z).tex(u * pixelWidth, v * pixelHeight).endVertex();
        tessellator.draw();
    }
}
