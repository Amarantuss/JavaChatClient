package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

import java.util.Set;

public class SetAdminPacketWriter extends PacketWriter {
    public SetAdminPacketWriter() {
        super(PacketType.SET_ADMIN, Set.of("user_id", "admin"));
    }

    public SetAdminPacketWriter setUserId(String user_id) {
        packetBuilder.addData("user_id", user_id);
        return this;
    }

    public SetAdminPacketWriter setAdmin(boolean admin) {
        packetBuilder.addData("admin", admin);
        return this;
    }
}
