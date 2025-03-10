package me.amarantuss.roomapp.util.classes.input;

import java.time.LocalTime;

public class StringFormatter {
    public static String serverFormat(String userName, String message) {
        LocalTime localTime = LocalTime.now();
        return "[" + localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond() + "] " + userName + " » " + message;
    }
}
