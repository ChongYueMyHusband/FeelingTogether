package com.example.feelingtogethertestone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chat.ChatActivity;
import com.example.fourm.FourmmainActivity;

public class HomeActivity extends AppCompatActivity {
    // 还需要实现 今日鸡汤、动态日历

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button emotionDiary = (Button) findViewById(R.id.EmotionDiary);
        Button emotionForum = (Button) findViewById(R.id.EmotionForum);
        Button chattingRobot = (Button) findViewById(R.id.ChattingRobot);
        Button userInfo = (Button) findViewById(R.id.UserInfo);

        emotionDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(HomeActivity.this,EmotionDiaryActivity.class);
                startActivity(intent);
            }
        });

        emotionForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(HomeActivity.this, FourmmainActivity.class);
                startActivity(intent);
            }
        });

        chattingRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(HomeActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        });

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(HomeActivity.this,PersonalInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}