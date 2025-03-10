package me.amarantuss.roomapp.client;

import me.amarantuss.roomapp.client.chat.Chat;
import me.amarantuss.roomapp.client.connection.ClientConnection;
import me.amarantuss.roomapp.util.classes.input.Input;
import me.amarantuss.roomapp.util.classes.network.ClientUser;

public class Client {
    public static void main(String args[]) {
        final String host = "127.0.0.1";
        final int port = 5101;

        System.out.println("Java ChatRoom Client");

        System.out.print("Username: ");
        String username = Input.getStringInput();

        ClientUser user = new ClientUser(username);

        System.out.println("Connecting to server on " + host + ":" + port);
        ClientConnection clientConnection = new ClientConnection(host, port, user);
        clientConnection.connect();
        System.out.println("Connected: " + clientConnection.isConnected());

        if(!clientConnection.isConnected()) return;

        Chat chat = new Chat(clientConnection);
        chat.chat();
    }
}
