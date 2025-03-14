package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Set;

public class ClientMessagePacketWriter extends PacketWriter {
    public ClientMessagePacketWriter() {
        super(PacketType.CLIENT_MESSAGE, Set.of("message"));
    }

    public ClientMessagePacketWriter setMessage(String message) {
        packetBuilder.addData("message", message);
        return this;
    }
}
