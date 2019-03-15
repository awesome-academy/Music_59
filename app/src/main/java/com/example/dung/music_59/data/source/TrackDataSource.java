package com.example.dung.music_59.data.source;

import com.example.dung.music_59.data.model.Genre;
import com.example.dung.music_59.data.model.Track;

import java.util.List;

public interface TrackDataSource {
    interface onGetGenresCallBack {
        void onGetGenresCompletion(List<Genre> genres);
    }

    interface onGetTrackCallBack {
        void onTrackLoaded(List<Track> tracks);

        void onFailure();
    }

    interface Local {
        void getGenres(onGetGenresCallBack callBack);
    }

    interface Remote {
        void getTrackByGenre(String url, onGetTrackCallBack callBack);
    }

}
