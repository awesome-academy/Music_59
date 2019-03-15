package com.example.dung.music_59.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dung.music_59.R;
import com.example.dung.music_59.data.model.Track;

import java.util.Collections;
import java.util.List;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {
    private Context mContext;
    private List<Track> mTracks;
    private LayoutInflater mInflater;
    private onClickTrackListener mTrackListener;

    public TracksAdapter(Context context, List<Track> tracks) {
        mContext = context;
        mTracks = tracks;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.custom_item_track, viewGroup, false);
        return new ViewHolder(view, mTrackListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.onBindView(mTracks.get(i));
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public void setTrackListener(onClickTrackListener trackListener) {
        mTrackListener = trackListener;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
        notifyDataSetChanged();
    }

    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mTracks, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mTracks, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void swipe(int position, int direction) {
        mTracks.remove(position);
        notifyItemRemoved(position);
    }

    public interface onClickTrackListener {
        void onTrackClick(Track track);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageTrack;
        private TextView mTextNameTrack;
        private TextView mTextNameArtist;
        private ImageView mImageFavorite;
        private ImageView mImageDowload;
        private onClickTrackListener mTrackListener;
        private Track mTrack;

        public ViewHolder(@NonNull View itemView, onClickTrackListener trackListener) {
            super(itemView);
            mImageTrack = itemView.findViewById(R.id.image_track);
            mTextNameTrack = itemView.findViewById(R.id.text_name_track);
            mTextNameArtist = itemView.findViewById(R.id.text_name_artist);
            mImageDowload = itemView.findViewById(R.id.image_dowload);
            mImageFavorite = itemView.findViewById(R.id.image_favorite);
            mTrackListener = trackListener;
        }

        public void onBindView(Track track) {
            mTextNameTrack.setText(track.getTitle());
            mTextNameArtist.setText(track.getArtist());
            itemView.setOnClickListener(this);
            Glide.with(itemView.getContext()).load(track.getArtworkUrl())
                    .placeholder(R.drawable.back_ground_genre).into(mImageTrack);
            mTrack = track;
        }

        @Override
        public void onClick(View view) {
            mTrackListener.onTrackClick(mTrack);
        }
    }
}
