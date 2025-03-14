package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Set;

public class JoinPacketWriter extends PacketWriter {
    public JoinPacketWriter() {
        super(PacketType.JOIN, Set.of("room_id"));
    }

    public JoinPacketWriter setRoomId(String room_id) {
        packetBuilder.addData("room_id", room_id);
        return this;
    }
}
