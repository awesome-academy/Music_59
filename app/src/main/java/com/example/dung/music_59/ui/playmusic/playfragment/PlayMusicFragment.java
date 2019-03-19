package com.example.dung.music_59.ui.playmusic.playfragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dung.music_59.R;
import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.service.dowloadservice.DowloadTrackService;
import com.example.dung.music_59.service.musicservice.MusicService;
import com.example.dung.music_59.service.musicservice.MusicServiceListener;
import com.example.dung.music_59.ui.playmusic.PlayMusicActivity;
import com.example.dung.music_59.utils.TimeUtils;

public class PlayMusicFragment extends Fragment implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MusicServiceListener {
    private static final int DELAY_UPDATE_TIME_SONG = 100;
    private static final int MY_REQUEST_READ_STORAGE = 1;
    private static final int EXTRA_DEFAULT_VALUE = 0;
    private static final int EXTRA_VALUE = 1234;

    private Track mTrack;
    private MusicService mMusicService;
    private ImageView mImageMusic;
    private ImageButton mImageNext;
    private ImageButton mImagePrevious;
    private ImageButton mImagePlay;
    private ImageView mImageShuffle;
    private ImageView mImageLoop;
    private SeekBar mSeekBar;
    private TextView mTextTimeStart;
    private TextView mTextTrackDuration;
    private TextView mTextNameTrack;
    private ImageView mImageDowload;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_play_music, container, false);
        initView(view);
        initHandle();
        upDateTimeSong();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMusicService = ((PlayMusicActivity) getActivity()).getService();
        mMusicService.addListener(this);
        int a = getActivity().getIntent().getIntExtra("EXTRA_PLAY_ACTIVITY", EXTRA_DEFAULT_VALUE);
        if (a != EXTRA_VALUE) {
            mMusicService.playMusic();
        }

        mTrack = mMusicService.getTrack();
        if (mTrack != null) {
            Glide.with(getContext()).load(mTrack.getArtworkUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.back_ground_genre).into(mImageMusic);
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
        mImageShuffle = view.findViewById(R.id.image_shuffle);
        mImageLoop = view.findViewById(R.id.image_loop);
        mImageDowload = view.findViewById(R.id.image_dowload);
    }

    private void initHandle() {
        mImageNext.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImageShuffle.setOnClickListener(this);
        mImageLoop.setOnClickListener(this);
        mImageDowload.setOnClickListener(this);
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
                break;
            case R.id.image_dowload:
                checkWriteStoragePremission();
                break;
        }
    }

    private void checkWriteStoragePremission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = DowloadTrackService.getIntent(getContext(), mMusicService.getTrack());
            getActivity().startService(intent);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_REQUEST_READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_REQUEST_READ_STORAGE) return;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = DowloadTrackService.getIntent(getContext(), mMusicService.getTrack());
            getActivity().startService(intent);
            return;
        }
        Toast.makeText(getActivity(), R.string.permission_failure, Toast.LENGTH_SHORT).show();
    }

    private void handleShuffle() {
        mMusicService.setShuffle();
        if (mMusicService.isShuffle())
            mImageShuffle.setImageResource(R.drawable.ic_shuffle_red_24dp);
        else mImageShuffle.setImageResource(R.drawable.ic_shuffle_white_24dp);
    }

    private void handleLoop() {
        mMusicService.setLoopTrack();
        if (mMusicService.isLoop()) {
            mImageLoop.setImageResource(R.drawable.ic_loop_red_24dp);
        } else {
            mImageLoop.setImageResource(R.drawable.ic_loop_white_24dp);
        }
    }

    private void handleNext() {
        mImagePlay.setImageResource(R.drawable.ic_pause_black_24dp);
        mMusicService.nextTrack();
        updateImage();
    }

    private void handlePrevious() {
        mImagePlay.setImageResource(R.drawable.ic_pause_black_24dp);
        mMusicService.previousTrack();
        updateImage();
    }

    private void handlePlay() {
        if (!mMusicService.isPlaying()) {
            mMusicService.playTrack();
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
                    getTimeTrack();
                    mTextNameTrack.setText(mMusicService.getTrack().getTitle());
                    updateBottomController();
                    if (getActivity() != null) {
                        updateImage();
                    }
                }
                handler.postDelayed(this, DELAY_UPDATE_TIME_SONG);
            }
        }, DELAY_UPDATE_TIME_SONG);
    }

    private void getTimeTrack() {
        mTextTrackDuration.setText(TimeUtils.timeFormat(mMusicService.getTimeTotal()));
        mTextTimeStart.setText(TimeUtils.timeFormat(mMusicService.getCurrentPosition()));
    }

    public void updateBottomController() {
        if (mMusicService != null && mMusicService.isPlaying()) {
            mImagePlay.setImageResource(R.drawable.ic_pause_black_24dp);
            return;
        }
        mImagePlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
    }

    public void updateImage() {
        Glide.with(getContext()).load(mMusicService.getTrack().getArtworkUrl())
                .placeholder(R.drawable.back_ground_genre)
                .apply(RequestOptions.circleCropTransform()).into(mImageMusic);
    }

    @Override
    public void listenerChangeState() {
        //updateImage();
        updateBottomController();
    }
}
