package com.example.feelingtogethertestone.audio;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    private static PermissionUtil permissionUtil;
    private PermissionUtil(){}
    private final int Code = 100;


    public static PermissionUtil getInstance(){
        if(permissionUtil == null){
            synchronized (PermissionUtil.class){
                if(permissionUtil == null){
                    permissionUtil = new PermissionUtil();
                }
            }
        }
        return permissionUtil;
    }
    public void onRequestPermission(Activity context, String []permission){

        if(Build.VERSION.SDK_INT >= 23){
            List<String> list = new ArrayList<>();
            for(int i = 0; i < permission.length; i++){
               int res = ContextCompat.checkSelfPermission(context, permission[i]);
               if(res != PackageManager.PERMISSION_GRANTED){
                   list.add(permission[i]);
               }
            }
            if(list.size() > 0){
                String[] permission_arr = list.toArray(new String[list.size()]);
                ActivityCompat.requestPermissions(context, permission_arr,Code);
            }
        }
    }

public void createAppDir(){
       File recorderDir = SDCardUtil.getInstance().createAppFetchDir(IFileInter.FETCH_DIR_AUDIO);
        Contants.PATH_FETCH_DIR_RECORD = recorderDir.getAbsolutePath();
}

}
