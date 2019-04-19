package com.wexalian.mods.minetech.api;

import com.wexalian.mods.minetech.api.energy.kinetic.IGearAttachable;
import com.wexalian.mods.minetech.api.energy.kinetic.IKineticNode;
import com.wexalian.mods.minetech.api.energy.kinetic.IShaftAttachable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("ConstantConditions")
public class MineTechAPI
{
    public enum Kinesis
    {
        INSTANCE;
        
        @Nonnull
        @CapabilityInject(IShaftAttachable.class)
        public static Capability<IShaftAttachable> SHAFT_CAPABILITY = null;
        
        @Nonnull
        @CapabilityInject(IGearAttachable.class)
        public static Capability<IGearAttachable> GEAR_CAPABILITY = null;
        
        private BiConsumer<IKineticNode, Boolean> addNodeConsumer = (n, c) -> {};
        private Consumer<IKineticNode> removeNodeConsumer = n -> {};
        
        public void add(IKineticNode node, boolean isRemote)
        {
            addNodeConsumer.accept(node, isRemote);
        }
        
        public void remove(IKineticNode node)
        {
            removeNodeConsumer.accept(node);
        }
        
        public void setNodeConsumers(BiConsumer<IKineticNode, Boolean> addNodeConsumer, Consumer<IKineticNode> removeNodeConsumer)
        {
            this.addNodeConsumer = addNodeConsumer;
            this.removeNodeConsumer = removeNodeConsumer;
        }
    }
    
}
