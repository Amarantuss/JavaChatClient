package me.amarantuss.roomapp.util.classes.network.packets.readers;

import me.amarantuss.roomapp.util.enums.PacketType;

public class LeavePacketReader extends PacketReader {
    public LeavePacketReader(String json) {
        super(json, PacketType.LEAVE);
    }

    protected void read() {
        loaded = true;
    }
}
