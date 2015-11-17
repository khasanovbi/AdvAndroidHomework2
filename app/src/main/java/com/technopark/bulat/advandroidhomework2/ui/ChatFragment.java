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
import com.technopark.bulat.advandroidhomework2.adapters.ChatAdapter;
import com.technopark.bulat.advandroidhomework2.models.Channel;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.models.Message;

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
    private EditText mMessageEditText;
    private RecyclerView mChatRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mChannel = (Channel) getArguments().getSerializable(Channel.descriptionKey);
        //mRequestTask.execute(prepareChatRequestString(mChannel));

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

    public static String prepareSendMessageRequestString(Channel channel, String messageText) {
        Map<String, String> data = new HashMap<>();
        data.put("cid", GlobalUserIds.getInstance().cid);
        data.put("sid", GlobalUserIds.getInstance().sid);
        data.put("channel", channel.getId());
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
                    //mRequestTask.execute(prepareSendMessageRequestString(mChannel, messageText));
                }
                break;
        }
    }

    @Override
    public void onItemClick(ChatAdapter.MessageViewHolder item, int position) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new ContactInfoFragment()).commit();
    }
}
