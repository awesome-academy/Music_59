package com.example.dung.music_59.ui.genres;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dung.music_59.R;
import com.example.dung.music_59.data.model.Genre;
import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.data.source.TrackRepository;
import com.example.dung.music_59.data.source.local.TrackLocalDataSource;
import com.example.dung.music_59.data.source.remote.TrackRemoteDataSource;
import com.example.dung.music_59.service.musicservice.MusicService;
import com.example.dung.music_59.ui.adapter.TracksAdapter;
import com.example.dung.music_59.ui.playmusic.PlayMusicActivity;

import java.util.ArrayList;
import java.util.List;

public class GenresActivity extends AppCompatActivity
        implements TracksAdapter.onClickTrackListener, GenresContract.View {
    private static final String BUNDLE_GRENRE = " BUNDLE_GRENRE";
    private static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    public static MusicService sMusicService;
    private ImageView mImageGenre;
    private RecyclerView mRecyclerTrack;
    private TracksAdapter mTracksAdapter;
    private List<Track> mTracks;
    private GenresContract.Presenter mPresenter;
    private Genre mGenre;
    private Track mTrack;
    private Intent mPlayIntent;
    private boolean mISBound;
    private ServiceConnection mServiceConnection;

    public static Intent getIntent(Context context, Genre genre) {
        Intent intent = new Intent(context, GenresActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_GRENRE, genre);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundle();
        setContentView(R.layout.activity_genres);
        boundService();
        initView();
        setToolbar();
        mPresenter.loadTracksByGenres(mGenre);
    }

    private void boundService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
                if (sMusicService == null) {
                    sMusicService = binder.getService();
                }
                mISBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mISBound = false;
            }
        };
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(GenresActivity.this, MusicService.class);
            bindService(mPlayIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
        if (bundle != null) mGenre = (Genre) bundle.getParcelable(BUNDLE_GRENRE);
    }

    private void setToolbar() {
        Toolbar mToolbarGenres;
        mToolbarGenres = findViewById(R.id.tool_bar_genre);
        setSupportActionBar(mToolbarGenres);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mTracksAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mImageGenre = findViewById(R.id.image_track_genre);
        mRecyclerTrack = findViewById(R.id.recycler_track_genre);
        mTracks = new ArrayList<>();
        mTracksAdapter = new TracksAdapter(getApplicationContext(), mTracks);
        mRecyclerTrack.setAdapter(mTracksAdapter);
        mTracksAdapter.setTrackListener(this);
        mPresenter = new GenresPresenter(this,
                TrackRepository.getInstance(TrackLocalDataSource.getInstance(getApplicationContext()),
                        TrackRemoteDataSource.getInstance(getApplicationContext())));
        Glide.with(getApplicationContext()).load(mGenre.getImageId()).into(mImageGenre);
    }

    @Override
    public void onTrackClick(Track track) {
        if (sMusicService != null) {
            sMusicService.setTrackList(mTracks);
            int pos = sMusicService.getTrackPosition(track);
            sMusicService.setTrack(pos);
            startActivity(PlayMusicActivity.getIntent(getApplicationContext()));
        }
    }

    @Override
    public void showTracks(List<Track> tracks) {
        mTracks = tracks;
        mTracksAdapter.setTracks(tracks);
    }
}
