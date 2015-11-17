package com.technopark.bulat.advandroidhomework2.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.adapters.ChannelListAdapter;
import com.technopark.bulat.advandroidhomework2.models.Channel;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.network.request.messages.ChannelList;
import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;
import com.technopark.bulat.advandroidhomework2.network.socket.GlobalSocket;
import com.technopark.bulat.advandroidhomework2.network.socket.socketObserver.Observer;

public class ChannelListFragment extends Fragment implements ChannelListAdapter.OnItemClickListener, Observer {
    private RecyclerView mChannelListRecyclerView;
    private ChannelListAdapter mChannelListAdapter;

    public ChannelListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).unsetFullScreenFlag();
        GlobalSocket.getInstance().registerObserver(this);
        GlobalSocket.getInstance().performAsyncRequest(new ChannelList(GlobalUserIds.getInstance().cid, GlobalUserIds.getInstance().sid));
        View rootView = inflater.inflate(R.layout.fragment_channel_list, container, false);
        mChannelListRecyclerView = (RecyclerView) rootView.findViewById(R.id.channel_list_recycler_view);
        mChannelListAdapter = new ChannelListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        mChannelListAdapter.setOnItemClickListener(this);
        mChannelListRecyclerView.setAdapter(mChannelListAdapter);
        mChannelListRecyclerView.setLayoutManager(linearLayoutManager);
        mChannelListRecyclerView.setItemAnimator(itemAnimator);
        return rootView;
    }

    @Override
    public void onItemClick(ChannelListAdapter.ChannelViewHolder item, int position) {
        Channel channel = mChannelListAdapter.getChannelList().get(position);
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Channel.descriptionKey, channel);
        chatFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, chatFragment).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void handleResponseMessage(ResponseMessage responseMessage) {
        if (responseMessage.getAction().equals("channellist")) {
            final com.technopark.bulat.advandroidhomework2.network.response.messages.ChannelList channelList = new com.technopark.bulat.advandroidhomework2.network.response.messages.ChannelList();
            channelList.parse(responseMessage.getJsonData());
            int status = channelList.getStatus();
            if (status == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        for (Channel channel : channelList.getChannels()) {
                            mChannelListAdapter.add(channel);
                        }
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getBaseContext(), channelList.getError(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
