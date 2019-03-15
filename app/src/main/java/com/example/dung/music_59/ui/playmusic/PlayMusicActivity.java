package com.example.dung.music_59.ui.playmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.dung.music_59.R;
import com.example.dung.music_59.service.AppController;
import com.example.dung.music_59.service.MusicService;
import com.example.dung.music_59.ui.adapter.ViewPagerPlayAdapter;
import com.example.dung.music_59.ui.playmusic.playfragment.PlayMusicFragment;
import com.example.dung.music_59.ui.playmusic.playlistfragment.PlayListFragment;

public class PlayMusicActivity extends AppCompatActivity {
    private static final int PAGE_LIMIT = 2;
    public static MusicService sMusicService;
    private ViewPager mViewPager;
    private Intent mPlayIntent;
    private boolean mMusicBound = false;
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
            if (sMusicService == null) {
                sMusicService = binder.getService();
            }
            setUpViewPager();
            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMusicBound = false;
        }
    };

    public static Intent getIntent(Context context) {
        return new Intent(context, PlayMusicActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(PlayMusicActivity.this, MusicService.class);
            bindService(mPlayIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        }
        initView();
        setToolbar();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private void setToolbar() {
        Toolbar mToolbarPlay;
        mToolbarPlay = findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbarPlay);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpViewPager() {
        ViewPagerPlayAdapter viewPagerPlayAdapter = new ViewPagerPlayAdapter(getSupportFragmentManager());
        viewPagerPlayAdapter.addFragment(new PlayMusicFragment(), getString(R.string.title_home));
        viewPagerPlayAdapter.addFragment(new PlayListFragment(), getString(R.string.title_user));
        mViewPager.setAdapter(viewPagerPlayAdapter);
        mViewPager.setOffscreenPageLimit(PAGE_LIMIT);
    }

    private void initView() {
        mViewPager = findViewById(R.id.view_pager_play_music);
        AppController.getInstance().setPlayMusicActivity(this);

    }

    public MusicService getService() {
        return sMusicService;
    }
}
