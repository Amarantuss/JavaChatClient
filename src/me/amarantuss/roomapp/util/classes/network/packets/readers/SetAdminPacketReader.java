package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.UUID;

public class SetAdminPacketReader extends PacketReader {
    private UUID user_id;
    private boolean admin;

    public SetAdminPacketReader(String json) {
        super(json, PacketType.SET_ADMIN);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            this.admin = content.get("admin").getAsBoolean();
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

    public boolean getAdmin() {
        return admin;
    }
}
