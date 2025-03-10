package me.amarantuss.roomapp.util.classes.network;

import me.amarantuss.roomapp.server.ServerConnection;

import java.util.HashMap;

public class ServerUser {
    private final String username;
    private final ServerConnection serverConnection;

    public ServerUser(String username, ServerConnection serverConnection) {
        this.username = username;
        this.serverConnection = serverConnection;
    }

    public String getUsername() {
        return username;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }
}
