package me.amarantuss.roomapp.server;

import me.amarantuss.roomapp.util.classes.input.StringFormatter;
import me.amarantuss.roomapp.util.classes.network.ServerUser;
import me.amarantuss.roomapp.util.classes.network.packets.Packet;
import me.amarantuss.roomapp.util.classes.network.packets.writers.RoomBroadcastPacketWriter;
import me.amarantuss.roomapp.util.classes.network.packets.writers.ServerMessagePacketWriter;
import me.amarantuss.roomapp.util.enums.RoomResponse;

import java.time.LocalTime;
import java.util.*;
import java.util.function.IntToDoubleFunction;

public class Room implements Runnable {

    private final String id;
    private final int room_size;

    private final Set<ServerUser> users = new HashSet<>();
    private final List<Packet> messages = new ArrayList<>();

    private final Thread thread = new Thread(this);

    private boolean closing = false;

    private boolean locked = false;

    public Room(String id, int room_size) {
        this.id = id;
        this.room_size = room_size;
    }

    public RoomResponse join(ServerUser user) {
        if(users.contains(user)) return RoomResponse.ALREADY_IN;
        else if(locked) return RoomResponse.LOCKED;
        else if(users.size() >= room_size) return RoomResponse.FULL;

        if(!thread.isAlive()) thread.start();

        users.add(user);
        RoomManager.addToRoom(user, this);
        broadcast("User " + user.getUsername() + " joined to the room");
        return RoomResponse.JOINED;
    }

    public void quit(ServerUser user) {
        users.remove(user);
        RoomManager.removeFromRoom(user);
        if(users.isEmpty()) {
            forceClose();
            return;
        }
        broadcast("User " + user.getUsername() + " left the room");
    }

    public void run() {
        while(!closing) {
            Packet message = readMessage();
            if(closing) break;
            for(ServerUser serverUser : users) {
                serverUser.getServerConnection().send(message);
            }
        }
    }

    private synchronized Packet readMessage() {
        while(messages.isEmpty() && !closing) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Room (Thread) - InterruptedException");
            }
        }

        if(closing) {
            return null;
        }

        Packet firstMessage = messages.getFirst();
        messages.removeFirst();
        return firstMessage;
    }

    private synchronized void broadcast(String message) {
        messages.add(new RoomBroadcastPacketWriter().setMessage(message).build());
        notify();
    }

    public synchronized void send(ServerUser serverUser, String message) {
        LocalTime localTime = LocalTime.now();
        messages.add(new ServerMessagePacketWriter().setUsername(serverUser.getUsername()).setMessage(message).setHour(localTime.getHour()).setMinute(localTime.getMinute()).setSecond(localTime.getSecond()).build());
        notify();
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        if(locked) broadcast("Room is now locked");
        else broadcast("Room is now unlocked");
    }

    public String getId() {
        return id;
    }

    public int getRoomSize() {
        return room_size;
    }

    //todo display status like locked, connected_users etc

    private boolean stopping = false;

    public synchronized boolean forceClose() {
        if(stopping) return false;
        for(ServerUser user : users) {
            RoomManager.removeFromRoom(user);
        }
        RoomManager.deleteRoom(id);

        closing = true;
        notify();
        return true;
    }

    public synchronized void close() {
        if(stopping) return;

        stopping = true;
        if(!users.isEmpty()) broadcast("Room will be closed in 3 seconds");
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            for(ServerUser user : users) {
                RoomManager.removeFromRoom(user);
            }
            RoomManager.deleteRoom(id);

            closing = true;
            //Normal notify() won't work here
            notifyThread();
        }).start();
    }

    private synchronized void notifyThread() {
        notify();
    }
}
