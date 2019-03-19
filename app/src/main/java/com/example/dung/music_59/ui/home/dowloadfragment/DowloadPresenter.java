package com.example.dung.music_59.ui.home.dowloadfragment;

import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.data.source.TrackDataSource;
import com.example.dung.music_59.data.source.TrackRepository;

import java.util.List;

public class DowloadPresenter implements DowloadContract.Presenter {
    private DowloadContract.View mView;
    private TrackRepository mRepository;

    public DowloadPresenter(DowloadContract.View view, TrackRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void loadTracks() {
        mRepository.getTrackLocal(new TrackDataSource.OnGetTrackLocalCallBack() {
            @Override
            public void onGetTrackCompletion(List<Track> tracks) {
                mView.showTracks(tracks);
            }
        });
    }
}
