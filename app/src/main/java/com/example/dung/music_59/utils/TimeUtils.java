package com.example.dung.music_59.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimeUtils {
    public static String timeFormat(int time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(time);
    }
}
