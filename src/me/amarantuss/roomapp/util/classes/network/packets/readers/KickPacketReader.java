package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

import java.util.UUID;

public class KickPacketReader extends PacketReader {
    private UUID user_id;

    public KickPacketReader(String json) {
        super(json, PacketType.KICK);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            try {
                this.user_id = UUID.fromString(content.get("user_id").getAsString());
            } catch (IllegalArgumentException e) {
                return;
            }

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {
        }
    }

    public UUID getUserId() {
        return user_id;
    }
}
