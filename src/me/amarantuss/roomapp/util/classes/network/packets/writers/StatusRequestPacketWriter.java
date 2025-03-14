package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.server.RoomRole;
import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StatusRequestPacketWriter extends PacketWriter {
    public StatusRequestPacketWriter() {
        super(PacketType.STATUS_REQUEST, null);
    }
}