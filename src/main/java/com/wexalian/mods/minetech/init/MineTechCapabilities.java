package com.wexalian.mods.minetech.init;

import com.wexalian.mods.minetech.api.energy.kinetic.IGearAttachable;
import com.wexalian.mods.minetech.api.energy.kinetic.IShaftAttachable;
import net.minecraft.nbt.INBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class MineTechCapabilities
{
    public static void register()
    {
        registerSimpleCapability(IShaftAttachable.class);
        registerSimpleCapability(IGearAttachable.class);
    }
    
    private static <T> void registerSimpleCapability(Class<T> clazz)
    {
        CapabilityManager.INSTANCE.register(clazz, new Capability.IStorage<T>()
        {
            @Nullable
            @Override
            public INBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side)
            {
                return null;
            }
            
            @Override
            public void readNBT(Capability<T> capability, T instance, EnumFacing side, INBTBase nbt)
            {
            
            }
        }, () -> null);
    }
}
