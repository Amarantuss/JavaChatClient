package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Set;

public class ExceptionPacketWriter extends PacketWriter {
    public ExceptionPacketWriter() {
        super(PacketType.EXCEPTION, Set.of("code", "description"));
    }

    public ExceptionPacketWriter setCode(int code) {
        packetBuilder.addData("code", code);
        return this;
    }

    public ExceptionPacketWriter setDescription(String description) {
        packetBuilder.addData("description", description);
        return this;
    }
}
