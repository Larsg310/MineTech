package wexalian.mods.minetech.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import wexalian.mods.minetech.init.ModItems;
import wexalian.mods.minetech.lib.ItemNames;
import wexalian.mods.minetech.metal.Metals;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemOreDust extends Item
{
    public static final List<Metals> TYPES = ItemDirtyOreDust.TYPES;
    
    public ItemOreDust()
    {
        setCreativeTab(CreativeTabs.MATERIALS);
        setRegistryName(ItemNames.ORE_DUST);
        setUnlocalizedName(ItemNames.ORE_DUST);
        setHasSubtypes(true);
    }
    
    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items)
    {
        if (isInCreativeTab(tab))
        {
            TYPES.forEach(m -> items.add(new ItemStack(this, 1, TYPES.indexOf(m))));
        }
    }
    
    @Override
    @Nonnull
    public String getUnlocalizedName(@Nonnull ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + TYPES.get(stack.getMetadata()).getName();
    }
    
    @Nonnull
    public static ItemStack getFromMetal(Metals metal)
    {
        return getFromMetal(metal, 1);
    }
    
    @Nonnull
    public static ItemStack getFromMetal(Metals metal, int amount)
    {
        return new ItemStack(ModItems.ORE_DUST, amount, TYPES.indexOf(metal));
    }
}
