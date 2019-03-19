package com.example.dung.music_59.data.source;

public class TrackRepository implements TrackDataSource.Local, TrackDataSource.Remote {
    private static TrackRepository sInstance;
    private TrackDataSource.Local mLocal;
    private TrackDataSource.Remote mRemote;

    private TrackRepository(TrackDataSource.Local local, TrackDataSource.Remote remote) {
        mLocal = local;
        mRemote = remote;
    }

    public static TrackRepository getInstance(TrackDataSource.Local local, TrackDataSource.Remote remote) {
        if (sInstance == null) {
            sInstance = new TrackRepository(local, remote);
        }
        return sInstance;
    }

    @Override
    public void getGenres(TrackDataSource.OnGetGenresCallBack callBack) {
        mLocal.getGenres(callBack);
    }

    @Override
    public void getTrackLocal(TrackDataSource.OnGetTrackLocalCallBack callBack) {
        mLocal.getTrackLocal(callBack);
    }

    @Override
    public void getTrackByGenre(String url, TrackDataSource.OnGetTrackCallBack callBack) {
        mRemote.getTrackByGenre(url, callBack);
    }
}
