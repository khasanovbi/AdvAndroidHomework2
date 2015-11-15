package com.technopark.bulat.advandroidhomework2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.models.GlobalUserIds;
import com.technopark.bulat.advandroidhomework2.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bulat on 08.11.15.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private List<Message> messages;
    private final static int ANOTHER_USER = 0, CURRENT_USER = 1;
    private OnItemClickListener onItemClickListener;

    public ChatAdapter() {
        messages = new ArrayList<>();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ANOTHER_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_another_user, parent, false);
                break;
            case CURRENT_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_current_user, parent, false);
                break;
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.mMessageText.setText(message.getText());
        holder.mMessageAuthor.setText(message.getAuthorNickname());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getAuthorId().equals(GlobalUserIds.getInstance().cid)) {
            return CURRENT_USER;
        } else
            return ANOTHER_USER;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(MessageViewHolder item, int position);
    }

    public void add(Message message) {
        messages.add(message);
        notifyItemInserted(getItemCount());
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mMessageText;
        private TextView mMessageAuthor;
        public ImageView mAuthorImage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mMessageText = (TextView) itemView.findViewById(R.id.message_text);
            mMessageAuthor = (TextView) itemView.findViewById(R.id.message_author);
            mAuthorImage = (ImageView) itemView.findViewById(R.id.author_image);
            mAuthorImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition());
            }
        }
    }
}
