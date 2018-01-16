package wexalian.mods.minetech.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import wexalian.mods.minetech.init.ModItems;
import wexalian.mods.minetech.lib.ItemNames;
import wexalian.mods.minetech.metal.Metals;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ItemDirtyOreDust extends Item
{
    public static final List<Metals> TYPES = Arrays.asList(Metals.IRON, Metals.GOLD);
    
    public ItemDirtyOreDust()
    {
        setCreativeTab(CreativeTabs.MATERIALS);
        setRegistryName(ItemNames.DIRTY_ORE_DUST);
        setUnlocalizedName(ItemNames.DIRTY_ORE_DUST);
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
    
    @Nonnull
    public static ItemStack getFromMetal(Metals metal)
    {
        return getFromMetal(metal, 1);
    }
    
    @Nonnull
    public static ItemStack getFromMetal(Metals metal, int amount)
    {
        return new ItemStack(ModItems.DIRTY_ORE_DUST, amount, TYPES.indexOf(metal));
    }
    
    @Nonnull
    public static Metals getMetalFromStack(ItemStack stack)
    {
        if(stack.getItem() == ModItems.DIRTY_ORE_DUST)
        {
            return TYPES.get(stack.getItemDamage());
        }
        return TYPES.get(0);
    }
}
