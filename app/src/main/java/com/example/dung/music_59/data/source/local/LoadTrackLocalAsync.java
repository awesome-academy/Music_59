package com.example.dung.music_59.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.data.source.TrackDataSource;
import com.example.dung.music_59.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LoadTrackLocalAsync extends AsyncTask<String, Void, List<Track>> {
    private static final String FILE_FORMAT = "!=0";
    private Context mContext;
    private TrackDataSource.OnGetTrackLocalCallBack mCallBack;

    public LoadTrackLocalAsync(Context context, TrackDataSource.OnGetTrackLocalCallBack callBack) {
        mContext = context;
        mCallBack = callBack;
    }

    @Override
    protected List<Track> doInBackground(String... strings) {
        return getAllMyTracks();
    }

    public List getAllMyTracks() {
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projections = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DATA,
        };
        String selection = StringUtils.append(MediaStore.Audio.Media.IS_MUSIC, FILE_FORMAT);
        Cursor c = mContext.getContentResolver().query(songUri,
                projections, selection, null, null);
        return getAllTrackFromCursor(c);
    }

    private List<Track> getAllTrackFromCursor(Cursor c) {
        List<Track> tracks = new ArrayList<>();
        if (c == null) {
            return tracks;
        }
        c.moveToFirst();
        int titleIndex = c.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
        int dataIndex = c.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        int idColumn = c.getColumnIndex(MediaStore.Audio.Media._ID);
        while (!c.isAfterLast()) {
            String title = c.getString(titleIndex);
            String uri = c.getString(dataIndex);
            String artworkUrl = "";
            Track.Builder builder = new Track.Builder().setId(idColumn).setTitle(title)
                    .setStreamUrl(uri).setArtworkUrl(artworkUrl);
            Track track = builder.build();
            tracks.add(track);

            c.moveToNext();
        }
        c.close();
        return tracks;
    }

    @Override
    protected void onPostExecute(List<Track> tracks) {
        super.onPostExecute(tracks);
        mCallBack.onGetTrackCompletion(tracks);
    }
}
