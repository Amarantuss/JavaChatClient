package me.amarantuss.roomapp.client.chat;

import me.amarantuss.roomapp.client.ChatInterface;
import me.amarantuss.roomapp.client.connection.ClientConnection;
import me.amarantuss.roomapp.client.connection.ClientConnectionPipe;
import me.amarantuss.roomapp.client.connection.PacketWrapper;
import me.amarantuss.roomapp.server.RoomRole;
import me.amarantuss.roomapp.util.classes.input.Input;
import me.amarantuss.roomapp.util.classes.network.packets.readers.*;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat implements ChatInterface, Runnable {

    private ClientConnectionPipe clientConnectionPipe;

    private final Thread thread = new Thread(this);

    private boolean closing = false;

    public Chat(ClientConnection clientConnection) {
        this.clientConnectionPipe = clientConnection.registerChat(this);
        this.thread.start();
    }

    public void run() {
        this.clientConnectionPipe.loginPacket();
        while(!closing) {
            PacketWrapper packetWrapper = clientConnectionPipe.readMessage();
            if(!closing) display(packetWrapper);
        }
    }

    public void display(PacketWrapper packetWrapper) {
        switch(packetWrapper.getPacketType()) {
            case SUCCESS -> {
                SuccessPacketReader successPacketReader = (SuccessPacketReader) packetWrapper.getPacketReader();
                System.out.println("Success: " + successPacketReader.getDescription());
            }
            case SERVER_MESSAGE -> {
                ServerMessagePacketReader serverMessagePacketReader = (ServerMessagePacketReader) packetWrapper.getPacketReader();
                String hour = serverMessagePacketReader.getHour() < 10 ? "0" + serverMessagePacketReader.getHour() : "" + serverMessagePacketReader.getHour();
                String minute = serverMessagePacketReader.getMinute() < 10 ? "0" + serverMessagePacketReader.getMinute() : "" + serverMessagePacketReader.getMinute();
                String second = serverMessagePacketReader.getSecond() < 10 ? "0" + serverMessagePacketReader.getSecond() : "" + serverMessagePacketReader.getSecond();
                String message = "[" + hour + ":" + minute + ":" + second + "] " + serverMessagePacketReader.getUsername() + " » " + serverMessagePacketReader.getMessage();
                System.out.println(message);
            }
            case ROOM_BROADCAST -> {
                RoomBroadcastPacketReader roomBroadcastPacketReader = (RoomBroadcastPacketReader) packetWrapper.getPacketReader();
                String message = "Room » " + roomBroadcastPacketReader.getMessage();
                System.out.println(message);
            }
            case EXCEPTION -> {
                ExceptionPacketReader exceptionPacketReader = (ExceptionPacketReader) packetWrapper.getPacketReader();
                System.out.println("Exception: (Code: " + exceptionPacketReader.getCode() + ") | (Description: " + exceptionPacketReader.getDescription() + ")");
            }
            case STATUS -> {
                StatusPacketReader statusPacketReader = (StatusPacketReader) packetWrapper.getPacketReader();

                Map<UUID, String> users = statusPacketReader.getUsers();
                Map<UUID, RoomRole> roles = statusPacketReader.getRoles();

                System.out.println("⁂ Status Request ⁂");
                System.out.println("Room Id: " + statusPacketReader.getRoomId());
                System.out.println("Admins:");
                for(UUID uuid : users.keySet()) if(roles.get(uuid).isAdmin()) System.out.println("- " + users.get(uuid) + ": " + uuid.toString());
                System.out.println("Users:");
                for(UUID uuid : users.keySet()) if(!roles.get(uuid).isAdmin()) System.out.println("- " + users.get(uuid) + ": " + uuid.toString());
                System.out.println("Locked: " + statusPacketReader.isLocked());
                System.out.println("Room Size: " + users.size() + "/" + statusPacketReader.getRoomSize());
            }
        }
    }

    public void send(String message) {
        Pattern pattern = Pattern.compile("<login>");
        Matcher matcher = pattern.matcher(message);
        if(matcher.find()) {
            this.clientConnectionPipe.loginPacket();
            return;
        }

        pattern = Pattern.compile("(?<=<room:)\\d+(?=>)");
        matcher = pattern.matcher(message);
        if(matcher.find()) {
            this.clientConnectionPipe.createRoomPacket(Integer.parseInt(matcher.group()));
            return;
        }

        pattern = Pattern.compile("(?<=<join:)[0-9a-fA-F-]{36}(?=>)");
        matcher = pattern.matcher(message);
        if(matcher.find()) {
            this.clientConnectionPipe.joinPacket(matcher.group());
            return;
        }

        pattern = Pattern.compile("(?<=<kick:)[0-9a-fA-F-]{36}(?=>)");
        matcher = pattern.matcher(message);
        if(matcher.find()) {
            this.clientConnectionPipe.kickPacket(matcher.group());
            return;
        }

        pattern = Pattern.compile("(?<=<admin:)[0-9a-fA-F-]{36}(?=:(true|false)>)");
        matcher = pattern.matcher(message);
        if(matcher.find()) {
            String user_id = matcher.group();
            pattern = Pattern.compile("(?<=<admin:[0-9a-fA-F-]{36}:)(true|false)(?=>)");
            matcher = pattern.matcher(message);
            if(matcher.find()) {
                this.clientConnectionPipe.setAdminPacket(user_id, Boolean.parseBoolean(matcher.group()));
                return;
            }
        }

        pattern = Pattern.compile("(?<=<ban:)[0-9a-fA-F-]{36}(?=:(true|false)>)");
        matcher = pattern.matcher(message);
        if(matcher.find()) {
            String user_id = matcher.group();
            pattern = Pattern.compile("(?<=<ban:[0-9a-fA-F-]{36}:)(true|false)(?=>)");
            matcher = pattern.matcher(message);
            if(matcher.find()) {
                this.clientConnectionPipe.setBanPacket(user_id, Boolean.parseBoolean(matcher.group()));
                return;
            }
        }

        pattern = Pattern.compile("<leave>");
        matcher = pattern.matcher(message);
        if(matcher.find()) {
            this.clientConnectionPipe.leavePacket();
            return;
        }

        pattern = Pattern.compile("<close>");
        matcher = pattern.matcher(message);
        if(matcher.find()) {
            this.clientConnectionPipe.closeRoomPacket();
            return;
        }

        pattern = Pattern.compile("(?<=<locked:)(true|false)(?=>)");
        matcher = pattern.matcher(message);
        if(matcher.find()) {
            this.clientConnectionPipe.lockRoomPacket(Boolean.parseBoolean(matcher.group()));
            return;
        }

        pattern = Pattern.compile("<exit>");
        matcher = pattern.matcher(message);
        if(matcher.find()) {
            this.clientConnectionPipe.closeConnection();
            return;
        }

        pattern = Pattern.compile("<status>");
        matcher = pattern.matcher(message);
        if(matcher.find()) {
            this.clientConnectionPipe.statusRequestPacket();
            return;
        }

        this.clientConnectionPipe.messagePacket(message);
    }

    public void chat() {
        while(!closing) {
            String line = Input.getStringInput();
            if(closing) return;
            send(line);
        }
    }

    public void close() {
        //Some info when the chat closes
        closing = true;
        clientConnectionPipe.notifyThread();
    }
}
