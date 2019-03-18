package com.example.dung.music_59.data.source.local;

import android.content.Context;

import com.example.dung.music_59.data.source.TrackDataSource;

public class TrackLocalDataSource implements TrackDataSource.Local {
    private static TrackLocalDataSource sInstance;
    private Context mContext;

    private TrackLocalDataSource(Context context) {
        mContext = context;
    }

    public static TrackLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TrackLocalDataSource(context);
        }
        return sInstance;
    }

    @Override
    public void getGenres(TrackDataSource.OnGetGenresCallBack callBack) {
        new GenresAsyncTask(mContext, callBack).execute();
    }
}
