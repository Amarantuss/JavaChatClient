package me.amarantuss.roomapp.client.connection;

import me.amarantuss.roomapp.util.classes.network.packets.Packet;
import me.amarantuss.roomapp.util.classes.network.packets.writers.*;

import java.util.ArrayList;
import java.util.List;

public class ClientConnectionPipe {
    private final List<PacketWrapper> messages = new ArrayList<>();

    private ClientConnection clientConnection;

    private boolean notified = false;

    public ClientConnectionPipe(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void closeConnection() {
        this.clientConnection.close();
    }

    public synchronized PacketWrapper readMessage() {
        while(this.messages.isEmpty() && !notified) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        if(notified) {
            notified = false;
            return null;
        }

        PacketWrapper packetWrapper = this.messages.getFirst();
        this.messages.removeFirst();
        return packetWrapper;
    }

    public synchronized void add(PacketWrapper packetWrapper) {
        this.messages.add(packetWrapper);
        notify();
    }

    public synchronized void notifyThread() {
        notified = true;
        notify();
    }

    public void statusRequestPacket() {
        Packet packet = new StatusRequestPacketWriter().build();
        this.clientConnection.send(packet);
    }

    public void setAdminPacket(String user_id, boolean admin) {
        Packet packet = new SetAdminPacketWriter().setUserId(user_id).setAdmin(admin).build();
        this.clientConnection.send(packet);
    }

    public void setBanPacket(String user_id, boolean banned) {
        Packet packet = new BanPacketWriter().setUserId(user_id).setBanned(banned).build();
        this.clientConnection.send(packet);
    }

    public void kickPacket(String user_id) {
        Packet packet = new KickPacketWriter().setUserId(user_id).build();
        this.clientConnection.send(packet);
    }

    public void lockRoomPacket(boolean locked) {
        Packet packet = new LockRoomPacketWriter().setLocked(locked).build();
        this.clientConnection.send(packet);
    }

    public void closeRoomPacket(boolean force_close) {
        Packet packet = new CloseRoomPacketWriter().setForceClose(force_close).build();
        this.clientConnection.send(packet);
    }

    public void leavePacket() {
        Packet packet = new LeavePacketWriter().build();
        this.clientConnection.send(packet);
    }

    public void loginPacket() {
        Packet packet = new LoginPacketWriter().setUsername(this.clientConnection.getClientUser().getUsername()).build();
        this.clientConnection.send(packet);
    }

    public void joinPacket(String id) {
        Packet packet = new JoinPacketWriter().setRoomId(id).build();
        this.clientConnection.send(packet);
    }

    public void createRoomPacket(int room_size) {
        Packet packet = new CreateRoomPacketWriter().setRoomSize(room_size).build();
        this.clientConnection.send(packet);
    }

    public void messagePacket(String content) {
        Packet packet = new ClientMessagePacketWriter().setMessage(content).build();
        this.clientConnection.send(packet);
    }
}
