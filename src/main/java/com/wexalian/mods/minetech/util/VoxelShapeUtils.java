package com.wexalian.mods.minetech.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import javax.annotation.Nonnull;
import java.util.List;

public final class VoxelShapeUtils
{
    @Nonnull
    public static VoxelShape translate(@Nonnull VoxelShape shape, @Nonnull EnumFacing.Axis axis, int pixels)
    {
        VoxelShape translatedShape = shape;
        List<AxisAlignedBB> oldBoxes = shape.toBoundingBoxList();
        
        for (AxisAlignedBB box : oldBoxes)
        {
            float offset = pixels / 16F;
            box = box.offset(axis.getCoordinate(offset, 0, 0), axis.getCoordinate(0, offset, 0), axis.getCoordinate(0, 0, offset));
            translatedShape = (translatedShape == shape ? VoxelShapes.create(box) : VoxelShapes.or(translatedShape, VoxelShapes.create(box)));
        }
        return translatedShape;
    }
    
    @Nonnull
    public static VoxelShape rotate(@Nonnull VoxelShape shape, @Nonnull Rotation rotation, @Nonnull EnumFacing.Axis axis)
    {
        VoxelShape rotatedShape = shape;
        List<AxisAlignedBB> oldBoxes = shape.toBoundingBoxList();
        
        for (AxisAlignedBB box : oldBoxes)
        {
            box = box.offset(-0.5, -0.5, -0.5);
            box = rotateAroundOrigin(box, rotation, axis);
            box = box.offset(0.5, 0.5, 0.5);
            rotatedShape = (rotatedShape == shape ? VoxelShapes.create(box) : VoxelShapes.or(rotatedShape, VoxelShapes.create(box)));
        }
        return rotatedShape;
    }
    
    @Nonnull
    private static AxisAlignedBB rotateAroundOrigin(@Nonnull AxisAlignedBB box, @Nonnull Rotation rotation, @Nonnull EnumFacing.Axis axis)
    {
        // x: rotate (y,z)
        // y: rotate (x,z)
        // z: rotate (x,y)
        //
        // clockwise_90:        rotate (a,b) to (b,−a)
        // clockwise_180:       rotate (a,b) to (-a,-b)
        // counterclockwise_90: rotate (a,b) to (-b,a)
        
        switch (axis)
        {
            case X: // y/z axis swap
                switch (rotation)
                {
                    case NONE:
                        return box;
                    case CLOCKWISE_90:
                        return new AxisAlignedBB(box.minX, box.minZ, -box.minY, box.maxX, box.maxZ, -box.maxY);
                    case CLOCKWISE_180:
                        return new AxisAlignedBB(box.minX, -box.minY, -box.minZ, box.maxX, -box.maxY, -box.maxZ);
                    case COUNTERCLOCKWISE_90:
                        return new AxisAlignedBB(box.minX, -box.minZ, box.minY, box.maxX, -box.maxZ, box.maxY);
                }
            case Y: // x/z axis swap
                switch (rotation)
                {
                    case NONE:
                        return box;
                    case CLOCKWISE_90:
                        return new AxisAlignedBB(box.minZ, box.minY, -box.minX, box.maxZ, box.maxY, -box.maxX);
                    case CLOCKWISE_180:
                        return new AxisAlignedBB(-box.minX, box.minY, -box.minZ, -box.maxX, box.maxY, -box.maxZ);
                    case COUNTERCLOCKWISE_90:
                        return new AxisAlignedBB(-box.minZ, box.minY, box.minX, -box.maxZ, box.maxY, box.maxX);
                }
            case Z:  // x/y axis swap
                switch (rotation)
                {
                    case NONE:
                        return box;
                    case CLOCKWISE_90:
                        return new AxisAlignedBB(box.minY, -box.minX, box.minZ, box.maxY, -box.maxX, box.maxZ);
                    case CLOCKWISE_180:
                        return new AxisAlignedBB(-box.minX, -box.minY, box.minZ, -box.maxX, -box.maxY, box.maxZ);
                    case COUNTERCLOCKWISE_90:
                        return new AxisAlignedBB(-box.minY, box.minX, box.minZ, -box.maxY, box.maxX, box.maxZ);
                }
        }
        return box;
    }
}
