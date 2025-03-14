package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.server.RoomRole;
import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StatusPacketWriter extends PacketWriter {
    public StatusPacketWriter() {
        super(PacketType.STATUS, Set.of("users", "roles", "room_id", "locked", "room_size"));
    }

    public StatusPacketWriter setUsers(Map<UUID, String> users) {
        packetBuilder.addData("users", users);
        return this;
    }

    public StatusPacketWriter setRoles(Map<UUID, RoomRole> roles) {
        packetBuilder.addData("roles", roles);
        return this;
    }

    public StatusPacketWriter setRoomId(String room_id) {
        packetBuilder.addData("room_id", room_id);
        return this;
    }

    public StatusPacketWriter setLocked(boolean locked) {
        packetBuilder.addData("locked", locked);
        return this;
    }

    public StatusPacketWriter setRoomSize(int roomSize) {
        packetBuilder.addData("room_size", roomSize);
        return this;
    }
}
