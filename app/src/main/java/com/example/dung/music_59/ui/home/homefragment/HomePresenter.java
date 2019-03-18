package com.example.dung.music_59.ui.home.homefragment;

import com.example.dung.music_59.data.model.Genre;
import com.example.dung.music_59.data.source.TrackDataSource;
import com.example.dung.music_59.data.source.TrackRepository;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mView;
    private TrackRepository mRepository;

    public HomePresenter(HomeContract.View view, TrackRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void loadGenres() {
        mRepository.getGenres(new TrackDataSource.OnGetGenresCallBack() {
            @Override
            public void onGetGenresCompletion(List<Genre> genres) {
                mView.showGenres(genres);
            }
        });
    }
}
