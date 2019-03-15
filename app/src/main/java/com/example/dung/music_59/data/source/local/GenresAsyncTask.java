package com.example.dung.music_59.data.source.local;

import android.content.Context;
import android.os.AsyncTask;

import com.example.dung.music_59.R;
import com.example.dung.music_59.data.model.Genre;
import com.example.dung.music_59.data.source.TrackDataSource;

import java.util.ArrayList;
import java.util.List;

public class GenresAsyncTask extends AsyncTask<Void, Void, List<Genre>> {
    private static final String BASE_NAME_GENRE = "genres_%d";
    private static final String RAW = "raw";
    private Context mContext;
    private TrackDataSource.OnGetGenresCallBack mCallBack;

    public GenresAsyncTask(Context context, TrackDataSource.OnGetGenresCallBack callBack) {
        mContext = context;
        mCallBack = callBack;
    }

    @Override
    protected List<Genre> doInBackground(Void... voids) {
        List<Genre> genres = new ArrayList<>();
        String nameGenres[] = mContext.getResources().getStringArray(R.array.name_genres);
        String keyGenres[] = mContext.getResources().getStringArray(R.array.key_genres);
        for (int i = 0; i < nameGenres.length; i++) {
            int imageIdGenres = mContext.getResources().
                    getIdentifier(getNameImageGenres(i), RAW, mContext.getPackageName());
            Genre genre = new Genre(keyGenres[i], nameGenres[i], imageIdGenres);
            genres.add(genre);
        }
        return genres;
    }

    public String getNameImageGenres(int i) {
        return String.format(BASE_NAME_GENRE, ++i);
    }

    @Override
    protected void onPostExecute(List<Genre> genres) {
        super.onPostExecute(genres);
        mCallBack.onGetGenresCompletion(genres);
    }
}
