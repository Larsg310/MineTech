package com.wexalian.mods.minetech.network;

import com.wexalian.mods.minetech.lib.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler
{
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Reference.MOD_ID, "networking"))//
                                                                        .clientAcceptedVersions(PROTOCOL_VERSION::equals)//
                                                                        .serverAcceptedVersions(PROTOCOL_VERSION::equals)//
                                                                        .networkProtocolVersion(() -> PROTOCOL_VERSION)//
                                                                        .simpleChannel();
    
    public static void register()
    {
        int index = 0;
        CHANNEL.messageBuilder(MessageKineticUpdate.class, index++)//
               .encoder(MessageKineticUpdate::encode)//
               .decoder(MessageKineticUpdate::decode)//
               .consumer(MessageKineticUpdate::handle)//
               .add();//
        
        
    }
}
