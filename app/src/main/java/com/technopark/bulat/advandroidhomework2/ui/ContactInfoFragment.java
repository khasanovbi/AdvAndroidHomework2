package com.technopark.bulat.advandroidhomework2.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.models.User;
import com.technopark.bulat.advandroidhomework2.network.request.messages.UserInfo;
import com.technopark.bulat.advandroidhomework2.network.response.ResponseMessage;
import com.technopark.bulat.advandroidhomework2.network.socket.GlobalSocket;
import com.technopark.bulat.advandroidhomework2.network.socket.socketObserver.Observer;

public class ContactInfoFragment extends Fragment implements Observer {
    public static final String descriptionKey = "UserInfo";
    private String mUserId;
    private TextView mNicknameTextView;
    private TextView mStatusTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUserId = getArguments().getString(descriptionKey);

        View rootView = inflater.inflate(R.layout.fragment_contact_info, container, false);

        mNicknameTextView = (TextView) rootView.findViewById(R.id.contact_info_nickname);
        mStatusTextView = (TextView) rootView.findViewById(R.id.contact_info_status);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /* Subscribe to socket messages */
        GlobalSocket.getInstance().registerObserver(this);
        GlobalSocket.getInstance().performAsyncRequest(new UserInfo(mUserId, GlobalUserIds.getInstance().cid, GlobalUserIds.getInstance().sid));
    }

    @Override
    public void onPause() {
        super.onPause();
        /* Unsubscribe from socket messages */
        GlobalSocket.getInstance().removeObserver(this);
    }

    @Override
    public void handleResponseMessage(ResponseMessage responseMessage) {
        if (responseMessage.getAction().equals("userinfo")) {
            final com.technopark.bulat.advandroidhomework2.network.response.messages.UserInfo userInfo = new com.technopark.bulat.advandroidhomework2.network.response.messages.UserInfo();
            userInfo.parse(responseMessage.getJsonData());
            if (userInfo.getStatus() == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        User user = userInfo.getUser();
                        mNicknameTextView.setText(user.getNickname());
                        mStatusTextView.setText(user.getStatus());
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getBaseContext(), userInfo.getError(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
