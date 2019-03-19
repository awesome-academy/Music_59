package com.example.dung.music_59.utils;

import com.example.dung.music_59.BuildConfig;

public class StringUtils {

    public static String append(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string);
        }
        return builder.toString();
    }

    public static String initGenreApi(String kind, String keyGenre, int limit, int offset) {
        String BASE_URL_GENRE = StringUtils.append(Constants.BASE_URL, "charts?kind=%s&genre=%s&",
                Constants.CLIENT_ID, Constants.LIMIT, Constants.OFFSET);
        return String.format(BASE_URL_GENRE, kind, keyGenre,
                BuildConfig.CLIENT_ID, limit, offset);
    }

    public static String initStreamApi(long trackId) {
        String BASE_URL_STREAM = StringUtils.append(Constants.BASE_URL_STREAM,
                Constants.TRACKS, "/%s/stream?", Constants.CLIENT_ID);
        return String.format(BASE_URL_STREAM,
                trackId,
                BuildConfig.CLIENT_ID);
    }

    public static String initSearchApt(String a) {
        String BASE_URL_SEARCH = StringUtils.append(Constants.BASE_URL_STREAM,
                Constants.TRACKS, "?q=", a, Constants.CLIENT_ID);
        return null;
    }

    public static String initDowloadApi(long trackId) {
        String BASE_URL_DOWLOAD = StringUtils.append(Constants.BASE_URL_STREAM,
                Constants.TRACKS, "/%s/download?", Constants.CLIENT_ID);
        return String.format(BASE_URL_DOWLOAD,
                trackId,
                BuildConfig.CLIENT_ID);
    }
}
