package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

import java.util.Set;

public class LockRoomPacketWriter extends PacketWriter {
    public LockRoomPacketWriter() {
        super(PacketType.LOCK_ROOM, Set.of("locked"));
    }

    public LockRoomPacketWriter setLocked(boolean locked) {
        packetBuilder.addData("locked", locked);
        return this;
    }
}