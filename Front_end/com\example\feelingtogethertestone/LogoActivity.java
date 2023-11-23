package com.example.feelingtogethertestone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LogoActivity extends AppCompatActivity {

    private static final long LOGO_DISPLAY_TIME = 5000; // 5秒
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 跳转到登陆界面
                Intent loginIntent = new Intent(LogoActivity.this, MainActivity.class);
                startActivity(loginIntent);
                finish(); // 结束 LogoActivity，以防返回时再次显示 LOGO
            }
        }, LOGO_DISPLAY_TIME);
    }
}