package wexalian.mods.minetech.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemBase extends Item
{
    @SuppressWarnings("NullableProblems")
    @Override
    @Nullable
    protected RayTraceResult rayTrace(World world, EntityPlayer player, boolean useLiquids)
    {
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;
        
        double posX = player.posX;
        double posY = player.posY + (double) player.getEyeHeight();
        double posZ = player.posZ;
        Vec3d startVec = new Vec3d(posX, posY, posZ);
    
        float f = 0.017453292F;
        float xLook = MathHelper.sin(-yaw * f - (float) Math.PI);
        float yLook = -MathHelper.cos(-pitch * f);
        float zLook = MathHelper.cos(-yaw * f - (float) Math.PI);
        
        float xDir = xLook * yLook;
        float yDir = MathHelper.sin(-pitch * f);
        float zDir = zLook * yLook;
        
        double reachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d endVec = startVec.addVector((double) xDir * reachDistance, (double) yDir * reachDistance, (double) zDir * reachDistance);
        return world.rayTraceBlocks(startVec, endVec, useLiquids, !useLiquids, false);
    }
}
