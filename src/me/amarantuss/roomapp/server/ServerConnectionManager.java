package me.amarantuss.roomapp.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerConnectionManager implements Runnable {

    private boolean closing = false;

    private ServerSocket serverSocket;
    private Thread thread = new Thread(this);

    public ServerConnectionManager(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            thread.start();
        } catch (IOException e) {
            System.out.println("ServerConnectionManager - IOException");
        }
    }

    public boolean isRunning() {
        return serverSocket != null && !serverSocket.isClosed();
    }

    public void run() {
        while(!closing) {
            try {
                new ServerConnection(serverSocket.accept());
            } catch (IOException e) {
                System.out.println("ServerConnectionManager (Thread) - IOException");
            }
        }
    }

    public void close() {
        closing = true;
    }
}
