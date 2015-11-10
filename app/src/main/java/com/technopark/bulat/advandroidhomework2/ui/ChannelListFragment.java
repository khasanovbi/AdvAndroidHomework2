package com.technopark.bulat.advandroidhomework2.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.technopark.bulat.advandroidhomework2.R;
import com.technopark.bulat.advandroidhomework2.adapters.ChannelListAdapter;

import java.util.ArrayList;
import java.util.List;

import models.Channel;

public class ChannelListFragment extends Fragment {
    private RecyclerView mChannelListRecyclerView;

    public ChannelListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        List<Channel> list = new ArrayList<>();
        list.add(new Channel("Изображение", "Имя чата", "Краткое описание", 123));
        list.add(new Channel("Изображение", "Кроватка", "Создано Подстрешным и Кириенко в 1996 году", 10));
        list.add(new Channel("Изображение", "Гласчат", "Дорохов был тут", 2));
        list.add(new Channel("Изображение", "Москва", "Нерезиновый чат", 24000000));
        list.add(new Channel("Изображение", "Имя чата", "Краткое описание", 123));
        list.add(new Channel("Изображение", "Имя чата", "Краткое описание", 123));

        View rootView = inflater.inflate(R.layout.fragment_channel_list, container, false);
        mChannelListRecyclerView = (RecyclerView) rootView.findViewById(R.id.channel_list_recycler_view);
        ChannelListAdapter channelListAdapter = new ChannelListAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        mChannelListRecyclerView.setAdapter(channelListAdapter);
        mChannelListRecyclerView.setLayoutManager(linearLayoutManager);
        mChannelListRecyclerView.setItemAnimator(itemAnimator);
        return rootView;
    }


}
