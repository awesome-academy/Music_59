package com.example.dung.music_59.ui.home.dowloadfragment;

import com.example.dung.music_59.data.model.Track;

import java.util.List;

public interface DowloadContract {
    interface View {
        void showTracks(List<Track> tracks);
    }

    interface Presenter {
        void loadTracks();
    }
}
