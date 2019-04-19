package com.wexalian.mods.minetech.event;

import com.wexalian.mods.minetech.multiblock.MultiblockManager;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEvents
{
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (!event.getWorld().isRemote)
        {
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            
            if (MultiblockManager.INSTANCE.tryTrigger(state, event.getWorld(), event.getPos()))
            {
                event.setCanceled(true);
            }
        }
    }
}
