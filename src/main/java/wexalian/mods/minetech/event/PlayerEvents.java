package wexalian.mods.minetech.event;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PlayerEvents
{
    @SubscribeEvent
    public static void getItemTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        if(stack.getTagCompound() != null)
        {
            if(stack.getTagCompound().hasKey("minetech:progress", Constants.NBT.TAG_INT))
            {
                int progress = stack.getTagCompound().getInteger("minetech:progress");
                event.getToolTip().add(I18n.format("tooltip.minetech:progress", progress));
            }
        }
    }
}
