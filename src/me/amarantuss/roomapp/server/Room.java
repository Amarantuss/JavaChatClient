package me.amarantuss.roomapp.server;

import me.amarantuss.roomapp.util.classes.network.Sender;
import me.amarantuss.roomapp.util.classes.network.ServerUser;
import me.amarantuss.roomapp.util.classes.network.packets.Packet;
import me.amarantuss.roomapp.util.classes.network.packets.writers.RoomBroadcastPacketWriter;
import me.amarantuss.roomapp.util.classes.network.packets.writers.ServerMessagePacketWriter;
import me.amarantuss.roomapp.util.classes.network.packets.writers.StatusPacketWriter;
import me.amarantuss.roomapp.util.classes.network.packets.writers.StatusRequestPacketWriter;
import me.amarantuss.roomapp.util.enums.RoomAdminResponse;
import me.amarantuss.roomapp.util.enums.RoomBanResponse;
import me.amarantuss.roomapp.util.enums.RoomJoinResponse;
import me.amarantuss.roomapp.util.enums.RoomKickResponse;

import javax.management.relation.Role;
import java.time.LocalTime;
import java.util.*;

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

    public RoomJoinResponse join(ServerUser user, boolean admin) {
        if(isUser(user)) return RoomJoinResponse.ALREADY_IN;
        else if(locked) return RoomJoinResponse.LOCKED;
        else if(users.size() >= room_size) return RoomJoinResponse.FULL;
        else if(isBanned(user)) return RoomJoinResponse.BANNED;

        if(!thread.isAlive()) thread.start();

        addUser(user, admin);
        broadcast("User " + user.getUsername() + " joined with id: " + user.getId().toString());
        return RoomJoinResponse.JOINED;
    }

    public void quit(ServerUser user) {
        removeUser(user);
        if(users.isEmpty()) {
            close();
            return;
        }
        adminCheck();
        broadcast("User " + user.getUsername() + " left the room");
    }

    public RoomKickResponse kick(UUID uuid) {
        if(!isUser(uuid)) return RoomKickResponse.NO_USER_FOUND;
        else if(isAdmin(uuid)) return RoomKickResponse.IS_ADMIN;

        Sender.builder().addUser(getUser(uuid)).setPacket(new RoomBroadcastPacketWriter().setMessage("You have been kicked").build()).build().start();
        removeUser(getUser(uuid));

        return RoomKickResponse.KICKED;
    }

    public RoomBanResponse setBan(UUID uuid, boolean banned) {
        if(isUser(uuid) && isAdmin(uuid)) return RoomBanResponse.IS_ADMIN;

        if(banned) {
            if(isBanned(uuid)) return RoomBanResponse.ALREADY_BANNED;
            banned_users.add(uuid);

            if(isUser(uuid)) {
                Sender.builder().addUser(getUser(uuid)).setPacket(new RoomBroadcastPacketWriter().setMessage("You have been banned").build()).build().start();
                removeUser(getUser(uuid));
            }

            return RoomBanResponse.BANNED;
        } else {
            if(!isBanned(uuid)) return RoomBanResponse.NOT_BANNED;
            banned_users.remove(uuid);

            return RoomBanResponse.UNBANNED;
        }
    }

    public RoomAdminResponse setAdmin(UUID uuid, boolean admin) {
        if(isUser(uuid)) return RoomAdminResponse.NO_USER_FOUND;

        if(admin) {
            if(!isAdmin(uuid)) {
                getRole(uuid).setAdmin(true);
                Sender.builder().addUser(getUser(uuid)).setPacket(new RoomBroadcastPacketWriter().setMessage("You have been promoted to admin").build()).build().start();
                return RoomAdminResponse.PROMOTED;
            } else return RoomAdminResponse.ALREADY_ADMIN;
        } else {
            if(isAdmin(uuid)) {
                getRole(uuid).setAdmin(false);
                Sender.builder().addUser(getUser(uuid)).setPacket(new RoomBroadcastPacketWriter().setMessage("You have been promoted to admin").build()).build().start();
                return RoomAdminResponse.DEMOTED;
            } else return RoomAdminResponse.ALREADY_USER;
        }
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        if(locked) broadcast("Room is now locked");
        else broadcast("Room is now unlocked");
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

    private void adminCheck() {
        int admins = 0;
        for(ServerUser user : users.values()) {
            if(isAdmin(user)) admins++;
        }

        if(admins > 0) return;

        for(UUID uuid : roles.keySet()) {
            getRole(uuid).setAdmin(true);
            Sender.builder().addUser(getUser(uuid)).setPacket(new RoomBroadcastPacketWriter().setMessage("You have been promoted to admin because last admin left").build()).build().start();
            break;
        }
    }

    public synchronized void close() {
        Sender.builder().addUsers(users.values()).setPacket(new RoomBroadcastPacketWriter().setMessage("Room is closing").build()).build().start();

        for(ServerUser user : users.values()) {
            RoomManager.removeFromRoom(user);
        }
        RoomManager.deleteRoom(id);

        closing = true;
        notify();
    }


    // Getters

    // Public
    public String getId() {
        return id;
    }

    public int getRoomSize() {
        return room_size;
    }

    public boolean isUser(UUID uuid) {
        return users.containsKey(uuid);
    }

    public boolean isUser(ServerUser user) {
        return users.containsKey(user.getId());
    }

    public boolean isAdmin(UUID uuid) {
        return getRole(uuid).isAdmin();
    }

    public boolean isAdmin(ServerUser serverUser) {
        return getRole(serverUser).isAdmin();
    }

    public boolean isBanned(UUID uuid) {
        return banned_users.contains(uuid);
    }

    public boolean isBanned(ServerUser user) {
        return banned_users.contains(user.getId());
    }

    public Packet getStatus() {
        StatusPacketWriter statusPacketWriter = new StatusPacketWriter();

        Map<UUID, String> users = new HashMap<>();
        for(UUID uuid : this.users.keySet()) users.put(uuid, getUser(uuid).getUsername());
        statusPacketWriter.setUsers(users);

        statusPacketWriter.setRoles(roles);
        statusPacketWriter.setRoomId(id);
        statusPacketWriter.setLocked(locked);
        statusPacketWriter.setRoomSize(room_size);

        return statusPacketWriter.build();
    }

    // Private
    private RoomRole getRole(UUID uuid) {
        return roles.getOrDefault(uuid, null);
    }

    private RoomRole getRole(ServerUser serverUser) {
        return roles.getOrDefault(serverUser.getId(), null);
    }

    private ServerUser getUser(UUID uuid) {
        return users.getOrDefault(uuid, null);
    }


    // Thread related
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

}
