package me.amarantuss.roomapp.util.classes.network.packets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;

public class PacketBuilder {
    private final PacketType packetType;

    private final HashMap<String, Object> data = new HashMap<>();

    public PacketBuilder(PacketType packetType) {
        this.packetType = packetType;
    }

    public void addData(String key, Object value) {
        this.data.put(key, value);
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public Packet build() {
        return () -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", packetType.toString());

            JsonObject data = new JsonObject();
            for(String key : this.data.keySet()) {
                data.add(key, new Gson().toJsonTree(this.data.get(key)));
            }

            jsonObject.add("content", data);

            return jsonObject.toString();
        };
    }
}
