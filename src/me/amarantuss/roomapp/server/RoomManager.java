package me.amarantuss.roomapp.server;

import me.amarantuss.roomapp.util.classes.network.ServerUser;

import java.util.HashMap;
import java.util.UUID;

public class RoomManager {
    private static final HashMap<String, Room> rooms = new HashMap<>();
    private static final HashMap<ServerUser, Room> users = new HashMap<>();

    public static Room getRoom(String id) {
        return rooms.getOrDefault(id, null);
    }

    public static Room createRoom(int room_size) {
        room_size = Math.max(Math.min(room_size, 8), 2);
        String id = UUID.randomUUID().toString();
        Room room = new Room(id, room_size);
        rooms.put(id, room);
        return room;
    }

    static void deleteRoom(String id) {
        rooms.remove(id);
    }

    public static void addToRoom(ServerUser serverUser, Room room) {
        users.put(serverUser, room);
    }

    public static Room getUserRoom(ServerUser serverUser) {
        return users.getOrDefault(serverUser, null);
    }

    public static void removeFromRoom(ServerUser serverUser) {
        users.remove(serverUser);
    }
}
