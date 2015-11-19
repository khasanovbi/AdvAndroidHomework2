package com.technopark.bulat.advandroidhomework2.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.adapters.ChannelListAdapter;
import com.technopark.bulat.advandroidhomework2.models.Channel;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.network.request.messages.ChannelList;
import com.technopark.bulat.advandroidhomework2.network.response.RawResponse;
import com.technopark.bulat.advandroidhomework2.network.response.messages.ChannelListResponse;
import com.technopark.bulat.advandroidhomework2.network.socket.GlobalSocket;
import com.technopark.bulat.advandroidhomework2.network.socket.socketObserver.Observer;

public class ChannelListFragment extends Fragment implements ChannelListAdapter.OnItemClickListener, Observer {
    private ChannelListAdapter mChannelListAdapter;

    public ChannelListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.unsetFullScreenFlag();
        prepareActionBar();
        View rootView = inflater.inflate(R.layout.fragment_channel_list, container, false);
        RecyclerView mChannelListRecyclerView = (RecyclerView) rootView.findViewById(R.id.channel_list_recycler_view);
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
        Fragment chatFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        if (chatFragment == null) {
            chatFragment = new ChatFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Channel.descriptionKey, channel);
            chatFragment.setArguments(bundle);
        }
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragments_container, chatFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("TODO", "Drawer open");
                // TODO Drawer open
                break;
            case R.id.add_channel_button:
                Log.d("TODO", "Channel add popup");
                // TODO Channel add popup
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        /* Subscribe to socket messages */
        GlobalSocket.getInstance().registerObserver(this);
        GlobalSocket.getInstance().performAsyncRequest(new ChannelList(GlobalUserIds.getInstance().cid, GlobalUserIds.getInstance().sid));
    }

    @Override
    public void onPause() {
        super.onPause();
        /* Unsubscribe from socket messages */
        GlobalSocket.getInstance().removeObserver(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_channel_list, menu);
    }

    @Override
    public void handleResponseMessage(RawResponse rawResponse) {
        if (rawResponse.getAction().equals("channellist")) {
            final ChannelListResponse channelList = new ChannelListResponse(rawResponse.getJsonData());
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

    private void prepareActionBar() {
        MainActivity mainActivity = (MainActivity) getActivity();
        ActionBar actionBar = mainActivity.getmActionBar();
        actionBar.setTitle("Cписок чатов");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setIcon(R.drawable.ic_chat_white_24dp);
    }
}
