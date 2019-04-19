package com.wexalian.mods.minetech.api.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public interface INetworkNodeHost<T extends INetworkNode<T, X>, X>
{
    void addNeighbours(BiConsumer<T, X> nodeConsumer, BiPredicate<World, BlockPos> posValidator);
    
    ChunkPos getChunkPos();
    
    World getWorld();
}
