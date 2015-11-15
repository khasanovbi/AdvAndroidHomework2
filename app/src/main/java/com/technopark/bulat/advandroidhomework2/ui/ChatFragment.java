package com.technopark.bulat.advandroidhomework2.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.adapters.ChannelListAdapter;
import com.technopark.bulat.advandroidhomework2.adapters.ChatAdapter;
import com.technopark.bulat.advandroidhomework2.models.Channel;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.models.Message;
import com.technopark.bulat.advandroidhomework2.socket.RequestListener;
import com.technopark.bulat.advandroidhomework2.socket.SocketRequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment implements OnClickListener, ChatAdapter.OnItemClickListener {
    private ChatAdapter mChatAdapter;
    private Channel mChannel;
    private SocketRequestTask mSocketRequestTask;
    private EditText mMessageEditText;
    private RecyclerView mChatRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mChannel = (Channel) getArguments().getSerializable(Channel.descriptionKey);
        if (mSocketRequestTask != null) {
            mSocketRequestTask.cancel(true);
        }
        mSocketRequestTask = new SocketRequestTask(new RequestListener() {
            @Override
            public void onRequestResult(String result) {
                onEnterChatResult(result);
            }

            @Override
            public void onRequestError(int errorStringID) {

            }
        });
        mSocketRequestTask.execute(prepareChatRequestString(mChannel));

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

    public static String prepareChatRequestString(Channel channel) {
        Map<String, String> data = new HashMap<>();
        data.put("cid", GlobalUserIds.getInstance().cid);
        data.put("sid", GlobalUserIds.getInstance().sid);
        data.put("channel", channel.getChid());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "enter");
            jsonObject.put("data", new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String prepareSendMessageRequestString(Channel channel, String messageText) {
        Map<String, String> data = new HashMap<>();
        data.put("cid", GlobalUserIds.getInstance().cid);
        data.put("sid", GlobalUserIds.getInstance().sid);
        data.put("channel", channel.getChid());
        data.put("body", messageText);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "message");
            jsonObject.put("data", new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public void onEnterChatResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            int status = Integer.valueOf(data.getString("status"));
            if (status != 0) {
                Toast.makeText(getActivity(), data.getString("error"), Toast.LENGTH_SHORT).show();
            } else {
                /*
                JSONArray users = data.getJSONArray("users");
                for (int i = 0; i < users.length(); ++i) {
                    JSONObject jsonUser = (JSONObject) users.get(i);
                    String uid = jsonUser.getString("uid");
                    String nickname = jsonUser.getString("nick");
                    //mChannelListAdapter.add(channel);
                }
                */
                JSONArray lastMessages = data.getJSONArray("last_msg");
                for (int i = 0; i < lastMessages.length(); ++i) {
                    Message message = new Message();
                    JSONObject jsonMessage = (JSONObject) lastMessages.get(i);
                    message.setId(jsonMessage.getString("mid"));
                    message.setAuthorId(jsonMessage.getString("from"));
                    message.setAuthorNickname(jsonMessage.getString("nick"));
                    message.setText(jsonMessage.getString("body"));
                    message.setTime(jsonMessage.getString("time"));
                    mChatAdapter.add(message);
                }
                Message message = new Message();
                message.setId("123");
                message.setAuthorId("321");
                message.setAuthorNickname("alesha");
                message.setText("Привет лунатикам!");
                message.setTime("123.123");
                mChatAdapter.add(message);
                message = new Message();
                message.setId("123");
                message.setAuthorId("123");
                message.setAuthorNickname("alesha2");
                message.setText("Привет лунатикам!2");
                message.setTime("123.123");
                mChatAdapter.add(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onMessageSentResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            int status = Integer.valueOf(data.getString("status"));
            if (status != 0) {
                Toast.makeText(getActivity(), data.getString("error"), Toast.LENGTH_SHORT).show();
            } else {
                Message mCurrentUserMessage = new Message();
                mCurrentUserMessage.setText(mMessageEditText.getText().toString());
                mCurrentUserMessage.setAuthorId(GlobalUserIds.getInstance().cid);
                mCurrentUserMessage.setAuthorNickname("Вы");
                mChatAdapter.add(mCurrentUserMessage);
                mMessageEditText.setText("");
                mChatRecyclerView.scrollToPosition(mChatAdapter.getItemCount() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:
                String messageText = mMessageEditText.getText().toString();
                if (!messageText.equals("")) {
                    if (mSocketRequestTask != null) {
                        mSocketRequestTask.cancel(true);
                    }
                    mSocketRequestTask = new SocketRequestTask(new RequestListener() {
                        @Override
                        public void onRequestResult(String result) {
                            onMessageSentResult(result);
                        }

                        @Override
                        public void onRequestError(int errorStringID) {

                        }
                    });
                    mSocketRequestTask.execute(prepareSendMessageRequestString(mChannel, messageText));
                }
                break;
        }
    }

    @Override
    public void onItemClick(ChatAdapter.MessageViewHolder item, int position) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new ContactInfoFragment()).commit();
    }
}
