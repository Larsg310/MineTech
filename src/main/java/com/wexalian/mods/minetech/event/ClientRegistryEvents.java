package com.wexalian.mods.minetech.event;

import com.wexalian.mods.minetech.MineTechMod;
import com.wexalian.mods.minetech.api.item.MineTechItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientRegistryEvents
{
    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item event)
    {
        event.getItemColors().register(ClientRegistryEvents::getWaterColor, MineTechItems.PAN);
    }
    
    private static int getWaterColor(ItemStack itemStack, int tintIndex)
    {
        return MineTechMod.PROXY.getWaterColor();
    }
}
