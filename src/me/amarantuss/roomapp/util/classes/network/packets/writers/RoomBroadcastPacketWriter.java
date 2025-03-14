package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Set;

public class RoomBroadcastPacketWriter extends PacketWriter {
    public RoomBroadcastPacketWriter() {
        super(PacketType.ROOM_BROADCAST, Set.of("message"));
    }

    public RoomBroadcastPacketWriter setMessage(String message) {
        packetBuilder.addData("message", message);
        return this;
    }
}
