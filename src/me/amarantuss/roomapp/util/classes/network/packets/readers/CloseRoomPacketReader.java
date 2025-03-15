package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.enums.PacketType;

public class CloseRoomPacketReader extends PacketReader {
    public CloseRoomPacketReader(String json) {
        super(json, PacketType.CLOSE_ROOM);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {

        }
    }
}
