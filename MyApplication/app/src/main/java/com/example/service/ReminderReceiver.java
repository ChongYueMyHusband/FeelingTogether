package com.example.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.ContextApp;
import com.example.fragment.SettingFragment;
import com.example.utils.SharedPreferencesUtil;


public class ReminderReceiver extends BroadcastReceiver {

    @Override  
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "暂停播放", Toast.LENGTH_SHORT).show();
        ContextApp.getPlayer().pause();
        ReminderService.setPendingIntent(null);
        SharedPreferencesUtil.putString(SettingFragment.CLOSE_TIME, null);
    }

}