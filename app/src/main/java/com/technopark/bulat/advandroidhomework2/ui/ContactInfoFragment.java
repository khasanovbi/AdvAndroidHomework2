package com.technopark.bulat.advandroidhomework2.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.models.User;
import com.technopark.bulat.advandroidhomework2.network.request.messages.UserInfoRequest;
import com.technopark.bulat.advandroidhomework2.network.response.RawResponse;
import com.technopark.bulat.advandroidhomework2.network.response.messages.UserInfoResponse;
import com.technopark.bulat.advandroidhomework2.network.socket.GlobalSocket;
import com.technopark.bulat.advandroidhomework2.network.socket.socketObserver.Observer;

public class ContactInfoFragment extends Fragment implements Observer {
    public static final String descriptionKey = "UserInfo";
    private String mUserId;
    private TextView mNicknameTextView;
    private TextView mStatusTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUserId = getArguments().getString(descriptionKey);

        prepareView();

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
        GlobalSocket.getInstance().performAsyncRequest(new UserInfoRequest(mUserId, GlobalUserIds.getInstance().cid, GlobalUserIds.getInstance().sid));
    }

    @Override
    public void onPause() {
        super.onPause();
        /* Unsubscribe from socket messages */
        GlobalSocket.getInstance().removeObserver(this);
    }

    @Override
    public void handleResponseMessage(RawResponse rawResponse) {
        if (rawResponse.getAction().equals("userinfo")) {
            final UserInfoResponse userInfoResponse = new UserInfoResponse(rawResponse.getJsonData());
            if (userInfoResponse.getStatus() == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        User user = userInfoResponse.getUser();
                        mNicknameTextView.setText(user.getNickname());
                        mStatusTextView.setText(user.getStatus());
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getBaseContext(), userInfoResponse.getError(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void prepareView() {
        MainActivity mainActivity = (MainActivity) getActivity();
        ActionBar actionBar = mainActivity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.info);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white_24dp);
        actionBar.setIcon(R.drawable.ic_person_white_24dp);
    }
}
