package me.amarantuss.roomapp.util.classes.network.packets.readers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.classes.network.packets.PacketFactory;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

public abstract class PacketReader {
    protected String json;

    protected boolean loaded = false;

    public PacketReader(String json, PacketType packetType) {
        if(PacketFactory.getPacketType(json) != packetType) return;
        this.json = json;
        read();
    }

    protected JsonObject getJsonContent() {
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            return jsonObject.get("content").getAsJsonObject();
        } catch (JsonSyntaxException | NullPointerException e) {
        }
        return null;
    }

    protected abstract void read();

    public boolean valid() {
        return loaded;
    }
}
