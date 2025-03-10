package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.classes.network.packets.Packet;
import me.amarantuss.roomapp.util.classes.network.packets.PacketBuilder;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

import java.util.HashMap;
import java.util.Set;

public abstract class PacketWriter {
    protected final PacketBuilder packetBuilder;

    protected Set<String> keys;

    public PacketWriter(PacketType packetType, Set<String> keys) {
        this.packetBuilder = new PacketBuilder(packetType);
        this.keys = keys;
    }

    public Packet build() {
        HashMap<String, Object> data = packetBuilder.getData();
        if(keys != null && !data.keySet().containsAll(keys)) return null;
        return packetBuilder.build();
    }
}
