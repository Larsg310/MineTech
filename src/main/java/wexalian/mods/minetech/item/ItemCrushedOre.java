package wexalian.mods.minetech.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import wexalian.mods.minetech.lib.ItemNames;
import wexalian.mods.minetech.metal.Metals;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ItemCrushedOre extends Item
{
    public static final List<Metals> TYPES = Arrays.asList(Metals.IRON, Metals.GOLD);

    public ItemCrushedOre()
    {
        setCreativeTab(CreativeTabs.MATERIALS);
        setRegistryName(ItemNames.CRUSHED_ORE);
        setUnlocalizedName(ItemNames.CRUSHED_ORE);
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items)
    {
        if (isInCreativeTab(tab))
        {
            for (Metals metal : TYPES)
            {
                items.add(new ItemStack(this, 1, TYPES.indexOf(metal)));
            }
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(@Nonnull ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + TYPES.get(stack.getMetadata()).getName();
    }
}
