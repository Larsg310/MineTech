package com.wexalian.mods.minetech.gui;

import com.wexalian.mods.minetech.container.ContainerGrindstone;
import com.wexalian.mods.minetech.tileentity.TileEntityGrindstone;
import com.wexalian.mods.minetech.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import com.wexalian.mods.minetech.lib.Reference;

public class GuiGrindstone extends GuiContainer
{
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/grindstone/grindstone.png");
    private static final ResourceLocation WHEEL = new ResourceLocation(Reference.MOD_ID, "textures/gui/grindstone/grindstone_wheel.png");

    private static final int X_SIZE = 176;
    private static final int Y_SIZE = 166;

    private static final int WHEEL_X = 80;
    private static final int WHEEL_Y = 36;
    private static final int WHEEL_WIDTH = 16;
    private static final int WHEEL_HEIGHT = 16;

    private static final float ROTATION_VALUE = 360F / TileEntityGrindstone.MAX_PROGRESS;

    private static final String NAME = "Grindstone";

    private TileEntityGrindstone tile;

    public GuiGrindstone(TileEntityGrindstone tileEntity, InventoryPlayer playerInventory)
    {
        super(new ContainerGrindstone(tileEntity, playerInventory));
        tile = tileEntity;
    }

    public void render(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @SuppressWarnings("MagicNumber")
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRenderer.drawString(NAME, xSize / 2F - fontRenderer.getStringWidth(NAME) / 2F, 6, 0x404040);
    }

    @SuppressWarnings("MagicNumber")
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, X_SIZE, Y_SIZE);

        GlStateManager.translated(guiLeft + WHEEL_X + WHEEL_WIDTH / 2, guiTop + WHEEL_Y + WHEEL_WIDTH / 2, 0);
        GlStateManager.rotatef(tile.getProgress() * ROTATION_VALUE, 0F, 0F, 1F);
        GuiUtil.drawTexture(-WHEEL_WIDTH / 2, -WHEEL_WIDTH / 2, WHEEL_WIDTH, WHEEL_HEIGHT, WHEEL);
        GlStateManager.rotatef(-tile.getProgress() * ROTATION_VALUE, 0F, 0F, 1F);
        GlStateManager.translated(-(guiLeft + WHEEL_X + WHEEL_WIDTH / 2), -(guiTop + WHEEL_Y + WHEEL_WIDTH / 2), 0);
    }
}
