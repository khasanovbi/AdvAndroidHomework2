package com.technopark.bulat.advandroidhomework2.adapters;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.technopark.bulat.advandroidhomework2.R;

import java.util.List;

import models.Channel;

/**
 * Created by bulat on 07.11.15.
 */
public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ChannelViewHolder> {
    private List<Channel> channelList;

    public ChannelListAdapter(List<Channel> channelList) {
        this.channelList = channelList;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel, parent, false);
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        Channel channel = channelList.get(position);
        StringBuilder stringBuilder = new StringBuilder(channel.getName());
        stringBuilder.append(" (").append(String.valueOf(channel.getOnlineCount())).append(")");
        holder.mName.setText(stringBuilder.toString());
        holder.mDescription.setText(channel.getDescription());
        // TODO image
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mDescription;
        private ImageView mImage;
        public ChannelViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.channel_name);
            mDescription = (TextView) itemView.findViewById(R.id.channel_description);
        }
    }
}
