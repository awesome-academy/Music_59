package com.example.dung.music_59.ui.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.dung.music_59.R;
import com.example.dung.music_59.service.musicservice.MusicService;
import com.example.dung.music_59.ui.adapter.ViewPagerAdapter;
import com.example.dung.music_59.ui.home.dowloadfragment.DowloadFragment;
import com.example.dung.music_59.ui.home.homefragment.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private static final int PAGE_LIMIT = 3;
    private static final int PAGE_HOME = 0;
    private static final int PAGE_USER = 1;
    private static final int PAGE_DOWLOAD = 2;
    private static MusicService sMusicService;
    private ViewPager mHomeViewPager;
    private TabLayout mHomeTabLayout;
    private Intent mPlayIntent;
    private boolean mISBound;
    private ServiceConnection mServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boundService();
        setToolbar();
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        sMusicService.setIsRunBackGround(false);
        super.onDestroy();

    }

    private void boundService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
                if (sMusicService == null) {
                    sMusicService = binder.getService();
                }
                sMusicService.setIsRunBackGround(true);
                initView();
                setUpViewPager();
                setupTabIcon();
                mISBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mISBound = false;
            }
        };
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(MainActivity.this, MusicService.class);
            bindService(mPlayIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        }
    }

    private void setToolbar() {
        Toolbar mToolbarHome = findViewById(R.id.tool_bar_home);
        setSupportActionBar(mToolbarHome);
        getSupportActionBar().setTitle(R.string.title_music_59);
    }

    private void setUpViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new HomeFragment(), getString(R.string.title_home));
        pagerAdapter.addFragment(new UserFragment(), getString(R.string.title_user));
        pagerAdapter.addFragment(new DowloadFragment(), getString(R.string.title_dowload));
        mHomeViewPager.setAdapter(pagerAdapter);
        mHomeViewPager.setOffscreenPageLimit(PAGE_LIMIT);
    }

    private void initView() {
        mHomeViewPager = findViewById(R.id.view_pager);
        mHomeTabLayout = findViewById(R.id.tab_layout);
        mHomeTabLayout.setupWithViewPager(mHomeViewPager);
    }

    public void setupTabIcon() {
        mHomeTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.color_accent);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.color_black);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mHomeTabLayout.getTabAt(PAGE_HOME).setIcon(R.drawable.ic_home_white_24dp);
        mHomeTabLayout.getTabAt(PAGE_USER).setIcon(R.drawable.ic_account_circle_white_24dp);
        mHomeTabLayout.getTabAt(PAGE_DOWLOAD).setIcon(R.drawable.ic_file_download_white_24dp);
    }

    public MusicService getService() {
        return sMusicService;
    }

}
