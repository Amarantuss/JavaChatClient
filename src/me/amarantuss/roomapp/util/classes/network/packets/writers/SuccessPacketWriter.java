package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

import java.util.Set;

public class SuccessPacketWriter extends PacketWriter {
    public SuccessPacketWriter() {
        super(PacketType.SUCCESS, Set.of("description"));
    }

    public SuccessPacketWriter setDescription(String description) {
        packetBuilder.addData("description", description);
        return this;
    }
}
