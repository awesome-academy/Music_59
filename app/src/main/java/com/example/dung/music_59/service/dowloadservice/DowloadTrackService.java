package com.example.dung.music_59.service.dowloadservice;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.example.dung.music_59.data.model.Track;

public class DowloadTrackService extends IntentService {
    public static final String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    public static final String EXTRA_TRACK = "EXTRA_TRACK";
    private static final String TAG = "DownloadTrackService";
    private static final String BASE_FILE_PATH = "/SoundCloud/%s.mp3";
    private static final String REGEX = "/";
    private static final String REPLACEMENT = "_";

    public DowloadTrackService() {
        super(TAG);
    }

    public static Intent getIntent(Context context, Track track) {
        Intent intent = new Intent(context, DowloadTrackService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DowloadTrackService.EXTRA_TRACK, track);
        intent.putExtra(DowloadTrackService.EXTRA_BUNDLE, bundle);
        return intent;

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle bundle = intent.getBundleExtra(DowloadTrackService.EXTRA_BUNDLE);
        Track track = bundle.getParcelable(DowloadTrackService.EXTRA_TRACK);
        startDowload(track);
    }

    private void startDowload(Track track) {
        String urlDowload = track.getDowloadUrl();
        String part = String.format(BASE_FILE_PATH, track.getTitle().replaceAll(REGEX, REPLACEMENT));
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlDowload));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.allowScanningByMediaScanner();
        request.setTitle(track.getTitle());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, part);
        request.setAllowedOverRoaming(true);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }
}
