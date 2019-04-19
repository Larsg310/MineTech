package com.wexalian.mods.minetech.render;

import com.wexalian.mods.minetech.block.BlockGear;
import com.wexalian.mods.minetech.tileentity.kinetic.TileEntityGear;
import com.wexalian.mods.minetech.util.RenderUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Objects;

public class TileEntityGearRenderer extends TileEntityRenderer<TileEntityGear>
{
    @SuppressWarnings("Duplicates")
    @Override
    public void render(TileEntityGear tile, double x, double y, double z, float partialTicks, int destroyStage)
    {
        World world = Objects.requireNonNull(tile.getWorld());
        BlockPos pos = tile.getPos();
        IBlockState state = world.getBlockState(pos);
        
        for (EnumFacing face : EnumFacing.values())
        {
            if (tile.isEnabled(face.ordinal()))
            {
                GlStateManager.pushMatrix();
                
                GlStateManager.translated(x + 0.5D, y + 0.5D, z + 0.5D);
                
                Vec3i axis = face.getDirectionVec();
                
                float nodeAngle = tile.getNode(face.ordinal()).getAngle(partialTicks);
                float angle = (face.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE ? 22.5f : 0) + nodeAngle + 11.25F;
                
                GlStateManager.rotatef(angle, Math.abs(axis.getX()), Math.abs(axis.getY()), Math.abs(axis.getZ()));
                
                GlStateManager.translated(-0.5D, -0.5D, -0.5D);
                
                RenderUtil.renderBlockStateInWorld(state.with(BlockGear.FACING, face), world, pos, 1F);
                
                GlStateManager.popMatrix();
            }
        }
    }
}
