package com.technopark.bulat.advandroidhomework2.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog.Builder;
import android.view.View;
import android.widget.EditText;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.network.request.messages.CreateChannelRequest;
import com.technopark.bulat.advandroidhomework2.network.socket.GlobalSocket;

public class ChannelAddDialogFragment extends DialogFragment implements OnClickListener {
    private EditText mChannelNameEditText;
    private EditText mChannelDescriptionEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_channel_add_dialog, null);
        mChannelNameEditText = (EditText) view.findViewById(R.id.channel_name);
        mChannelDescriptionEditText = (EditText) view.findViewById(R.id.channel_description);

        Builder alertDialogBuilder = new Builder(getActivity())
                .setTitle("Новый канал")
                .setPositiveButton(R.string.ok_button, this)
                .setNegativeButton(R.string.cancel_button, this)
                .setView(view);
        return alertDialogBuilder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                String name = mChannelNameEditText.getText().toString();
                if (name.length() != 0) {
                    String description = mChannelDescriptionEditText.getText().toString();
                    GlobalSocket.getInstance().performAsyncRequest(new CreateChannelRequest(GlobalUserIds.getInstance().cid, GlobalUserIds.getInstance().sid, name, description));
                }
                break;
        }
    }
}
