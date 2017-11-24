package wexalian.mods.minetech.proxy;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;

public interface IProxy
{
    default void preInit() {}
    
    default void init() {}
    
    default void postInit() {}
    
    default IAnimationStateMachine loadAnimation(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters) {return null;}
}
