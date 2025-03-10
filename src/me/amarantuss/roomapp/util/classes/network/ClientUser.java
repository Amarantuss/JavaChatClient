package me.amarantuss.roomapp.util.classes.network;

import com.google.gson.Gson;

import java.util.HashMap;

public class ClientUser {
    public final String username;

    public ClientUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
