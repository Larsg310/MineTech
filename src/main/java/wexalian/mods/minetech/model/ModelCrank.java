package wexalian.mods.minetech.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCrank extends ModelBase
{
    public static final float RAD = 0.017453292F;
//    public ModelRenderer model;
    public ModelRenderer post;
    public ModelRenderer handle;
    
    @SuppressWarnings("MagicNumber")
    public ModelCrank()
    {
        textureWidth = textureHeight = 16;
//
//        model = new ModelRenderer(this, 0, 0);
//        model.addBox(-8, 0, -8, 16, 16, 16);
//        model.setRotationPoint(8, 0, 8);
        
        post = new ModelRenderer(this,0,0);
        post.addBox(-1,0,-1,2,12,2);
        post.setRotationPoint(8,0,8);
    
        handle = new ModelRenderer(this,0,0);
        handle.addBox(-1,10,1,2,2,6);
        handle.setRotationPoint(8,0,8);
    }
    
    public void render(float scale, float rotationDegrees)
    {
        post.rotateAngleY = rotationDegrees * RAD;
        post.render(scale);
        
        handle.rotateAngleY = rotationDegrees * RAD;
        handle.render(scale);
    }
}
