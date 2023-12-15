package com.example.feelingtogethertestone.audio;

import android.os.Environment;

import java.io.File;

public class SDCardUtil {
    private SDCardUtil(){}
    private static SDCardUtil  sdCardUtil;
    public static SDCardUtil getInstance(){
        if(sdCardUtil == null){
            synchronized (SDCardUtil.class){
                if(sdCardUtil == null){
                    sdCardUtil = new SDCardUtil();
                }
            }
        }
        return sdCardUtil;
    }
    /*public boolean isHaveSDCard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }*/
    /*
    创建公共目录
     */
    public File createAppPublicDir(){
        //if(isHaveSDCard()){
            File sdDir = Environment.getExternalStorageDirectory();
            File appDir = new File(sdDir, IFileInter.APP_DIR);
            if(!appDir.exists()){
                appDir.mkdir();
            }
            Contants.PATH_APP_DIR = appDir.getAbsolutePath();
            return appDir;
        //}
        //return null;
    }
    /*
    创建分支目录
     */
    public File createAppFetchDir(String dir){
        File publicDir = createAppPublicDir();
        if(publicDir != null){
            File fetchDir = new File(publicDir, dir);
            if(!fetchDir.exists()){
                fetchDir.mkdir();
            }
            return fetchDir;
        }
        return null;
    }
}
