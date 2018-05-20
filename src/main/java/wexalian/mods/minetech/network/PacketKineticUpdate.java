package wexalian.mods.minetech.network;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import wexalian.mods.minetech.MineTech;
import wexalian.mods.minetech.kinesis.ClientKineticManager;
import wexalian.mods.minetech.kinesis.IKineticNode;

import java.util.Collection;
import java.util.UUID;

public class PacketKineticUpdate implements IMessage
{
    private final Object2FloatMap<UUID> updates = new Object2FloatOpenHashMap<>();
    private Collection<IKineticNode> nodes;
    
    public PacketKineticUpdate() {}
    
    public PacketKineticUpdate(Collection<IKineticNode> nodes)
    {
        this.nodes = nodes;
    }
    
    @Override
    public void toBytes(ByteBuf buf)
    {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeInt(nodes.size());
        for (IKineticNode node : nodes)
        {
            buffer.writeUniqueId(node.getNodeID());
            buffer.writeFloat(node.getVelocity());
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        PacketBuffer buffer = new PacketBuffer(buf);
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) updates.put(buffer.readUniqueId(), buffer.readFloat());
    }
    
    public static class Handler implements IMessageHandler<PacketKineticUpdate, IMessage>
    {
        @Override
        public IMessage onMessage(PacketKineticUpdate message, MessageContext ctx)
        {
            MineTech.proxy.schedule(Side.CLIENT, () -> ClientKineticManager.INSTANCE.clientUpdates.putAll(message.updates));
            return null;
        }
    }
}
