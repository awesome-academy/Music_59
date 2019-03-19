package com.example.dung.music_59.ui.home.dowloadfragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dung.music_59.R;
import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.data.source.TrackRepository;
import com.example.dung.music_59.data.source.local.TrackLocalDataSource;
import com.example.dung.music_59.data.source.remote.TrackRemoteDataSource;
import com.example.dung.music_59.service.musicservice.MusicService;
import com.example.dung.music_59.ui.adapter.TracksAdapter;
import com.example.dung.music_59.ui.home.MainActivity;
import com.example.dung.music_59.ui.playmusic.PlayMusicActivity;

import java.util.ArrayList;
import java.util.List;

public class DowloadFragment extends Fragment implements TracksAdapter.onClickTrackListener,
        DowloadContract.View {
    private static final int MY_REQUEST_READ_STORAGE = 123;
    private List<Track> mTracks;
    private RecyclerView mRecyclerTracks;
    private TracksAdapter mTracksAdapter;
    private MusicService mMusicService;
    private DowloadContract.Presenter mPresenter;
    private TrackRepository mRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_dowload, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMusicService = ((MainActivity) getActivity()).getService();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkStoragePremission()) mPresenter.loadTracks();
    }

    private void initView(View view) {
        mRecyclerTracks = view.findViewById(R.id.recycler_track_dowload);
        mTracks = new ArrayList<>();
        mTracksAdapter = new TracksAdapter(getContext(), mTracks);
        mTracksAdapter.setTrackListener(this);
        mRecyclerTracks.setAdapter(mTracksAdapter);
        mRepository = TrackRepository.getInstance(TrackLocalDataSource.getInstance(getContext()),
                TrackRemoteDataSource.getInstance(getContext()));
        mPresenter = new DowloadPresenter(this, mRepository);

    }

    private boolean checkStoragePremission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            mPresenter.loadTracks();
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_REQUEST_READ_STORAGE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_REQUEST_READ_STORAGE) return;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mPresenter.loadTracks();

        } else {
            Toast.makeText(getActivity(), R.string.permission_failure, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTrackClick(Track track) {
        mMusicService.setTrackList(mTracks);
        if (mMusicService != null) mMusicService.setTrackList(mTracks);
        int pos = mMusicService.getTrackPosition(track);
        if (mMusicService != null) mMusicService.setTrack(pos);
        startActivity(PlayMusicActivity.getIntent(getContext()));
    }

    @Override
    public void showTracks(List<Track> tracks) {
        mTracksAdapter.setTracks(tracks);
        mTracks = tracks;
    }
}
