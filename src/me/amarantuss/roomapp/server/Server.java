package me.amarantuss.roomapp.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String args[]) {

        final int port = 5101;

        System.out.println("Starting ChatRoom server on port [" + port + "]");
        ServerConnectionManager serverConnectionManager = new ServerConnectionManager(port);
        if(!serverConnectionManager.isRunning()) {
            System.out.println("Can't start the server");
        } else {
            System.out.println("Started server");
        }
    }
}
