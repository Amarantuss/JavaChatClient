package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Set;

public class CreateRoomPacketWriter extends PacketWriter {
    public CreateRoomPacketWriter() {
        super(PacketType.CREATE_ROOM, Set.of("room_size"));
    }

    public CreateRoomPacketWriter setRoomSize(int room_size) {
        packetBuilder.addData("room_size", room_size);
        return this;
    }
}
