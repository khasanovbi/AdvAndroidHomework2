package com.technopark.bulat.advandroidhomework2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technopark.bulat.advandroidhomework2.R;

import java.util.ArrayList;
import java.util.List;

import com.technopark.bulat.advandroidhomework2.models.Channel;

/**
 * Created by bulat on 07.11.15.
 */
public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ChannelViewHolder> {
    private List<Channel> channelList;
    private OnItemClickListener onItemClickListener;

    public ChannelListAdapter() {
        channelList = new ArrayList<>();
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel, parent, false);
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        Channel channel = channelList.get(position);
        holder.mName.setText(channel.getName() + " (" + String.valueOf(channel.getOnlineCount()) + ")");
        holder.mDescription.setText(channel.getDescription());
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    public void add(Channel channel) {
        channelList.add(channel);
        notifyItemInserted(getItemCount());
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ChannelViewHolder item, int position);
    }

    public class ChannelViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private TextView mName;
        private TextView mDescription;

        public ChannelViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mName = (TextView) itemView.findViewById(R.id.channel_name);
            mDescription = (TextView) itemView.findViewById(R.id.channel_description);
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition());
            }
        }
    }
}
