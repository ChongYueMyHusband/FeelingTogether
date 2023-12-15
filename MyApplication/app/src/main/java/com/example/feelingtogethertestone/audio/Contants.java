package com.example.feelingtogethertestone.audio;

import java.util.List;

public class Contants {
    public static String PATH_APP_DIR;
    public static String PATH_FETCH_DIR_RECORD;
    private static List<AudioBean> sAudioList;
    public static void setAudioList(List<AudioBean> audioList){
        if(audioList != null){
            Contants.sAudioList = audioList;
        }
    }
    public static List<AudioBean> getsAudioList(){
        return sAudioList;
    }

}
