package com.example.feelingtogethertestone.audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

public class AudioService extends Service implements MediaPlayer.OnCompletionListener{
    private MediaPlayer mediaPlayer = null;
    private List<AudioBean> mlist;
    private int playPosition = -1;
    public AudioService() {
    }
    public interface  OnPlayChangeListener{
        public void playChange(int changPos);
    }
    private OnPlayChangeListener onPlayChangeListener;

    public void setOnPlayChangeListener(OnPlayChangeListener onPlayChangeListener) {
        this.onPlayChangeListener = onPlayChangeListener;
    }
     public void notifyActivityRefreshUI(){
        if(onPlayChangeListener != null){
            onPlayChangeListener.playChange(playPosition);
        }
     }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    public class AudioBinder extends Binder{
        public AudioService getServer(){
            return AudioService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return new AudioBinder();
    }
    public void CutMusicOrPause(int position)  {
        int playPosition = this.playPosition;
        if(playPosition != position){
            if(playPosition != -1){
                mlist.get(playPosition).setPlaying(false);
            }
                play(position);
            return;
        }
        pauseOrContinue();
    }

    public void play(int position)  {
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
        }
        mlist = Contants.getsAudioList();
        if(mlist.size() <= 0){
            return;
        }
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        try {
            mediaPlayer.reset();
            playPosition = position;
            mediaPlayer.setDataSource(mlist.get(position).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mlist.get(position).setPlaying(true);
            notifyActivityRefreshUI();
            setFlag(true);
            upDateProgress();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void pauseOrContinue(){
        int playPosition = this.playPosition;
       AudioBean audioBean = mlist.get(playPosition);
       if(mediaPlayer.isPlaying()){
           mediaPlayer.pause();
           audioBean.setPlaying(false);
       }else{
           mediaPlayer.start();
           audioBean.setPlaying(true);
       }
       notifyActivityRefreshUI();
    }
    private boolean flag = true;
    private final int PROGRESS_ID = 1;
    private final int INIERMINATE_TIME = 1;
    public void setFlag(boolean flag){
        this.flag = flag;
    }

    public void upDateProgress(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(flag){
                   long total = mlist.get(playPosition).getDurationLong();
                  int currPosition =  mediaPlayer.getCurrentPosition();
                  int progress = (int)(currPosition*100/total);
                  mlist.get(playPosition).setPro(progress);
                  handler.sendEmptyMessageDelayed(PROGRESS_ID, INIERMINATE_TIME);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(message.what == PROGRESS_ID){
                notifyActivityRefreshUI();
            }
            return true;
        }
    });
}