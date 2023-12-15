package com.example.feelingtogethertestone.audio;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feelingtogethertestone.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioUtil extends AppCompatActivity{
    private  AudioUtil(){}
    private static AudioUtil utils;
    public static AudioUtil getInstance(){
        if(utils == null){
            synchronized (AudioInfoUtil.class){
                if(utils == null){
                    utils = new AudioUtil();
                }
            }
        }
        return utils;
    }
    private AudioRecord audioRecord;
    // 采样率，现在能够保证在所有设备上使用的采样率是44100Hz, 但是其他的采样率（22050, 16000, 11025）在一些设备上也可以使用。
    public static final int SAMPLE_RATE_INHZ = 44100;

    // 声道数。CHANNEL_IN_MONO and CHANNEL_IN_STEREO. 其中CHANNEL_IN_MONO是可以保证在所有设备能够使用的。
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;

    // 返回的音频数据的格式。 ENCODING_PCM_8BIT, ENCODING_PCM_16BIT, and ENCODING_PCM_FLOAT.
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    /**
     * 录音的工作线程
     */
    private Thread recordingAudioThread;
    private boolean isRecording = false;//mark if is recording
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String wavFilePath;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtil.getInstance().onRequestPermission(this, permissions);
        PermissionUtil.getInstance().createAppDir();
    }

   /* public void onClick(View v) {
        // 单击录音按钮
        if (v.getId() == R.id.record) {
            Toast.makeText(com.example.luyin.AudioUtil.this, "开始录音",Toast.LENGTH_LONG).show();
            startRecordAudio();
        } else if (v.getId() == R.id.stop) {  // 单击停止按钮
            stopRecordAudio();
            change();
        }else if(v.getId() == R.id.show){
            playRecordAudio();
        }else if(v.getId() == R.id.history){
            Intent intent = new Intent();
            intent.setClass(com.example.luyin.AudioUtil.this, History.class);
            startActivity(intent);
        }
    }*/
    public void change(){
        wavFilePath = this.getExternalFilesDir(Environment.DIRECTORY_PODCASTS) + "/wav_" + System.currentTimeMillis() + ".wav";
        PcmToWavUtil ptwUtil = new PcmToWavUtil();
        ptwUtil.pcmToWav(this.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + "jerboa_audio_cache.pcm",wavFilePath,true);
        Log.i(TAG, "audio cache wav file path:" + wavFilePath);
    }
    /**
     * 开始录音，返回临时缓存文件（.pcm）的文件路径
     */
    public String startRecordAudio() {
        String audioCacheFilePath = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + "jerboa_audio_cache.pcm";
        try{
            // 获取最小录音缓存大小，
            int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
            this.audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT, minBufferSize);


            // 开始录音
            this.isRecording = true;
            audioRecord.startRecording();

            // 创建数据流，将缓存导入数据流
            this.recordingAudioThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(audioCacheFilePath);
                    Log.i(TAG, "audio cache pcm file path:" + audioCacheFilePath);


                    try {
                        if(!file.exists()){
                            file.createNewFile();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.e(TAG, "临时缓存文件未找到");
                    }
                    if (fos == null) {
                        return;
                    }

                    byte[] data = new byte[minBufferSize];
                    int read;
                    if (fos != null) {
                        while (isRecording && !recordingAudioThread.isInterrupted()) {
                            read = audioRecord.read(data, 0, minBufferSize);
                            if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                                try {
                                    fos.write(data);
                                    Log.i("audioRecordTest", "写录音数据->" + read);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    try {
                        // 关闭数据流
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.recordingAudioThread.start();
        }
        catch(IllegalStateException e){
            Log.w(TAG,"需要获取录音权限！");
            this.checkIfNeedRequestRunningPermission();
        }
        catch(SecurityException e){
            Log.w(TAG,"需要获取录音权限！");
            this.checkIfNeedRequestRunningPermission();
        }
        return audioCacheFilePath;
    }

    private void checkIfNeedRequestRunningPermission() {
    }
    /**
     * 停止录音
     */
    public void stopRecordAudio(){
        try {
            this.isRecording = false;
            if (this.audioRecord != null) {
                this.audioRecord.stop();
                this.audioRecord.release();
                this.audioRecord = null;
                this.recordingAudioThread.interrupt();
                this.recordingAudioThread = null;
            }
        }
        catch (Exception e){
            Log.w(TAG,e.getLocalizedMessage());
        }
    }

    /**
     * 播放录音
     */
    public void playRecordAudio(){

        playWavWithMediaPlayer(wavFilePath);
    }
    /**
     * 使用MediaPlayer播放文件
     * @param filePath
     */
    protected void playWavWithMediaPlayer(String filePath){
        //File wavFile = new File(filePath);
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch(Exception e){

        }
    }
}












