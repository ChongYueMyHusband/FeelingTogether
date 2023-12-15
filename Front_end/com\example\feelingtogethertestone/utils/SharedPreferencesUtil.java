package com.example.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.example.ContextApp;


public class SharedPreferencesUtil {

    private static final String NAME = "my_settings";

    private static final SharedPreferences settings =  ContextApp.getContext().getSharedPreferences(NAME, MODE_PRIVATE);


    public static void putString(String key, String val){

        // 获取SharedPreferences.Editor对象
        SharedPreferences.Editor editor = settings.edit();

        // 向SharedPreferences.Editor对象中添加数据
        editor.putString(key, val);
        // 提交数据
        editor.apply();
    }

    public static String getString(String key){
        return settings.getString(key, null);
    }


}
