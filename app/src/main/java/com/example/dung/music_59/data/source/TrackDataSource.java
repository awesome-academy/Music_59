package com.example.dung.music_59.data.source;

import com.example.dung.music_59.data.model.Genre;
import com.example.dung.music_59.data.model.Track;

import java.util.List;

public interface TrackDataSource {
    interface OnGetGenresCallBack {
        void onGetGenresCompletion(List<Genre> genres);
    }

    interface OnGetTrackCallBack {
        void onTracksLoaded(List<Track> tracks);

        void onFailure(String msg);
    }

    interface OnGetTrackLocalCallBack {
        void onGetTrackCompletion(List<Track> tracks);
    }

    interface Local {
        void getGenres(OnGetGenresCallBack callBack);

        void getTrackLocal(OnGetTrackLocalCallBack callBack);
    }

    interface Remote {
        void getTrackByGenre(String url, OnGetTrackCallBack callBack);
    }

}
