package com.example.dung.music_59.ui.playmusic.playlistfragment;

public interface TrackTouchListener {
    void onMode(int oldPositoin, int newPosition);

    void swipe(int position, int direction);
}
