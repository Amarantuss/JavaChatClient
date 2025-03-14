package me.amarantuss.roomapp.util.classes.network.packets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import me.amarantuss.roomapp.util.enums.PacketType;

public class PacketFactory {
    public static PacketType getPacketType(String json) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonElement jsonElement = jsonObject.get("type");
            if(jsonElement == null) throw new JsonSyntaxException("Invalid json message");
            return PacketType.valueOf(jsonElement.getAsString());
        } catch (JsonSyntaxException | IllegalArgumentException e) {
        }
        return null;
    }


}
