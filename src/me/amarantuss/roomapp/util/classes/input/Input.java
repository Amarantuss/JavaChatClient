package me.amarantuss.roomapp.util.classes.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String getStringInput() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Couldn't read a line");
        }
        return null;
    }
}
