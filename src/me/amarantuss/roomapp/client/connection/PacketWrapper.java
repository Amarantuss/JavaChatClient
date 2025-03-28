package me.amarantuss.roomapp.client.connection;

import me.amarantuss.roomapp.util.classes.network.packets.readers.PacketReader;
import me.amarantuss.roomapp.util.enums.PacketType;

public class PacketWrapper {
    private final PacketType packetType;
    private final PacketReader packetReader;

    public PacketWrapper(PacketType packetType, PacketReader packetReader) {
        this.packetType = packetType;
        this.packetReader = packetReader;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public PacketReader getPacketReader() {
        return packetReader;
    }
}
