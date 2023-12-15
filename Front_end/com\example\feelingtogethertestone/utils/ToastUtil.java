package com.example.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.ContextApp;


public class ToastUtil {

    public static void toast(String text){
        Toast.makeText(ContextApp.getContext(), text, Toast.LENGTH_SHORT).show();
    }
    public static void toast(String text, Context context){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
