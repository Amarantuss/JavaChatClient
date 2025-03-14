package me.amarantuss.roomapp.util.classes.network;

import me.amarantuss.roomapp.server.ServerConnection;

import java.util.HashMap;
import java.util.UUID;

public class ServerUser {
    private final String username;
    private final ServerConnection serverConnection;
    private final UUID uuid = UUID.randomUUID();

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

    public UUID getId() {
        return uuid;
    }
}
