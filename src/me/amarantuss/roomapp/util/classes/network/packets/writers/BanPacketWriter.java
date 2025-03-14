package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Set;

public class BanPacketWriter extends PacketWriter {
    public BanPacketWriter() {
        super(PacketType.BAN, Set.of("user_id", "banned"));
    }

    public BanPacketWriter setUserId(String user_id) {
        packetBuilder.addData("user_id", user_id);
        return this;
    }

    public BanPacketWriter setBanned(boolean banned) {
        packetBuilder.addData("banned", banned);
        return this;
    }
}
