package me.amarantuss.roomapp.server;

import me.amarantuss.roomapp.util.classes.network.ServerUser;
import me.amarantuss.roomapp.util.classes.network.packets.Packet;
import me.amarantuss.roomapp.util.classes.network.packets.PacketFactory;
import me.amarantuss.roomapp.util.classes.network.packets.readers.*;
import me.amarantuss.roomapp.util.classes.network.packets.writers.*;
import me.amarantuss.roomapp.util.enums.RoomResponse;

import java.net.Socket;

//todo change packet codes XD

public class ServerConnection {

    private ServerConnectionHandler serverConnectionHandler;

    private ServerUser serverUser;

    public ServerConnection(Socket socket) {
        this.serverConnectionHandler = new ServerConnectionHandler(this, socket);
    }

    public void receive(String message) {
        if(serverUser == null) {
            LoginPacketReader loginPacketReader = new LoginPacketReader(message);

            if(!loginPacketReader.valid()) {
                send(makeExceptionPacket(1, "Not authorized and current message is not authorization"));
                return;
            }

            this.serverUser = new ServerUser(loginPacketReader.getUsername(), this);

            send(makeSuccessPacket("Logged in"));
            return;
        }

        switch (PacketFactory.getPacketType(message)) {
            case CREATE_ROOM -> {
                CreateRoomPacketReader createRoomPacketReader = new CreateRoomPacketReader(message);

                if(!createRoomPacketReader.valid()) {
                    send(makeExceptionPacket(2, "Invalid packet format"));
                    return;
                } else if(RoomManager.getUserRoom(serverUser) != null) {
                    send(makeExceptionPacket(9, "You are in a room"));
                    return;
                }

                Room room = RoomManager.createRoom(createRoomPacketReader.getRoomSize());
                room.join(serverUser);

                send(makeSuccessPacket("Created room with size: " + room.getRoomSize() + " and id: " + room.getId()));
            }
            case JOIN -> {
                JoinPacketReader joinPacketReader = new JoinPacketReader(message);

                if(!joinPacketReader.valid()) {
                    send(makeExceptionPacket(2, "Invalid packet format"));
                    return;
                } else if(RoomManager.getUserRoom(serverUser) != null) {
                    send(makeExceptionPacket(9, "You are already in a room"));
                    return;
                }

                Room room = RoomManager.getRoom(joinPacketReader.getRoomId());
                if(room == null) {
                    send(makeExceptionPacket(3, "Room doesn't exist"));
                    return;
                }
                RoomResponse roomResponse = room.join(serverUser);
                switch (roomResponse) {
                    case JOINED -> send(makeSuccessPacket("Joined to the room"));
                    case ALREADY_IN -> send(makeExceptionPacket(4, "You are already connected with this room"));
                    case FULL -> send(makeExceptionPacket(5, "Room you are trying to connect with is full"));
                    case LOCKED -> send(makeExceptionPacket(6, "Room you are tying to connect with is locked"));
                }
            }
            case CLIENT_MESSAGE -> {
                ClientMessagePacketReader clientMessagePacketReader = new ClientMessagePacketReader(message);

                if(!clientMessagePacketReader.valid()) {
                    send(makeExceptionPacket(2, "Invalid packet format"));
                    return;
                } else if(RoomManager.getUserRoom(serverUser) == null) {
                    send(makeExceptionPacket(8, "You are not connected to any room"));
                    return;
                }

                RoomManager.getUserRoom(serverUser).send(serverUser, clientMessagePacketReader.getMessage());
            }
            case LEAVE -> {
                LeavePacketReader leavePacketReader = new LeavePacketReader(message);

                if(!leavePacketReader.valid()) {
                    send(makeExceptionPacket(2, "Invalid packet format"));
                    return;
                } else if(RoomManager.getUserRoom(serverUser) == null) {
                    send(makeExceptionPacket(8, "You are not connected to any room"));
                    return;
                }

                Room room = RoomManager.getUserRoom(serverUser);
                room.quit(serverUser);

                send(makeSuccessPacket("Left the room"));
            }
            case CLOSE_ROOM -> {
                CloseRoomPacketReader closeRoomPacketReader = new CloseRoomPacketReader(message);

                if(!closeRoomPacketReader.valid()) {
                    send(makeExceptionPacket(2, "Invalid packet format"));
                    return;
                } else if(RoomManager.getUserRoom(serverUser) == null) {
                    send(makeExceptionPacket(8, "You are not connected to any room"));
                    return;
                }

                Room room = RoomManager.getUserRoom(serverUser);

                if(closeRoomPacketReader.getForceClose()) {
                    boolean result = room.forceClose();
                    if(result) send(makeSuccessPacket("Force closed room"));
                    else send(makeExceptionPacket(11, "Room is already closing"));
                    return;
                }
                room.close();
            }
            case LOCK_ROOM -> {
                LockRoomPacketReader lockRoomPacketReader = new LockRoomPacketReader(message);

                if(!lockRoomPacketReader.valid()) {
                    send(makeExceptionPacket(2, "Invalid packet format"));
                    return;
                } else if(RoomManager.getUserRoom(serverUser) == null) {
                    send(makeExceptionPacket(8, "You are not connected to any room"));
                    return;
                }

                Room room = RoomManager.getUserRoom(serverUser);
                room.setLocked(lockRoomPacketReader.getLocked());
            }
            case LOGIN -> send(makeExceptionPacket(10, "You are already logged in"));
            case null, default -> send(makeExceptionPacket(7, "Invalid packet"));
        }
    }

    public void send(Packet packet) {
        this.serverConnectionHandler.send(packet.json());
    }

    public void close() {
        if(RoomManager.getUserRoom(serverUser) != null) {
            RoomManager.getUserRoom(serverUser).quit(serverUser);
        }
        this.serverConnectionHandler.close();
    }

    public void closeServerConnectionOnly() {
        if(RoomManager.getUserRoom(serverUser) != null) {
            RoomManager.getUserRoom(serverUser).quit(serverUser);
        }
    }

    private static Packet makeSuccessPacket(String description) {
        return new SuccessPacketWriter().setDescription(description).build();
    }

    private static Packet makeExceptionPacket(int code, String description) {
        return new ExceptionPacketWriter().setCode(code).setDescription(description).build();
    }
}
