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

import java.util.HashMap;
import java.util.Map;

import com.technopark.bulat.advandroidhomework2.models.Channel;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.socket.RequestListener;
import com.technopark.bulat.advandroidhomework2.socket.SocketRequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChannelListFragment extends Fragment implements RequestListener, ChannelListAdapter.OnItemClickListener {
    private RecyclerView mChannelListRecyclerView;
    private ChannelListAdapter mChannelListAdapter;
    private SocketRequestTask mSocketRequestTask;

    public ChannelListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).unsetFullScreenFlag();
        if (mSocketRequestTask != null) {
            mSocketRequestTask.cancel(true);
        }
        mSocketRequestTask = new SocketRequestTask(this);
        mSocketRequestTask.execute(prepareChannelListRequestString());

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

    public static String prepareChannelListRequestString() {
        Map<String, String> data = new HashMap<>();
        data.put("cid", GlobalUserIds.getInstance().cid);
        data.put("sid", GlobalUserIds.getInstance().sid);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "channellist");
            jsonObject.put("data", new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void onRequestResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            int status = Integer.valueOf(data.getString("status"));
            if (status != 0) {
                Toast.makeText(getActivity(), data.getString("error"), Toast.LENGTH_SHORT).show();
            } else {
                JSONArray channels = data.getJSONArray("channels");
                for (int i = 0; i < channels.length(); ++i) {
                    JSONObject jsonChannel = (JSONObject) channels.get(i);
                    Channel channel = new Channel();
                    channel.setChid(jsonChannel.getString("chid"));
                    channel.setDescription(jsonChannel.getString("descr"));
                    channel.setName(jsonChannel.getString("name"));
                    channel.setOnlineCount(jsonChannel.getInt("online"));
                    mChannelListAdapter.add(channel);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(int errorStringID) {

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
}
