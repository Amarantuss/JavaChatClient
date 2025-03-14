package me.amarantuss.roomapp.util.classes.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class ConnectionHandler implements Runnable {
    protected Socket socket;
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;

    protected Thread thread;

    private boolean closed = false;

    protected ConnectionHandler(Socket socket, boolean startThread) {
        try {
            this.socket = socket;
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("ConnectionHandler - IOException");
        }

        this.thread = new Thread(this);
        if(!startThread) return;
        this.thread.start();
        start();
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void run() {
        try {
            while(!socket.isClosed()) {
                receive(dataInputStream.readUTF());
            }
        } catch (IOException e) {
//            System.out.println("ConnectionHandler (Thread) (Input) - IOException");
            if(!closed) {
                stop();
            }
        }
    }

    abstract public void receive(String message);

    public synchronized void send(String message) {
        try {
            dataOutputStream.writeUTF(message);
        } catch (IOException e) {
            if(!closed) {
                System.out.println("ConnectionHandler (Output) - IOException");
            }
        }
    }

    public void close() {
        try {
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
            closed = true;
        } catch (IOException e) {
            System.out.println("ConnectionHandler (Closing) - IOException");
        }
    }

    public void stop() {

    }

    public void start() {

    }
}
