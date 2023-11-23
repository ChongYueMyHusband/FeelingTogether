package com.example.feelingtogethertestone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EmotionForumActivity extends AppCompatActivity {
    private Button activeSectionBtn;    // 积极情绪分区按钮
    private Button negativeSectionBtn;    // 消极情绪分区按钮
    private Button writingPostsBtn;    // 发帖按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_forum);

        activeSectionBtn = (Button) findViewById(R.id.activeSection);
        negativeSectionBtn = (Button) findViewById(R.id.negativeSection);
        writingPostsBtn = (Button) findViewById(R.id.writingPosts);

        activeSectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(EmotionForumActivity.this, ActiveSectionActivity.class);
                startActivity(intent);
            }
        });

        negativeSectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(EmotionForumActivity.this, NegativeSectionActivity.class);
                startActivity(intent);
            }
        });

        writingPostsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(EmotionForumActivity.this, WritingPostsActivity.class);
                startActivity(intent);
            }
        });
    }
}