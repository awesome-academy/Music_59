package com.example.dung.music_59.ui.genres;

import com.example.dung.music_59.data.model.Genre;
import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.data.source.TrackDataSource;
import com.example.dung.music_59.data.source.TrackRepository;
import com.example.dung.music_59.utils.StringUtils;

import java.util.List;

public class GenresPresenter implements GenresContract.Presenter {
    private static final String GENRE_KIND = "top";
    private static final int LIMIT = 20;
    private static final int OFFSET = 0;
    private GenresContract.View mView;
    private TrackRepository mRepository;

    public GenresPresenter(GenresContract.View view, TrackRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void loadTracksByGenres(Genre genre) {
        String url = StringUtils.initGenreApi(GENRE_KIND, genre.getKey(), LIMIT, OFFSET);
        mRepository.getTrackByGenre(url, new TrackDataSource.OnGetTrackCallBack() {
            @Override
            public void onTracksLoaded(List<Track> tracks) {
                mView.showTracks(tracks);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
