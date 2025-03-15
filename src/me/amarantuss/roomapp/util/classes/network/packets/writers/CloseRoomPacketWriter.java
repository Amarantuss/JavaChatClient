package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Set;

public class CloseRoomPacketWriter extends PacketWriter {
    public CloseRoomPacketWriter() {
        super(PacketType.CLOSE_ROOM, null);
    }
}
