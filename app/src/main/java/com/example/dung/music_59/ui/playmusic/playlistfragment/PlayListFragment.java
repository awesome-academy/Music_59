package com.example.dung.music_59.ui.playmusic.playlistfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dung.music_59.R;
import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.service.musicservice.MusicService;
import com.example.dung.music_59.ui.adapter.TracksAdapter;
import com.example.dung.music_59.ui.playmusic.PlayMusicActivity;

import java.util.ArrayList;
import java.util.List;

public class PlayListFragment extends Fragment implements TracksAdapter.onClickTrackListener {
    private List<Track> mTracks;
    private TracksAdapter mTracksAdapter;
    private RecyclerView mRecyclerPlayList;
    private MusicService mMusicService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_list, container, false);
        initView(view);
        addItemTouchCallback(mRecyclerPlayList);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMusicService = ((PlayMusicActivity) getActivity()).getService();
        mTracksAdapter.setTracks(mMusicService.getTracksList());
        mTracks = mMusicService.getTracksList();
    }

    private void initView(View view) {
        mRecyclerPlayList = view.findViewById(R.id.recycler_track_play_list);
        mTracks = new ArrayList<>();
        mTracksAdapter = new TracksAdapter(getContext(), mTracks);
        mRecyclerPlayList.setAdapter(mTracksAdapter);
        mTracksAdapter.setTrackListener(this);
    }

    private void addItemTouchCallback(RecyclerView recyclerView) {
        ItemTouchHelper.Callback callback = new TrackTouchHelperCallback(new TrackTouchListener() {
            @Override
            public void onMode(int oldPositoin, int newPosition) {
                mTracksAdapter.onMove(oldPositoin, newPosition);
            }

            @Override
            public void swipe(int position, int direction) {
                mTracksAdapter.swipe(position, direction);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onTrackClick(Track track) {
        int pos = mMusicService.getTrackPosition(track);
        if (mMusicService != null) mMusicService.setTrack(pos);
        mMusicService.playMusic();

    }
}
