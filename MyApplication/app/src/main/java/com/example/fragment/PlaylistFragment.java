package com.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.PlaylistAdapter;
import com.example.datasource.PlaylistDB;
import com.example.feelingtogethertestone.R;


public class PlaylistFragment extends Fragment {
    private RecyclerView recyclerView;

    @Override
    public void onStart() {
        super.onStart();
        // 选择你要的布局方式，这里以LinearLayout为例
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        // 将布局管理器设置到RecyclerView上
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new PlaylistAdapter(PlaylistDB.selectList()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_playlist, container, false);
        recyclerView = fragment.findViewById(R.id.playlist);
        return fragment;
    }


}