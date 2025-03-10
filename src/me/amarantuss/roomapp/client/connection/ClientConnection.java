package me.amarantuss.roomapp.client.connection;

import me.amarantuss.roomapp.client.ChatInterface;
import me.amarantuss.roomapp.util.classes.network.ClientUser;
import me.amarantuss.roomapp.util.classes.network.packets.Packet;
import me.amarantuss.roomapp.util.classes.network.packets.PacketFactory;
import me.amarantuss.roomapp.util.classes.network.packets.PacketType;
import me.amarantuss.roomapp.util.classes.network.packets.readers.ExceptionPacketReader;
import me.amarantuss.roomapp.util.classes.network.packets.readers.RoomBroadcastPacketReader;
import me.amarantuss.roomapp.util.classes.network.packets.readers.ServerMessagePacketReader;
import me.amarantuss.roomapp.util.classes.network.packets.readers.SuccessPacketReader;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection {

    private final String host;
    private final int port;
    private final ClientUser clientUser;

    private final ClientConnectionPipe clientConnectionPipe = new ClientConnectionPipe(this);
    private ChatInterface chatInterface;

    private ClientConnectionHandler clientConnectionHandler;

    public ClientConnection(String host, int port, ClientUser clientUser) {
        this.host = host;
        this.port = port;
        this.clientUser = clientUser;
    }

    public void connect() {
        try {
            this.clientConnectionHandler = new ClientConnectionHandler(this, new Socket(host, port));
        } catch (IOException e) {
            System.out.println("Couldn't connect to the server");
        }
    }

    public boolean isConnected() {
        return clientConnectionHandler != null && clientConnectionHandler.isConnected();
    }

    public void receive(String message) {
        switch(PacketFactory.getPacketType(message)) {
            case EXCEPTION -> this.clientConnectionPipe.add(new PacketWrapper(PacketType.EXCEPTION, new ExceptionPacketReader(message)));
            case SERVER_MESSAGE -> this.clientConnectionPipe.add(new PacketWrapper(PacketType.SERVER_MESSAGE, new ServerMessagePacketReader(message)));
            case SUCCESS -> this.clientConnectionPipe.add(new PacketWrapper(PacketType.SUCCESS, new SuccessPacketReader(message)));
            case ROOM_BROADCAST -> this.clientConnectionPipe.add(new PacketWrapper(PacketType.ROOM_BROADCAST, new RoomBroadcastPacketReader(message)));
            case null, default -> {
                return;
            }
        }
    }

    public void send(Packet packet) {
        if(!isConnected()) return;
        this.clientConnectionHandler.send(packet.json());
    }

    public ClientConnectionPipe registerChat(ChatInterface chatInterface) {
        this.chatInterface = chatInterface;
        return clientConnectionPipe;
    }

    public void close() {
        if(this.chatInterface != null) this.chatInterface.close();
        this.clientConnectionHandler.close();
    }

    public void closeClientConnectionOnly() {
        if(this.chatInterface != null) this.chatInterface.close();
    }

    public ClientUser getClientUser() {
        return clientUser;
    }
}
