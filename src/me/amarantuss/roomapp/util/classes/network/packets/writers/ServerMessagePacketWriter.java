package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Set;

public class ServerMessagePacketWriter extends PacketWriter {
    public ServerMessagePacketWriter() {
        super(PacketType.SERVER_MESSAGE, Set.of("username", "message", "hour", "minute", "second"));
    }

    public ServerMessagePacketWriter setUsername(String string) {
        packetBuilder.addData("username", string);
        return this;
    }

    public ServerMessagePacketWriter setMessage(String message) {
        packetBuilder.addData("message", message);
        return this;
    }

    public ServerMessagePacketWriter setHour(int hour) {
        packetBuilder.addData("hour", hour);
        return this;
    }

    public ServerMessagePacketWriter setMinute(int minute) {
        packetBuilder.addData("minute", minute);
        return this;
    }

    public ServerMessagePacketWriter setSecond(int second) {
        packetBuilder.addData("second", second);
        return this;
    }
}
