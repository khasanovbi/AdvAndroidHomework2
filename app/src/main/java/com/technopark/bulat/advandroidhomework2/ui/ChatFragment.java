package com.technopark.bulat.advandroidhomework2.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.adapters.ChatAdapter;
import com.technopark.bulat.advandroidhomework2.models.Channel;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.network.request.messages.EnterChat;
import com.technopark.bulat.advandroidhomework2.network.request.messages.SendMessage;
import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;
import com.technopark.bulat.advandroidhomework2.network.response.events.MessageEvent;
import com.technopark.bulat.advandroidhomework2.network.socket.GlobalSocket;
import com.technopark.bulat.advandroidhomework2.network.socket.socketObserver.Observer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment implements OnClickListener, ChatAdapter.OnItemClickListener, Observer {
    private static final String LOG_TAG = "ChatFragment";
    private ChatAdapter mChatAdapter;
    private Channel mChannel;
    private EditText mMessageEditText;
    private RecyclerView mChatRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mChannel = (Channel) getArguments().getSerializable(Channel.descriptionKey);

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        mChatRecyclerView = (RecyclerView) rootView.findViewById(R.id.chat_recycler_view);
        mChatAdapter = new ChatAdapter();
        mChatAdapter.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        mChatRecyclerView.setAdapter(mChatAdapter);
        mChatRecyclerView.setLayoutManager(linearLayoutManager);
        mChatRecyclerView.setItemAnimator(itemAnimator);

        mMessageEditText = (EditText) rootView.findViewById(R.id.message_text);
        rootView.findViewById(R.id.send_button).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /* Subscribe to socket messages */
        GlobalSocket.getInstance().registerObserver(this);
        GlobalSocket.getInstance().performAsyncRequest(new EnterChat(GlobalUserIds.getInstance().cid, GlobalUserIds.getInstance().sid, mChannel.getId()));
    }

    @Override
    public void onPause() {
        super.onPause();
        /* Unsubscribe from socket messages */
        GlobalSocket.getInstance().removeObserver(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                String messageText = mMessageEditText.getText().toString();
                if (!messageText.equals("")) {
                    GlobalSocket.getInstance().performAsyncRequest(new SendMessage(GlobalUserIds.getInstance().cid, GlobalUserIds.getInstance().sid, mChannel.getId(), messageText));
                }
                break;
        }
    }

    @Override
    public void onItemClick(ChatAdapter.MessageViewHolder item, int position) {
        ContactInfoFragment contactInfoFragment = new ContactInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ContactInfoFragment.descriptionKey, mChatAdapter.getMessages().get(position).getAuthorId());
        contactInfoFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragments_container, contactInfoFragment).commit();
    }

    @Override
    public void handleResponseMessage(ResponseMessage responseMessage) {
        String action = responseMessage.getAction();
        if (action.equals("enter")) {
            com.technopark.bulat.advandroidhomework2.network.response.messages.EnterChat enterChat = new com.technopark.bulat.advandroidhomework2.network.response.messages.EnterChat();
            enterChat.parse(responseMessage.getJsonData());

        } else {
            if (action.equals("ev_message")) {
                final MessageEvent messageEvent = new MessageEvent();
                messageEvent.parse(responseMessage.getJsonData());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        mChatAdapter.add(messageEvent.getMessage());
                        mMessageEditText.setText("");
                        mChatRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
                    }
                });
            }
        }
    }
}