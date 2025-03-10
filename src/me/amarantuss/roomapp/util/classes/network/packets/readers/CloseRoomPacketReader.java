package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

public class CloseRoomPacketReader extends PacketReader {
    private boolean force_close;

    public CloseRoomPacketReader(String json) {
        super(json, PacketType.CLOSE_ROOM);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            this.force_close = content.get("force_close").getAsBoolean();

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {

        }
    }

    public boolean getForceClose() {
        return force_close;
    }
}
