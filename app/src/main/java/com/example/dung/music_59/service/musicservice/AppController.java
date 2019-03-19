package com.example.dung.music_59.service.musicservice;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

public class AppController extends Application {
    private static AppController mInstance;
    private Activity mPlayMusicActivity;
    private Fragment mPlayMusicFragment;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static AppController getInstance() {
        return mInstance;
    }

    public Activity getPlayMusicActivity() {
        return mPlayMusicActivity;
    }

    public void setPlayMusicActivity(Activity playMusicActivity) {
        mPlayMusicActivity = playMusicActivity;
    }

    public Fragment getPlayMusicFragment() {
        return mPlayMusicFragment;
    }

    public void setPlayMusicFragment(Fragment playMusicFragment) {
        mPlayMusicFragment = playMusicFragment;
    }
}
