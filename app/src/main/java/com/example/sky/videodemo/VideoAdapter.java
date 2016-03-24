package com.example.sky.videodemo;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sky on 2016/3/24.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private List<String> mList;
    private VideoFragment mVideoFragment;

    public VideoAdapter(List<String> list, VideoFragment videoFragment){
        mList = list;
        mVideoFragment = videoFragment;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_video, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, final int position) {
        holder.mTextView.setText(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoFragment != null && mVideoFragment.isVisible()){
                    mVideoFragment.play(mList.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class VideoHolder extends RecyclerView.ViewHolder{

        protected TextView mTextView;

        public VideoHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }
}
