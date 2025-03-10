package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

public class LockRoomPacketReader extends PacketReader {
    private boolean locked;

    public LockRoomPacketReader(String json) {
        super(json, PacketType.LOCK_ROOM);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            this.locked = content.get("locked").getAsBoolean();

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {
        }
    }

    public boolean getLocked() {
        return locked;
    }
}
