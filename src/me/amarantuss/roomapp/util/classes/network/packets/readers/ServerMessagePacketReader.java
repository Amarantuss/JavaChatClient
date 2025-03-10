package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

public class ServerMessagePacketReader extends PacketReader {
    private String username;
    private String message;
    private int hour;
    private int minute;
    private int second;

    public ServerMessagePacketReader(String json) {
        super(json, PacketType.SERVER_MESSAGE);
    }

    protected void read() {
        try {
            JsonObject content = getJsonContent();

            this.username = content.get("username").getAsString();
            this.message = content.get("message").getAsString();
            this.hour = content.get("hour").getAsInt();
            this.minute = content.get("minute").getAsInt();
            this.second = content.get("second").getAsInt();

            loaded = true;
        } catch (JsonSyntaxException | NullPointerException e) {
        }
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
}
