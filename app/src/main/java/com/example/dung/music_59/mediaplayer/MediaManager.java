package com.example.dung.music_59.mediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.service.MusicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MediaManager implements IMediaPlayer, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static MediaManager sInstance;
    private List<Track> mTracks;
    private MediaPlayer mMediaPlayer;
    private int mTrackPosition;
    private boolean mIsRestartNotifi;
    private boolean mIsShuffle;
    private boolean mIsRepeat;
    private OnFailure mLoadFail;
    private OnShowNotifi mOnShowNotifi;

    private MediaManager(OnShowNotifi onShowNotifi) {
        mTracks = new ArrayList<>();
        mMediaPlayer = new MediaPlayer();
        mOnShowNotifi = onShowNotifi;
    }

    private MediaManager() {

    }

    public static MediaManager getInstance(OnShowNotifi onShowNotifi) {
        if (sInstance == null) {
            sInstance = new MediaManager(onShowNotifi);
        }
        return sInstance;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nextTrack();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mOnShowNotifi.onShowNotifi();
    }

    @Override
    public void create() {
        mMediaPlayer.reset();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(String.valueOf(Uri.parse(mTracks.get(mTrackPosition).getStreamUrl())));
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            mLoadFail.onLoadFail(e.getMessage());
        }
    }

    @Override
    public void setTracksList(List<Track> tracks) {
        mTracks = tracks;
    }

    @Override
    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void next() {
        if (mIsRepeat) {

        } else if (mIsShuffle) {
            shuffle();
        } else {
            nextTrack();
        }
        create();
    }

    @Override
    public void start() {
        mMediaPlayer.start();
    }

    private void nextTrack() {
        mTrackPosition++;
        if (mTrackPosition > mTracks.size() - 1) {
            mTrackPosition = 0;
        }
    }

    private void shuffle() {
        int newPosition = mTrackPosition;
        while (newPosition == mTrackPosition) {
            Random mRandom = new Random();
            newPosition = mRandom.nextInt(mTracks.size());
        }
        mTrackPosition = newPosition;
    }

    @Override
    public void play() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void previous() {
        mTrackPosition--;
        if (mTrackPosition < 0) {
            mTrackPosition = mTracks.size() - 1;
        }
        create();
    }

    @Override
    public void seekTo(int pons) {
        mMediaPlayer.seekTo(pons);
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public Track getTrack() {
        return mTracks.get(mTrackPosition);
    }

    @Override
    public void setShuffle() {
        if (mIsShuffle) {
            mIsShuffle = false;
        } else {
            mIsShuffle = true;
        }
    }

    @Override
    public void setRepeat() {
        if (mIsRepeat) {
            mIsRepeat = false;
        } else {
            mIsRepeat = true;
        }
    }

    @Override
    public int getTrackPosition(Track track) {
        int pos = 0;
        for (int i = 0; i < mTracks.size(); i++) {
            if (track.getId() == mTracks.get(i).getId()) {
                pos = i;
            }
        }
        mTrackPosition = pos;
        return mTrackPosition;
    }

    @Override
    public void setTrackPosition(int pos) {

    }

    @Override
    public int getTrackDuaration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    interface OnFailure {
        void onLoadFail(String msg);
    }
    
    public interface OnShowNotifi{
        void onShowNotifi();
    }
}
