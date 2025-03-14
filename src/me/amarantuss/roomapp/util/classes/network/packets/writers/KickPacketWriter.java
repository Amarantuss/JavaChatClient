package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

import java.util.Set;

public class KickPacketWriter extends PacketWriter {
    public KickPacketWriter() {
        super(PacketType.KICK, Set.of("user_id"));
    }

    public KickPacketWriter setUserId(String user_id) {
        packetBuilder.addData("user_id", user_id);
        return this;
    }
}
