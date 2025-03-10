package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

public class LeavePacketReader extends PacketReader {
    public LeavePacketReader(String json) {
        super(json, PacketType.LEAVE);
    }

    protected void read() {
        loaded = true;
    }
}
