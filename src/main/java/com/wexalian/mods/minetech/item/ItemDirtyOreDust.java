package com.wexalian.mods.minetech.item;

import com.wexalian.mods.minetech.MineTechMod;
import com.wexalian.mods.minetech.lib.ItemNames;
import com.wexalian.mods.minetech.lib.Materials;
import net.minecraft.item.Item;

public class ItemDirtyOreDust extends Item
{
    public ItemDirtyOreDust(Materials material)
    {
        super(new Properties().group(MineTechMod.ITEM_GROUP));
        setRegistryName(ItemNames.DIRTY_ORE_DUST + "_" + material.getName());
    }
}
