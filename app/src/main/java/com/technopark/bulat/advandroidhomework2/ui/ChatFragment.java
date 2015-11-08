package com.technopark.bulat.advandroidhomework2.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.adapters.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

import models.Message;
import models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    RecyclerView mChatRecyclerView;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        User user1 = new User("Собеседник №1", "email", "статус", "изображение");
        User user2 = new User("Собеседник №2", "email", "статус", "изображение");
        List<Message> list = new ArrayList<>();
        list.add(new Message("Текст, написанный собеседником №1", user1));
        list.add(new Message("Текст, написанный собеседником №2", user2));

        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        mChatRecyclerView = (RecyclerView) rootView.findViewById(R.id.chat_recycler_view);
        ChatAdapter chatAdapter = new ChatAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        mChatRecyclerView.setAdapter(chatAdapter);
        mChatRecyclerView.setLayoutManager(linearLayoutManager);
        mChatRecyclerView.setItemAnimator(itemAnimator);
        return rootView;
    }


}
