package me.amarantuss.roomapp.client.connection;

import me.amarantuss.roomapp.util.classes.network.ConnectionHandler;

import java.net.Socket;

public class ClientConnectionHandler extends ConnectionHandler {
    private final ClientConnection clientConnection;

    public ClientConnectionHandler(ClientConnection clientConnection, Socket socket) {
        super(socket, true);

        this.clientConnection = clientConnection;
    }

    public void receive(String message) {
        this.clientConnection.receive(message);
    }

    public void stop() {
        close();
        this.clientConnection.closeClientConnectionOnly();
    }
}
