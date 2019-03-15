package com.example.dung.music_59.ui.playmusic.playlistfragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class TrackTouchHelperCallback extends ItemTouchHelper.Callback {
    private TrackTouchListener mListener;

    public TrackTouchHelperCallback(TrackTouchListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragflag =  ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragflag,swipeFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder viewHolder1) {
        mListener.onMode(viewHolder.getAdapterPosition(),viewHolder1.getAdapterPosition());
        return false;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        mListener.swipe(viewHolder.getAdapterPosition(), i);
    }
}
