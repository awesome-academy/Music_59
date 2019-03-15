package com.example.dung.music_59.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.example.dung.music_59.R;
import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.mediaplayer.MediaManager;
import com.example.dung.music_59.ui.playmusic.PlayMusicActivity;

import java.util.List;

public class MusicService extends Service implements MediaManager.OnShowNotifi {
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_CLOSE = "ACTION_CLOSE";
    private static final int REQUEST_CODE = 0;
    private static final int FLAGS_PENDING_INTENT = 0;
    private static final int FORE_GROUND_ID = 1;
    private final IBinder mIBinder = new MusicBinder();
    private MediaManager mMediaManager;
    private RemoteViews mNotificationView;
    private Notification mNotification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() == null) {
            return START_NOT_STICKY;
        } else {
            switch (intent.getAction()) {
                case ACTION_NEXT:
                    nextTrack();
                    break;
                case ACTION_PLAY:
                    if (isPlaying()) {
                        pauseTrack();
                    } else {
                        playTrack();
                    }
                    break;
                case ACTION_PREVIOUS:
                    previousTrack();
                    break;
                case ACTION_CLOSE:
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaManager = MediaManager.getInstance(this);
    }

    @SuppressLint("NewApi")
    public void playMusic() {
        mMediaManager.create();
        // showNotification();
    }

    public void setTrackList(List<Track> tracks) {
        mMediaManager.setTracksList(tracks);
    }

    public void nextTrack() {
        mMediaManager.next();
        changeStatePlay();
    }

    public void previousTrack() {
        mMediaManager.previous();
        changeStatePlay();
    }

    public void pauseTrack() {
        mMediaManager.pause();
        changeStatePlay();
    }

    public void playTrack() {
        mMediaManager.play();
        changeStatePlay();
    }

    public void setShuffle() {
        mMediaManager.setShuffle();
    }

    public void setRepeat() {
        mMediaManager.setLoop();
    }

    public int getPosition() {
        return mMediaManager.getCurrentPosition();
    }

    public int getTimeTotal() {
        return mMediaManager.getTrackDuaration();
    }

    public boolean isPlaying() {
        return mMediaManager.isPlaying();
    }

    public void seekTo(int pons) {
        mMediaManager.seekTo(pons);
    }

    public int getCurrentPosition() {
        return mMediaManager.getCurrentPosition();
    }

    public Track getTrack() {
        return mMediaManager.getTrack();
    }

    public void setTrack(int trackIndex) {
        mMediaManager.setTrackPosition(trackIndex);
    }

    public int getTrackPosition(Track track) {
        return mMediaManager.getTrackPosition(track);
    }

    public List<Track> getTracksList() {
        return mMediaManager.getTracksList();
    }

    @SuppressLint("NewApi")
    public void showNotification() {
        Intent intentMain = new Intent(getApplicationContext(), PlayMusicActivity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        mNotificationView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        mNotificationView.setTextViewText(R.id.text_notifi_name_song, mMediaManager.getTrack().getTitle());
        //Button previous song
        setPreviousNotifiClick();
        //Button pause song
        setPlayNotifiClick();
        //Button next song
        setNextNotifiClick();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                (int) System.currentTimeMillis(), intentMain, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification = new Notification.Builder(this).build();
        mNotification.bigContentView = mNotificationView;
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
        mNotification.icon = R.drawable.ic_launcher_background;
        mNotification.contentIntent = pendingIntent;
        startForeground(FORE_GROUND_ID, mNotification);
    }

    private void setPreviousNotifiClick() {
        Intent previousIntent = new Intent(this, MusicService.class);
        previousIntent.setAction(MusicService.ACTION_PREVIOUS);
        PendingIntent pendingIntent
                = PendingIntent.getService(getApplicationContext(), REQUEST_CODE, previousIntent, 0);
        mNotificationView.setOnClickPendingIntent(R.id.image_notifi_previous, pendingIntent);
    }

    private void setPlayNotifiClick() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.ACTION_PLAY);
        PendingIntent pendingIntent
                = PendingIntent.getService(getApplicationContext(), REQUEST_CODE, intent, 0);
        mNotificationView.setOnClickPendingIntent(R.id.image_notifi_pause, pendingIntent);
    }

    private void setNextNotifiClick() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.ACTION_NEXT);
        PendingIntent pendingIntent
                = PendingIntent.getService(getApplicationContext(), REQUEST_CODE, intent, 0);
        mNotificationView.setOnClickPendingIntent(R.id.image_notifi_next, pendingIntent);
    }

    public void changeStatePlay() {
        if (mMediaManager.isPlaying()) {
            mNotificationView.setImageViewResource(R.id.image_notifi_pause, R.drawable.ic_pause_black_24dp);
        } else {
            mNotificationView.setImageViewResource(R.id.image_notifi_pause, R.drawable.ic_play_arrow_white_24dp);
        }
        startForeground(FORE_GROUND_ID, mNotification);
    }

    @Override
    public void onShowNotifi() {
        showNotification();
        changeStatePlay();
    }

    public boolean isShuffle() {
        return mMediaManager.isShuffle();
    }

    public void setLoopTrack() {
        mMediaManager.setLoop();
    }

    public boolean isLoop() {
        return mMediaManager.isLoop();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
