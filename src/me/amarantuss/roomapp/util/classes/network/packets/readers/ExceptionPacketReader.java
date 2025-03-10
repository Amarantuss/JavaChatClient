package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.classes.network.packets.PacketFactory;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

public class ExceptionPacketReader extends PacketReader {
    private int code;
    private String description;

    public ExceptionPacketReader(String json) {
        super(json, PacketType.EXCEPTION);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            this.code = content.get("code").getAsInt();
            this.description = content.get("description").getAsString();

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {
        }
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
