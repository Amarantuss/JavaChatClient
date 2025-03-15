package me.amarantuss.roomapp.util.classes.network;

import me.amarantuss.roomapp.util.classes.network.packets.Packet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Sender implements Runnable {
    private final Set<ServerUser> users;
    private final Packet packet;
    private final int delay;

    private Sender(Set<ServerUser> users, Packet packet, int delay) {
        this.users = users;
        this.packet = packet;
        this.delay = delay;
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        if(packet == null) return;
        try {
            Thread.sleep(delay);

            for(ServerUser user : users) {
                user.getServerConnection().send(packet);
            }

        } catch (InterruptedException e) {
            System.out.println("Sender (Thread) - InterruptedException");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Set<ServerUser> users = new HashSet<>();
        private Packet packet;
        private int delay = 0;

        private Builder() {

        }

        public Builder addUser(ServerUser serverUser) {
            this.users.add(serverUser);
            return this;
        }

        public Builder addUsers(Collection<ServerUser> serverUsers) {
            this.users.addAll(serverUsers);
            return this;
        }

        public Builder setPacket(Packet packet) {
            this.packet = packet;
            return this;
        }

        public Builder setDelay(int delay) {
            this.delay = delay;
            return this;
        }

        public Sender build() {
            return new Sender(users, packet, delay);
        }
    }
}
