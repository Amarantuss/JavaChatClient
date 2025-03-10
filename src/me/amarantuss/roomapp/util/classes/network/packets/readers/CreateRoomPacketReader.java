package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

public class CreateRoomPacketReader extends PacketReader {
    private int room_size;

    public CreateRoomPacketReader(String json) {
        super(json, PacketType.CREATE_ROOM);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            this.room_size = content.get("room_size").getAsInt();

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {

        }
    }

    public int getRoomSize() {
        return room_size;
    }
}
