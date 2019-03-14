package com.example.dung.music_59.ui.playmusic.playfragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dung.music_59.R;
import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.service.MusicService;
import com.example.dung.music_59.ui.playmusic.PlayMusicActivity;
import com.example.dung.music_59.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayMusicFragment extends Fragment implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {
    private static final int DELAY_UPDATE_TIME_SONG = 100;
    private static final String ARGUMENT_TRACK = " ARGUMENT_TRACK ";
    private static final String ARGUMENT_LIST_TRACK = "ARGUMENT_LIST_TRACK";
    private Track mTrack;
    private List<Track> mTracks;
    private MusicService mMusicService;
    private ImageView mImageMusic;
    private ImageButton mImageNext;
    private ImageButton mImagePrevious;
    private ImageButton mImagePlay;
    private SeekBar mSeekBar;
    private TextView mTextTimeStart;
    private TextView mTextTrackDuration;
    private TextView mTextNameTrack;

    public static PlayMusicFragment newInstance(Track track, List<Track> tracks) {
        PlayMusicFragment playMusicFragment = new PlayMusicFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGUMENT_TRACK, track);
        args.putParcelableArrayList(ARGUMENT_LIST_TRACK, (ArrayList<? extends Parcelable>) tracks);
        playMusicFragment.setArguments(args);
        return playMusicFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_play_music, container, false);
        getBundle();
        initView(view);
        initHandle();
        upDateTimeSong();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMusicService();
    }

    public void setMusicService() {
        mMusicService = ((PlayMusicActivity) getActivity()).getService();
        mMusicService.setTrackList(mTracks);
        int pos = 0;
        for (int i = 0; i < mTracks.size(); i++) {
            if (mTrack.getId() == mTracks.get(i).getId()) {
                pos = i;
            }
        }
        mMusicService.setTrack(pos);
        mMusicService.playMusic();
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTrack = bundle.getParcelable(ARGUMENT_TRACK);
            mTracks = bundle.getParcelableArrayList(ARGUMENT_LIST_TRACK);
        }
    }

    private void initView(View view) {
        mImageNext = view.findViewById(R.id.image_next);
        mImagePlay = view.findViewById(R.id.image_play);
        mImagePrevious = view.findViewById(R.id.image_previous);
        mImageMusic = view.findViewById(R.id.image_track_play);
        mSeekBar = view.findViewById(R.id.seek_bar);
        mTextTimeStart = view.findViewById(R.id.text_time_start);
        mTextTrackDuration = view.findViewById(R.id.text_time_end);
        mSeekBar.setOnSeekBarChangeListener(this);
        mTextNameTrack = view.findViewById(R.id.text_name_track);
        Glide.with(getContext()).load(mTrack.getArtworkUrl())
                .apply(RequestOptions.circleCropTransform()).into(mImageMusic);
    }

    private void initHandle() {
        mImageNext.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_play:
                handlePlay();
                break;
            case R.id.image_previous:
                handlePrevious();
                break;
            case R.id.image_next:
                handleNext();
                break;
            case R.id.image_loop:
                handleLoop();
                break;
            case R.id.image_shuffle:
                handleShuffle();
        }
    }

    private void handleShuffle() {
        mMusicService.setShuffle();
    }

    private void handleLoop() {
        mMusicService.setRepeat();
    }

    private void handleNext() {
        mImagePlay.setImageResource(R.drawable.ic_pause_black_24dp);
        mMusicService.nextTrack();
    }

    private void handlePrevious() {
        mImagePlay.setImageResource(R.drawable.ic_pause_black_24dp);
        mMusicService.previousTrack();
    }

    private void handlePlay() {
        if (!mMusicService.isPlaying()) {
            mMusicService.pauseToPlayTrack();
            mImagePlay.setImageResource(R.drawable.ic_pause_black_24dp);
        } else {
            mMusicService.pauseTrack();
            mImagePlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMusicService.seekTo(seekBar.getProgress());
    }

    private void upDateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMusicService != null && mMusicService.isPlaying()) {
                    mSeekBar.setProgress(mMusicService.getCurrentPosition());
                    mSeekBar.setMax(mMusicService.getTimeTotal());
                    mTextNameTrack.setText(mMusicService.getTrack().getTitle());
                    getTimeTrack();
                }
                handler.postDelayed(this, DELAY_UPDATE_TIME_SONG);
            }
        }, DELAY_UPDATE_TIME_SONG);
    }

    private void getTimeTrack() {
        mTextTrackDuration.setText(TimeUtils.timeFormat(mMusicService.getTimeTotal()));
        mTextTimeStart.setText(TimeUtils.timeFormat(mMusicService.getCurrentPosition()));
    }
}
