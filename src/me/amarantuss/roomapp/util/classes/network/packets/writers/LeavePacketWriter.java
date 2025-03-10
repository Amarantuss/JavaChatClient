package me.amarantuss.roomapp.util.classes.network.packets.writers;

import me.amarantuss.roomapp.util.classes.network.packets.PacketType;

import java.util.Set;

public class LeavePacketWriter extends PacketWriter {
    public LeavePacketWriter() {
        super(PacketType.LEAVE, null);
    }
}
