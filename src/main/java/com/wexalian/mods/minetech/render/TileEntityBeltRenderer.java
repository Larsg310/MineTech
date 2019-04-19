package com.wexalian.mods.minetech.render;

import com.wexalian.mods.minetech.tileentity.TileEntityBelt;
import com.wexalian.mods.minetech.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class TileEntityBeltRenderer extends TileEntityRenderer<TileEntityBelt>
{
    @Override
    public void render(TileEntityBelt tile, double xOff, double yOff, double zOff, float partialTicks, int destroyStage)
    {
        GlStateManager.pushMatrix();
        
        GlStateManager.translated(xOff, yOff, zOff);
        
        renderBelt(tile);
        renderItems(tile, partialTicks);
        
        GlStateManager.popMatrix();
    }
    
    @SuppressWarnings("Duplicates")
    private void renderBelt(TileEntityBelt tile)
    {
        GlStateManager.pushMatrix();
        
        World world = Objects.requireNonNull(tile.getWorld());
        BlockPos pos = tile.getPos();
        IBlockState state = tile.getBlockState();
        
        RenderUtil.renderBlockStateInWorld(state, world, pos, 1F);
        
        GlStateManager.popMatrix();
    }
    
    private void renderItems(TileEntityBelt tile, float partialTicks)
    {
        // TileEntityBelt.BeltContent[] contents = tile.getLeftLane();
        //
        // for (int i = 0; i < contents.length; i++)
        // {
        //     if(contents[i] != null){
        //         renderItemStack(tile, contents[i].getStack(), i, partialTicks);
        //     }
        // }
    }
    
    // private void renderItemStack(TileEntityBelt tile, ItemStack stack, int index, float partialTicks)
    // {
    //     GlStateManager.pushMatrix();
    //     RenderHelper.enableStandardItemLighting();
    //     GlStateManager.enableLighting();
    //
    //     float[] coords = this.getCoords(tile, index, partialTicks);
    //
    //     GlStateManager.translated(coords[0], 0.3125, coords[1]);
    //
    //     float scale = stack.getItem() instanceof ItemBlock ? 0.25F : 0.4F;
    //     GlStateManager.scalef(scale, scale, scale);
    //
    //     ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
    //     itemRenderer.renderItem(stack, itemRenderer.getModelWithOverrides(stack));
    //
    //     GlStateManager.popMatrix();
    // }
    //
    // private float[] getCoords(TileEntityBelt tile, int index, float partialTicks)
    // {
    //     EnumFacing facing = tile.getBlockState().get(BlockBelt.FACING);
    //     BeltConnection connection = tile.getBlockState().get(BlockBelt.BELT_CONNECTION);
    //     World world = Objects.requireNonNull(tile.getWorld());
    //     float movePerTick = (float) tile.getLaneSpeed() / 20F;
    //
    //     // x^4 + y^4 = 0.25^4
    //
    //     if (facing == EnumFacing.NORTH)
    //     {
    //         if (connection == BeltConnection.STRAIGHT)
    //         {
    //             float x = 0.25F;
    //             // float y = root(4, 0.)
    //             // return (world.getGameTime() % (1F / movePerTick)) * movePerTick
    //             // float y = (1F / (float) tile.getLaneSpeed() * index) + ((world.getGameTime() % (1F / movePerTick)) * movePerTick) + movePerTick * partialTicks;
    //             float y = index/4F;
    //             return new float[]{x, y};
    //
    //         }
    //     }
    //
    //     return new float[]{0, 0};
    // }
    
    private float root(float base, float value)
    {
        return (float) Math.pow(Math.E, Math.log(value) / base);
    }
}
