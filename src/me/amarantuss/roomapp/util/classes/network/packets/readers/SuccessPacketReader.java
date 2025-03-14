package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.enums.PacketType;

public class SuccessPacketReader extends PacketReader {
    private String description;

    public SuccessPacketReader(String json) {
        super(json, PacketType.SUCCESS);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            this.description = content.get("description").getAsString();

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {
        }
    }

    public String getDescription() {
        return description;
    }
}
