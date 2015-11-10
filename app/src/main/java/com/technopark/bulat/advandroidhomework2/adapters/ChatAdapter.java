package com.technopark.bulat.advandroidhomework2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.technopark.bulat.advandroidhomework2.R;

import java.util.List;

import models.Message;

/**
 * Created by bulat on 08.11.15.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private List<Message> messages;

    public ChatAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.mMessageText.setText(message.getText());
        holder.mMessageAuthor.setText(message.getAuthor().getNickName());
        // TODO add image
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView mMessageText;
        private TextView mMessageAuthor;
        private ImageView mAuthorImage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mMessageText = (TextView) itemView.findViewById(R.id.message_text);
            mMessageAuthor = (TextView) itemView.findViewById(R.id.message_author);
            mAuthorImage = (ImageView) itemView.findViewById(R.id.author_image);
        }
    }
}
