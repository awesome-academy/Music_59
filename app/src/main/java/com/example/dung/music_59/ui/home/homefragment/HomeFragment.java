package com.example.dung.music_59.ui.home.homefragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dung.music_59.R;
import com.example.dung.music_59.data.model.Genre;
import com.example.dung.music_59.data.source.TrackRepository;
import com.example.dung.music_59.data.source.local.TrackLocalDataSource;
import com.example.dung.music_59.data.source.remote.TrackRemoteDataSource;
import com.example.dung.music_59.ui.adapter.GenresAdapter;
import com.example.dung.music_59.ui.genres.GenresActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment
        implements HomeContract.View, GenresAdapter.onClickGenreListener {
    public static final int SPAN_COUNT = 2;
    private RecyclerView mRecyclerGenres;
    private List<Genre> mGenres;
    private GenresAdapter mGenresAdapter;
    private HomeContract.Presenter mPresenter;
    private TrackRepository mRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        mPresenter.loadGenres();
        return view;
    }

    @Override
    public void showGenres(List<Genre> genres) {
        mGenresAdapter.setGenres(genres);
    }

    @Override
    public void onGenreClick(Genre genre) {
        startActivity(GenresActivity.getIntent(getActivity(), genre));
    }

    private void initView(View view) {
        mRecyclerGenres = view.findViewById(R.id.recycler_genres);
        mGenres = new ArrayList<>();
        mGenresAdapter = new GenresAdapter(getContext(), mGenres);
        mRecyclerGenres.setAdapter(mGenresAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        mRecyclerGenres.setLayoutManager(layoutManager);
        mRepository = TrackRepository.getInstance(TrackLocalDataSource.getInstance(getContext()),
                TrackRemoteDataSource.getInstance(getContext()));
        mPresenter = new HomePresenter(this, mRepository);
        mGenresAdapter.setGenreListener(this);
    }
}
