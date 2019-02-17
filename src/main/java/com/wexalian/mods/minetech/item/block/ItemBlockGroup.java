package com.wexalian.mods.minetech.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;

import java.util.Objects;

public class ItemBlockGroup extends ItemBlock
{
    public ItemBlockGroup(Block block, ItemGroup group)
    {
        super(block, new Properties().group(group));
        setRegistryName(Objects.requireNonNull(block.getRegistryName()));
    }
}
