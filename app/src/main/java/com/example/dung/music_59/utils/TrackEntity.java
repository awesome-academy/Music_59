package com.example.dung.music_59.utils;

import android.support.annotation.StringDef;

@StringDef({
        TrackEntity.ID,
        TrackEntity.DURATION,
        TrackEntity.TITLE,
        TrackEntity.STREAM_URL,
        TrackEntity.ARTIST,
        TrackEntity.DOWLOAD_URL,
        TrackEntity.DESCRIPTION,
        TrackEntity.ARTWORD_URL,
        TrackEntity.DOWNLOADABLE,
        TrackEntity.TRACK,
        TrackEntity.COLLECTION
})

public @interface TrackEntity {
    String ID = "id";
    String DURATION = "duration";
    String TITLE = "title";
    String STREAM_URL = "url";
    String ARTIST = "artist";
    String DOWLOAD_URL = "download_url";
    String DESCRIPTION = "description";
    String ARTWORD_URL = "artwork_url";
    String DOWNLOADABLE = "downloadable";
    String TRACK = "track";
    String COLLECTION = "collection";
}

