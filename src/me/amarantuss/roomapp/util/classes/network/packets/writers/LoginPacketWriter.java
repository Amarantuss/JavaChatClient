package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.classes.network.packets.Packet;
import me.amarantuss.roomapp.util.classes.network.packets.PacketBuilder;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

import java.util.HashMap;
import java.util.Set;

public class LoginPacketWriter extends PacketWriter {
    public LoginPacketWriter() {
        super(PacketType.LOGIN, Set.of("username"));
    }

    public LoginPacketWriter setUsername(String string) {
        packetBuilder.addData("username", string);
        return this;
    }
}
