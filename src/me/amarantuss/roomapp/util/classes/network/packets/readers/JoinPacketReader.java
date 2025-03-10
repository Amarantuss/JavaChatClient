package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

public class JoinPacketReader extends PacketReader {
    private String room_id;

    public JoinPacketReader(String json) {
        super(json, PacketType.JOIN);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            this.room_id = content.get("room_id").getAsString();

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {
        }
    }

    public String getRoomId() {
        return room_id;
    }
}
