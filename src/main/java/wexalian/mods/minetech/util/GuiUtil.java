package wexalian.mods.minetech.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiUtil
{
    /**
     * Draws a textured rectangle at z = 0;
     *
     * @param xPos          X coordinate to start drawing at
     * @param yPos          Y coordinate to start drawing at
     * @param width         Width of the rectangle
     * @param height        Height of the rectangle
     * @param uMin          Starting position in the texture to start drawing at, in pixels.
     * @param vMin          Starting position in the texture to start drawing at, in pixels.
     * @param uMax          Stopping position in the texture to start drawing at, in pixels.
     * @param vMax          Stopping position in the texture to start drawing at, in pixels.
     * @param textureWidth  Width of the texture in the default texture pack, in pixels.
     * @param textureHeight Height of the texture in the default texture pack, in pixels.
     * @param texture       The texture to render to screen
     */
    public static void drawTexture(int xPos, int yPos, int width, int height, float uMin, float uMax, float vMin, float vMax, float textureWidth, float textureHeight, ResourceLocation texture)
    {
        float uScale = 1F / textureWidth;
        float vScale = 1F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(xPos, yPos + height, 0).tex(uMin * uScale, vMax * vScale).endVertex();
        vertexbuffer.pos(xPos + width, yPos + height, 0).tex(uMax * uScale, vMax * vScale).endVertex();
        vertexbuffer.pos(xPos + width, yPos, 0).tex(uMax * uScale, vMin * vScale).endVertex();
        vertexbuffer.pos(xPos, yPos, 0).tex(uMin * uScale, vMin * vScale).endVertex();
        tessellator.draw();
    }
    
    /**
     * Draws a textured rectangle at z = 0; The <code>uMin</code>, <code>uMax</code>, <code>vMin</code> and <code>vMax</code> are the same as <code>xPos</code>, <code>xPos+width</code>, <code>yPos</code> and <code>yPos+height</code>.
     * </p>
     * Used for drawing a 1:1 ratio square from the texture.
     * </p>
     *
     * @param xPos          X coordinate to start drawing at
     * @param yPos          Y coordinate to start drawing at
     * @param width         Width of the rectangle
     * @param height        Height of the rectangle
     * @param textureWidth  Width of the texture in the default texture pack, in pixels.
     * @param textureHeight Height of the texture in the default texture pack, in pixels.
     * @param texture       The texture to render to screen
     */
    public static void drawTexture(int xPos, int yPos, int width, int height, float textureWidth, float textureHeight, ResourceLocation texture)
    {
        drawTexture(xPos, yPos, width, height, xPos, xPos + width, yPos, yPos + height, textureWidth, textureHeight, texture);
    }
    
    /**
     * Draws a textured rectangle at z = 0; The <code>uMin</code>, <code>uMax</code>, <code>vMin</code> and <code>vMax</code> are the same as <code>xPos</code>, <code>xPos+width</code>, <code>yPos</code> and <code>yPos+height</code>. The <code>textureWidth</code> and <code>textureHeight</code> are the same as <code>width</code> and <code>height</code>.
     * </p>
     * This is for rectangles which are the same size as the texture.
     * </p>
     *
     * @param xPos    X coordinate to start drawing at
     * @param yPos    Y coordinate to start drawing at
     * @param width   Width of the rectangle
     * @param height  Height of the rectangle
     * @param texture The texture to render to screen
     */
    public static void drawTexture(int xPos, int yPos, int width, int height, ResourceLocation texture)
    {
        drawTexture(xPos, yPos, width, height, 0, width, 0, height, width, height, texture);
    }
    
    public static void drawHoveringText(List<String> textLines, int x, int y, int screenWidth, int screenHeight)
    {
        drawHoveringText(textLines, x, y, screenWidth, screenHeight, Minecraft.getMinecraft().fontRenderer);
    }
    
    public static void drawHoveringText(List<String> textLines, int x, int y, int screenWidth, int screenHeight, FontRenderer font)
    {
        if (!textLines.isEmpty())
        {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;
            for (String s : textLines)
            {
                int j = font.getStringWidth(s);
                
                if (j > i)
                {
                    i = j;
                }
            }
            int xPos = x + 17;
            int yPos = y - 7;
            int k = 8;
            
            if (textLines.size() > 1)
            {
                k += 2 + (textLines.size() - 1) * 10;
            }
            
            if (xPos + i > screenWidth)
            {
                xPos -= 28 + i;
            }
            
            if (yPos + k + 6 > screenHeight)
            {
                yPos = screenHeight - k - 6;
            }
            
            int color1 = 0xF0100010;
            drawGradientRect(xPos - 3, yPos - 4, xPos + i + 3, yPos - 3, color1, color1);
            drawGradientRect(xPos - 3, yPos + k + 3, xPos + i + 3, yPos + k + 4, color1, color1);
            drawGradientRect(xPos - 3, yPos - 3, xPos + i + 3, yPos + k + 3, color1, color1);
            drawGradientRect(xPos - 4, yPos - 3, xPos - 3, yPos + k + 3, color1, color1);
            drawGradientRect(xPos + i + 3, yPos - 3, xPos + i + 4, yPos + k + 3, color1, color1);
            int color2 = 0x505000FF;
            int color3 = 0x5028007F;
            drawGradientRect(xPos - 3, yPos - 3 + 1, xPos - 3 + 1, yPos + k + 3 - 1, color2, color3);
            drawGradientRect(xPos + i + 2, yPos - 3 + 1, xPos + i + 3, yPos + k + 3 - 1, color2, color3);
            drawGradientRect(xPos - 3, yPos - 3, xPos + i + 3, yPos - 3 + 1, color2, color2);
            drawGradientRect(xPos - 3, yPos + k + 2, xPos + i + 3, yPos + k + 3, color3, color3);
            
            for (int k1 = 0; k1 < textLines.size(); ++k1)
            {
                String s1 = textLines.get(k1);
                font.drawStringWithShadow(s1, (float) xPos, (float) yPos, -1);
                
                if (k1 == 0)
                {
                    yPos += 2;
                }
                
                yPos += 10;
            }
            
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
    
    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float alpha1 = (float) (startColor >> 24 & 255) / 255F;
        float red1 = (float) (startColor >> 16 & 255) / 255F;
        float green1 = (float) (startColor >> 8 & 255) / 255F;
        float blue1 = (float) (startColor & 255) / 255F;
        
        float alpha2 = (float) (endColor >> 24 & 255) / 255F;
        float red2 = (float) (endColor >> 16 & 255) / 255F;
        float green2 = (float) (endColor >> 8 & 255) / 255F;
        float blue2 = (float) (endColor & 255) / 255F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos((double) right, (double) top, (double) 0).color(red1, green1, blue1, alpha1).endVertex();
        vertexbuffer.pos((double) left, (double) top, (double) 0).color(red1, green1, blue1, alpha1).endVertex();
        vertexbuffer.pos((double) left, (double) bottom, (double) 0).color(red2, green2, blue2, alpha2).endVertex();
        vertexbuffer.pos((double) right, (double) bottom, (double) 0).color(red2, green2, blue2, alpha2).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
