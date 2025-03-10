package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

public class RoomBroadcastPacketReader extends PacketReader {
    private String message;

    public RoomBroadcastPacketReader(String json) {
        super(json, PacketType.ROOM_BROADCAST);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            this.message = content.get("message").getAsString();

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {
        }
    }

    public String getMessage() {
        return message;
    }
}
