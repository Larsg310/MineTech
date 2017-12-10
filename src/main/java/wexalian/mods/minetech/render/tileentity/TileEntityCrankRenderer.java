package wexalian.mods.minetech.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import wexalian.mods.minetech.model.ModelCrank;
import wexalian.mods.minetech.tileentity.TileEntityCrank;

public class TileEntityCrankRenderer extends TileEntitySpecialRenderer<TileEntityCrank>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/blocks/planks_oak.png");
    
    public static final float FULL_ROTATION = 360F;
    public static final float SCALE = 0.0625F;
    
    public ModelCrank model = new ModelCrank();
    
    @Override
    public void render(TileEntityCrank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        setLightmapDisabled(true);
        bindTexture(TEXTURE);
        float angle = FULL_ROTATION / TileEntityCrank.MAX_CRANK_TIME * te.getCrankTime();
        model.render(SCALE, angle);
        setLightmapDisabled(false);
        GL11.glPopMatrix();
    }
}
