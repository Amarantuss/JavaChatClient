package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.enums.PacketType;

public class StatusRequestPacketReader extends PacketReader {
    public StatusRequestPacketReader(String json) {
        super(json, PacketType.STATUS_REQUEST);
    }

    protected void read() {
        loaded = true;
    }
}
