package wexalian.mods.minetech.gui;

import wexalian.mods.minetech.container.ContainerGrindstone;
import wexalian.mods.minetech.lib.Reference;
import wexalian.mods.minetech.tileentity.TileEntityGrindstone;
import wexalian.mods.minetech.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiGrindstone extends GuiContainer
{
    public static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/grindstone/grindstone.png");
    public static final ResourceLocation WHEEL = new ResourceLocation(Reference.MOD_ID, "textures/gui/grindstone/grindstone_wheel.png");
    
    public static final int X_SIZE = 176;
    public static final int Y_SIZE = 166;
    
    public static final int WHEEL_X = 80;
    public static final int WHEEL_Y = 36;
    public static final int WHEEL_WIDTH = 16;
    public static final int WHEEL_HEIGHT = 16;
    
    public static final float ROTATION_VALUE = 360F / TileEntityGrindstone.MAX_PROGRESS;
    
    public static final String NAME = "Grindstone";
    
    private TileEntityGrindstone tile;
    
    public GuiGrindstone(TileEntityGrindstone tileEntity, EntityPlayer player)
    {
        super(new ContainerGrindstone(tileEntity, player));
        tile = tileEntity;
    }
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
    
    @SuppressWarnings("MagicNumber")
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(BACKGROUND);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, X_SIZE, Y_SIZE);
        
        GlStateManager.translate(guiLeft + WHEEL_X + WHEEL_WIDTH / 2, guiTop + WHEEL_Y + WHEEL_WIDTH / 2, 0);
        GlStateManager.rotate(tile.getProgress() * ROTATION_VALUE, 0F, 0F, 1F);
        
        GuiUtil.drawTexture(-WHEEL_WIDTH / 2, -WHEEL_WIDTH / 2, WHEEL_WIDTH, WHEEL_HEIGHT, WHEEL);
        
        GlStateManager.rotate(-tile.getProgress() * ROTATION_VALUE, 0F, 0F, 1F);
        GlStateManager.translate(-(guiLeft + WHEEL_X + WHEEL_WIDTH / 2), -(guiTop + WHEEL_Y + WHEEL_WIDTH / 2), 0);
    }
    
    @SuppressWarnings("MagicNumber")
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRenderer.drawString(NAME, xSize / 2 - fontRenderer.getStringWidth(NAME) / 2, 6, 0x404040);
    }
}
