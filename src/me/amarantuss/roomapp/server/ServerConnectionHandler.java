package me.amarantuss.roomapp.server;

import me.amarantuss.roomapp.util.classes.network.ConnectionHandler;

import java.net.Socket;

public class ServerConnectionHandler extends ConnectionHandler {

    private final ServerConnection serverConnection;

    public ServerConnectionHandler(ServerConnection serverConnection, Socket socket) {
        super(socket, true);

        this.serverConnection = serverConnection;
    }

    public void receive(String message) {
        this.serverConnection.receive(message);
    }

    public void stop() {
        close();
        this.serverConnection.closeServerConnectionOnly();
    }
}
