package com.example.dung.music_59.ui.home;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dung.music_59.R;
import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.service.musicservice.MusicService;
import com.example.dung.music_59.ui.adapter.TracksAdapter;
import com.example.dung.music_59.ui.playmusic.PlayMusicActivity;

import java.util.ArrayList;
import java.util.List;

public class DowloadFragment extends Fragment implements TracksAdapter.onClickTrackListener {
    private static final int MY_REQUEST_READ_STORAGE = 123;
    private List<Track> mTracks;
    private RecyclerView mRecyclerTracks;
    private TracksAdapter mTracksAdapter;
    private MusicService mMusicService;

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

    private void initView(View view) {
        mRecyclerTracks = view.findViewById(R.id.recycler_track_dowload);
        mTracks = new ArrayList<>();
        mTracksAdapter = new TracksAdapter(getContext(), mTracks);
        mTracksAdapter.setTrackListener(this);
        mRecyclerTracks.setAdapter(mTracksAdapter);
    }

    @Override
    public void onTrackClick(Track track) {

    }
}
