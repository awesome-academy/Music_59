package com.example.dung.music_59.mediaplayer;

import com.example.dung.music_59.data.model.Track;

import java.util.List;

public interface IMediaPlayer {
    void create();

    void start();

    void setTracksList(List<Track> tracks);

    void pause();

    void next();

    void play();

    void previous();

    void setShuffle();

    void setRepeat();

    int getTrackPosition();

    void setTrackPosition(int pos);

    int getTrackDuaration();

    boolean isPlaying();

    void seekTo(int pons);

    int getCurrentPosition();

    Track getTrack();


}
