package com.wexalian.mods.minetech.network;

import com.wexalian.mods.minetech.energy.kinetic.KineticManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageKineticUpdate
{
    private final Object2ObjectMap<UUID, Float> updates;
    
    public MessageKineticUpdate(Object2ObjectMap<UUID, Float> updates)
    {
        this.updates = updates;
    }
    
    public static void encode(MessageKineticUpdate message, PacketBuffer buffer)
    {
        buffer.writeInt(message.updates.size());
        message.updates.forEach((uuid, velocity) -> {
            buffer.writeUniqueId(uuid);
            buffer.writeFloat(velocity);
        });
    }
    
    public static MessageKineticUpdate decode(PacketBuffer buffer)
    {
        Object2ObjectMap<UUID, Float> updates = new Object2ObjectOpenHashMap<>();
        
        int size = buffer.readInt();
        for (int i = 0; i < size; i++)
        {
            updates.put(buffer.readUniqueId(), buffer.readFloat());
        }
        return new MessageKineticUpdate(updates);
    }
    
    public static void handle(MessageKineticUpdate message, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> KineticManager.INSTANCE.clientUpdates.putAll(message.updates));
        ctx.get().setPacketHandled(true);
    }
}
