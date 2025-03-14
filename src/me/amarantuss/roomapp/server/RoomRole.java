package me.amarantuss.roomapp.server;

public class RoomRole {
    private boolean admin;

    private RoomRole(boolean admin) {
        this.admin = admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean admin = false;

        private Builder() {

        }

        public Builder setAdmin(boolean admin) {
            this.admin = admin;
            return this;
        }

        public RoomRole build() {
            return new RoomRole(admin);
        }
    }
}
