package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.enums.PacketType;

public class LoginPacketReader extends PacketReader {
    private String username;

    public LoginPacketReader(String json) {
        super(json, PacketType.LOGIN);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            this.username = content.get("username").getAsString();

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {
        }
    }

    public String getUsername() {
        return username;
    }
}
