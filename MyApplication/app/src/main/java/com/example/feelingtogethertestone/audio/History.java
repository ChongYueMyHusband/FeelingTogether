package com.example.feelingtogethertestone.audio;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feelingtogethertestone.databinding.ActivityHistoryBinding;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class History extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private List<AudioBean> mDates;
    private  HistoryAdapter adapter;
    private AudioService audioService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioService.AudioBinder audioBinder = (AudioService.AudioBinder) iBinder;
            audioService = audioBinder.getServer();
            audioService.setOnPlayChangeListener(playChangeListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    AudioService.OnPlayChangeListener playChangeListener = new AudioService.OnPlayChangeListener() {
        @Override
        public void playChange(int changPos) {
            adapter.notifyDataSetChanged();
        }
    };
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(this, AudioService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        mDates = new ArrayList<>();
        adapter = new HistoryAdapter(this, mDates);
        binding.audioLv.setAdapter(adapter);
        Contants.setAudioList(mDates);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                loadDates();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        setEvent();
    }
    protected void onDestroy() {

        super.onDestroy();
        unbindService(connection);
    }

    private void setEvent(){
        adapter.setOnItemPlayClickListener(playClickListener);
        adapter.setOnItemDeClickListener(playDeListener);
        adapter.setOnItemReClickListener(playReListener);
    }
    HistoryAdapter.OnItemPlayClickListener playClickListener = new HistoryAdapter.OnItemPlayClickListener() {
        @Override
        public void onItemPlayClick(HistoryAdapter adapter, View converView, View playview, int position) {
         for(int i = 0; i < mDates.size(); i++){
             if(i == position){
                 continue;
             }
             AudioBean bean = mDates.get(i);
             bean.setPlaying(false);
         }
       boolean playing = mDates.get(position).isPlaying();
         mDates.get(position).setPlaying(!playing);
         adapter.notifyDataSetChanged();
         audioService.CutMusicOrPause(position);

        }
    };
    HistoryAdapter.OnItemDeClickListener playDeListener = new HistoryAdapter.OnItemDeClickListener() {
        @Override
        public void onItemDeClick(HistoryAdapter adapter, View converView, View playview, int position) {
              AudioBean bean = mDates.get(position);
               String title = bean.getTitle();
              String path = bean.getPath();
              File file = new File(path);
              file.getAbsoluteFile().delete();
              mDates.remove(bean);
             adapter.notifyDataSetChanged();
        }
    };
    HistoryAdapter.OnItemReClickListener playReListener = new HistoryAdapter.OnItemReClickListener() {
        @Override
        public void onItemReClick(HistoryAdapter adapter, View converView, View playview, int position) {
                showDia(position);
        }
    };
    private void showDia(int position){
        RenameDia dia = new RenameDia(this);
        AudioBean bean = mDates.get(position);
        String title = bean.getTitle();
        dia.show();
        dia.setDialogWidth();
        dia.setTipText(title);
        dia.setOnEnsureListener(new RenameDia.OnEnsureListener() {
            @Override
            public void onEnsure(String msg) {
                String path = bean.getPath();
                if(bean.getTitle().equals(msg)){
                    return;
                }
                String fileSuffix = bean.getFileSuffix();
                File file = new File(path);
                String destPath = file.getParent() + File.separator+msg+fileSuffix;
                File destFile = new File(destPath);
                file.renameTo(destFile);
                bean.setTitle(msg);
                bean.setPath(destPath);
                adapter.notifyDataSetChanged();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadDates() throws IOException {
        String wavFilePath = this.getExternalFilesDir(Environment.DIRECTORY_PODCASTS).getAbsolutePath();
        File fetchFile = new File(wavFilePath);
        File[] listFiles = fetchFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if(new File(file,s).isDirectory()){
                  return false;
                }
               if(s.endsWith("wav") || s.endsWith("mp3")){
                   return true;
               };
                return false;
            }
        });
        java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
           AudioInfoUtil audioInfoUtil = AudioInfoUtil.getInstance();

        for(int i = 0; i < listFiles.length; i++) {
            File audioFile = listFiles[i];
            String fname = audioFile.getName();
            String title = fname.substring(0, fname.lastIndexOf("."));
            String suffix = fname.substring(fname.lastIndexOf("."));
            long flastMod = audioFile.lastModified();
           //String time = sdf.format(flastMod);
            String time = sdf1.format(date1);
          long flength = audioFile.length();
          String audioPath = audioFile.getAbsolutePath();
        long duration = audioInfoUtil.getAudioFileDuration(audioPath);
       String formatDuration = audioInfoUtil.getAudioFileFormatDuration(duration);
       AudioBean ab =  new AudioBean(i + "", title, time, formatDuration,audioPath,
                 duration, flastMod, suffix, flength);
       mDates.add(ab);
        }
        audioInfoUtil.releaseRetriever();
        Collections.sort(mDates,new Comparator<AudioBean>(){

            @Override
            public int compare(AudioBean audioBean, AudioBean t1) {
                if(audioBean.getLastModified() < t1.getLastModified()){
                    return 1;
                }else if(audioBean.getLastModified() == t1.getLastModified()){
                    return 0;
                }
                return -1;
            }
        });
        adapter.notifyDataSetChanged();

    }

}
