package com.example.musicPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ContextApp;
import com.example.adapter.PlayListDetailAdapter;
import com.example.callback.VoidCallBack;
import com.example.datasource.MusicInfoDB;
import com.example.datasource.PlaylistDB;
import com.example.entity.MusicInfo;
import com.example.entity.PlayList;
import com.example.feelingtogethertestone.R;
import com.example.utils.DownloadPool;
import com.example.utils.ToastUtil;
import com.example.utils.XMusicPlayer;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;

import java.util.ArrayList;

public class PlayListDetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_detail);

        //设置回退行为
        TitleBar bar = findViewById(R.id.pld_titleBar);
        bar.getLeftText().setOnClickListener(v -> finish());

        //获取歌单id
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        if (id == -1) return;

        //用歌单id查询歌单
        PlayList playList = PlaylistDB.getById(id);
        String[] musicIds = PlaylistDB.getMusicInfoIdsArray(playList);
        RecyclerView recyclerView = findViewById(R.id.pld_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置adpater
        PlayListDetailAdapter adapter = new PlayListDetailAdapter(playList, musicIds);
        recyclerView.setAdapter(adapter);


        //下载所有
        ButtonView downAll = findViewById(R.id.fragment_down_btn);
        //下载所有歌曲
        downAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String musicId : musicIds) {
                    //查询音乐信息
                    MusicInfo mfo = MusicInfoDB.getById(Integer.parseInt(musicId));
                    if (mfo != null) {
                        //异步下载
                        DownloadPool.download(mfo, new VoidCallBack<MusicInfo>() {
                            @Override
                            public void onSucceed(MusicInfo musicInfo) {
                                //下载成功回调
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.toast(musicInfo.getTitle() + "下载成功");
                                    }
                                });
                            }
                            @Override
                            public void onFail() {
                                //下载失败回调
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.toast(mfo.getTitle() + "下载失败");
                                    }
                                });

                            }
                        });
                    }
                }
            }
        });

        //播放所有
        ButtonView playAll = findViewById(R.id.fragment_play_btn);
        playAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XMusicPlayer player = ContextApp.getPlayer();
                //清空播放列表
                player.clearPlayerList();
                //获取播放列表
                ArrayList<MusicInfo> list = new ArrayList<>();
                for (String musicId : musicIds) {
                    //查询播放信息
                    MusicInfo info = MusicInfoDB.getById(Integer.parseInt(musicId));
                    list.add(info);
                }
                //设置播放列表
                player.setPlayList(list);
                //播放第一首
                player.play(0);
            }
        });
    }
}