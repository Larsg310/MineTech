package wexalian.mods.minetech.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collection;

public final class RayTraceUtil
{
    public static AdvancedRayTraceResult collisionRayTrace(World world, BlockPos pos, Vec3d start, Vec3d end, Collection<AxisAlignedBB> boxes)
    {
        
        double minDistance = Double.POSITIVE_INFINITY;
        AdvancedRayTraceResult hit = null;
        
        int i = -1;
        for (AxisAlignedBB aabb : boxes)
        {
            AdvancedRayTraceResult result = aabb == null ? null : collisionRayTrace(pos, start, end, aabb, i, null);
            if (result != null)
            {
                double d = result.squareDistanceTo(start);
                if (d < minDistance)
                {
                    minDistance = d;
                    hit = result;
                }
            }
            i++;
        }
        return hit;
    }
    
    public static AdvancedRayTraceResult collisionRayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB bounds, int subHit, Object hitInfo)
    {
        
        RayTraceResult result = bounds.offset(pos).calculateIntercept(start, end);
        if (result == null) return null;
        result = new RayTraceResult(RayTraceResult.Type.BLOCK, result.hitVec, result.sideHit, pos);
        result.subHit = subHit;
        result.hitInfo = hitInfo;
        return new AdvancedRayTraceResult(result, bounds);
    }
    
    public static class AdvancedRayTraceResult
    {
        public final AxisAlignedBB bounds;
        public final RayTraceResult hit;
        
        public AdvancedRayTraceResult(RayTraceResult hit, AxisAlignedBB bounds)
        {
            this.hit = hit;
            this.bounds = bounds;
        }
        
        public double squareDistanceTo(Vec3d vec)
        {
            return hit.hitVec.squareDistanceTo(vec);
        }
    }
}
