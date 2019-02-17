package com.wexalian.mods.minetech.item;

import com.wexalian.mods.minetech.lib.Materials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import com.wexalian.mods.minetech.lib.ItemNames;

public class ItemDirtyOreDust extends Item
{
    public ItemDirtyOreDust(Materials material)
    {
        super(new Properties().group(ItemGroup.MATERIALS));
        setRegistryName(ItemNames.DIRTY_ORE_DUST + "_" + material.getName());
    }
}
