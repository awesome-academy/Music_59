package com.example.dung.music_59.data.source.remote;

import android.os.AsyncTask;

import com.example.dung.music_59.data.model.Track;
import com.example.dung.music_59.data.source.TrackDataSource;
import com.example.dung.music_59.utils.StringUtils;
import com.example.dung.music_59.utils.TrackEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchTrackAsync extends AsyncTask<String, Void, List<Track>> {
    public static final int PARAM_URL = 0;
    private String mRequestMethod = "GET";
    private static final String COLLECTION = "collection";
    private static final String TRACK = "track";
    private TrackDataSource.getTrackCallBack mCallBack;
    protected Exception mException;

    public FetchTrackAsync(TrackDataSource.getTrackCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Track> doInBackground(String... strings) {
        List<Track> mTracks = new ArrayList<>();
        try {
            String data = readStream(strings[PARAM_URL]);
            mTracks = parseTrackData(data);
        } catch (IOException e) {
            mException = e;
        } catch (JSONException e) {
            mException = e;
        }
        return mTracks;
    }

    private List<Track> parseTrackData(String responseString) throws JSONException {
        List<Track> tracks = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(responseString);
        JSONArray jsonArray = jsonObject.getJSONArray(COLLECTION);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonTrack = jsonArray.getJSONObject(i).getJSONObject(TRACK);
            long id = jsonTrack.getLong(TrackEntity.ID);
            int duraion = jsonTrack.getInt(TrackEntity.DURATION);
            String title = jsonTrack.getString(TrackEntity.TITLE);
            String streamUrl = StringUtils.initStreamApi(id);
            String artworkUrl = jsonTrack.getString(TrackEntity.ARTWORD_URL);
            boolean isDownloadable = jsonTrack.getBoolean(TrackEntity.DOWNLOADABLE);

            Track.Builder builder = new Track.Builder().setId(id).setDuration(duraion).setTitle(title)
                    .setStreamUrl(streamUrl)
                    .setArtworkUrl(artworkUrl).setDowloadable(isDownloadable);
            Track track = builder.build();
            tracks.add(track);
        }
        return tracks;
    }

    private String readStream(String urlString) throws IOException {
        HttpURLConnection urlConnection;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(mRequestMethod);
        urlConnection.connect();
        StringBuffer response = null;
        int responseCode = urlConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader;
            response = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(List<Track> tracks) {
        super.onPostExecute(tracks);
        if (mException == null) {
            mCallBack.onTrackLoaded(tracks);
        } else {
            mCallBack.onFailure();
        }
    }
}
