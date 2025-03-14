package me.amarantuss.roomapp.server;

import me.amarantuss.roomapp.util.classes.network.ServerUser;
import me.amarantuss.roomapp.util.classes.network.packets.Packet;
import me.amarantuss.roomapp.util.classes.network.packets.writers.RoomBroadcastPacketWriter;
import me.amarantuss.roomapp.util.classes.network.packets.writers.ServerMessagePacketWriter;
import me.amarantuss.roomapp.util.enums.RoomBanResponse;
import me.amarantuss.roomapp.util.enums.RoomJoinResponse;
import me.amarantuss.roomapp.util.enums.RoomKickResponse;

import java.time.LocalTime;
import java.util.*;

// todo Made a new Room Specific one message sender that starts a new Thread a send message also freeze the current Thread :)

public class Room implements Runnable {

    private final String id;
    private final int room_size;

    private final Map<UUID, ServerUser> users = new HashMap<>();
    private final Map<UUID, RoomRole> roles = new HashMap<>();
    private final List<Packet> messages = new ArrayList<>();

    private final Set<UUID> banned_users = new HashSet<>();

    private final Thread thread = new Thread(this);

    private boolean closing = false;

    private boolean locked = false;

    public Room(String id, int room_size) {
        this.id = id;
        this.room_size = room_size;
    }

    private void removeUser(ServerUser serverUser) {
        RoomManager.removeFromRoom(serverUser);
        users.remove(serverUser.getId());
        roles.remove(serverUser.getId());
    }

    private void addUser(ServerUser serverUser, boolean admin) {
        RoomManager.addToRoom(serverUser, this);
        users.put(serverUser.getId(), serverUser);
        roles.put(serverUser.getId(), RoomRole.builder().setAdmin(admin).build());
    }

    private void adminCheck() {
        int admins = 0;
        for(ServerUser user : users.values()) {
            if(roles.get(user.getId()).isAdmin()) admins++;
        }

        if(admins > 0) return;

        for(UUID uuid : roles.keySet()) {
            roles.get(uuid).setAdmin(true);
            //todo make message for that user
            break;
        }
    }

    public RoomJoinResponse join(ServerUser user, boolean admin) {
        if(users.containsKey(user.getId())) return RoomJoinResponse.ALREADY_IN;
        else if(locked) return RoomJoinResponse.LOCKED;
        else if(users.size() >= room_size) return RoomJoinResponse.FULL;
        else if(banned_users.contains(user.getId())) return RoomJoinResponse.BANNED;

        if(!thread.isAlive()) thread.start();

        addUser(user, admin);
        broadcast("User " + user.getUsername() + " joined with id: " + user.getId().toString());
        return RoomJoinResponse.JOINED;
    }

    public void quit(ServerUser user) {
        removeUser(user);
        if(users.isEmpty()) {
            forceClose();
            return;
        }
        adminCheck();
        broadcast("User " + user.getUsername() + " left the room");
    }

    public RoomKickResponse kick(UUID uuid) {
        if(!users.containsKey(uuid)) return RoomKickResponse.NO_USER_FOUND;
        else if(getUserRole(uuid).isAdmin()) return RoomKickResponse.IS_ADMIN;

        //todo Add some info for the user
        removeUser(users.get(uuid));
        return RoomKickResponse.KICKED;
    }

    public RoomBanResponse setBan(UUID uuid, boolean banned) {
        if(roles.containsKey(uuid) && roles.get(uuid).isAdmin()) return RoomBanResponse.IS_ADMIN;

        if(banned) {
            if(banned_users.contains(uuid)) return RoomBanResponse.ALREADY_BANNED;
            banned_users.add(uuid);
            if(users.containsKey(uuid)) removeUser(users.get(uuid));
            return RoomBanResponse.BANNED;
        }
        else {
            if(!banned_users.contains(uuid)) return RoomBanResponse.NOT_BANNED;
            banned_users.remove(uuid);
            return RoomBanResponse.UNBANNED;
        }
    }

    public void run() {
        while(!closing) {
            Packet message = readMessage();
            if(closing) break;
            for(ServerUser serverUser : users.values()) {
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

    public RoomRole getUserRole(UUID uuid) {
        return roles.getOrDefault(uuid, null);
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
        for(ServerUser user : users.values()) {
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
            for(ServerUser user : users.values()) {
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
