package com.example.feelingtogethertestone.audio;

import android.media.MediaMetadataRetriever;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioInfoUtil {
    private MediaMetadataRetriever mediaMetadataRetriever;
    private  AudioInfoUtil(){}
    private static AudioInfoUtil utils;
    public static AudioInfoUtil getInstance(){
        if(utils == null){
            synchronized (AudioInfoUtil.class){
                if(utils == null){
                    utils = new AudioInfoUtil();
                }
            }
        }
        return utils;
    }
    public long getAudioFileDuration(String filePath){
        long duration = 0;
        if(mediaMetadataRetriever == null){
            mediaMetadataRetriever = new MediaMetadataRetriever();
        }
        mediaMetadataRetriever.setDataSource(filePath);
        String s = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        duration = Long.parseLong(s);
        return duration;
    }
    public  String getAudioFileFormatDuration(String format, long durlong){
        durlong -= 8*3600*1000;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
       return sdf.format(new Date(durlong));
    }
    public String getAudioFileFormatDuration(long durlong){
        return getAudioFileFormatDuration("HH:mm:ss",durlong);
    }
    public String getAudioFileArtist(String filepath){
        if(mediaMetadataRetriever == null){
            mediaMetadataRetriever = new MediaMetadataRetriever();
        }
        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        return artist;
    }
    public void releaseRetriever() throws IOException {
        if(mediaMetadataRetriever != null){
            mediaMetadataRetriever.release();
            mediaMetadataRetriever = null;
        }
    }

}
