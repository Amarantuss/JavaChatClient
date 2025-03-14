package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import me.amarantuss.roomapp.server.RoomRole;
import me.amarantuss.roomapp.util.classes.network.ServerUser;
import me.amarantuss.roomapp.util.enums.PacketType;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StatusPacketReader extends PacketReader {
    private UUID room_id;
    private Map<UUID, String> users;
    private Map<UUID, RoomRole> roles;
    private boolean locked;
    private int room_size;

    public StatusPacketReader(String json) {
        super(json, PacketType.STATUS);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            Gson gson = new Gson();

            TypeToken<Map<UUID, String>> usersToken = new TypeToken<Map<UUID,String>>(){};
            this.users = gson.fromJson(content.get("users"), usersToken);

            TypeToken<Map<UUID, RoomRole>> rolesToken = new TypeToken<Map<UUID,RoomRole>>(){};
            this.roles = gson.fromJson(content.get("roles"), rolesToken);

            this.locked = content.get("locked").getAsBoolean();
            this.room_size = content.get("room_size").getAsInt();

            try {
                this.room_id = UUID.fromString(content.get("room_id").getAsString());
            } catch (IllegalArgumentException e) {
                return;
            }

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {
        }
    }

    public Map<UUID, String> getUsers() {
        return users;
    }

    public Map<UUID, RoomRole> getRoles() {
        return roles;
    }

    public UUID getRoomId() {
        return room_id;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getRoomSize() {
        return room_size;
    }
}
