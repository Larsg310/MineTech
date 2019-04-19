package com.wexalian.mods.minetech.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Pair;

public class RayTraceUtil
{
    public static Pair<Vec3d, Vec3d> getRayTraceVectors(EntityPlayer player)
    {
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;
        Vec3d start = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        float f1 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f3 = -MathHelper.cos(-pitch * 0.017453292F);
        float f4 = MathHelper.sin(-pitch * 0.017453292F);
        float f5 = f2 * f3;
        float f6 = f1 * f3;
        double reachDistance = player.getAttribute(EntityPlayer.REACH_DISTANCE).getValue();
        
        Vec3d end = start.add(f5 * reachDistance, f4 * reachDistance, f6 * reachDistance);
        return Pair.of(start, end);
    }
}
