package com.example.feelingtogethertestone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calendar.CalendarActivity;
import com.example.chat.ChatActivity;
import com.example.fourm.CentralActivity;
import com.example.fourm.FourmmainActivity;
import com.example.musicPlayer.MusicPlayerActivity;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private String imageString = "null123"; // 头像的string表达

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button emotionDiary = (Button) findViewById(R.id.EmotionDiary);
        Button emotionForum = (Button) findViewById(R.id.EmotionForum);
        Button chattingRobot = (Button) findViewById(R.id.ChattingRobot);
        Button userInfo = (Button) findViewById(R.id.ProfilePhoto);
        Button player = (Button)findViewById(R.id.Player);
        TextView textView = (TextView) findViewById(R.id.UserInfo);
        Button calendar = (Button) findViewById(R.id.Calendar);

        // 更新个人信息
        sp = getSharedPreferences("user_info", 0);
        String username = sp.getString("username", "");
        String userIdiograph = sp.getString("userIdiograph", "");

        textView.setText("  UserName: " + username + "\n  Idiograph: " + userIdiograph);
        textView.setTextSize(20);
        // 更新个人头像
        String savedPhoto = sp.getString("headPortraitBase64Code", "");
        imageString = savedPhoto;
        // 将 Base64 编码的字符串解码为字节数组
        byte[] imageBytes = Base64.decode(savedPhoto, Base64.DEFAULT);
        // 使用 BitmapFactory 解码字节数组为 Bitmap
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        // 将 Bitmap 显示在 ImageView 中
//        userInfo.setImageBitmap(decodedBitmap); // 替换 imageView 为你的 ImageView 对象
        userInfo.setBackground(new BitmapDrawable(getResources(), decodedBitmap));



//        Button musicPlayer = (Button) findViewById(R.id.MusicPlayer);

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
                intent.setClass(HomeActivity.this, CentralActivity.class);
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

        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(HomeActivity.this,MusicPlayerActivity.class);
                startActivity(intent);
                //startActivity(new Intent(MainActivity.this, MusicPlayerActivity.class));
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(HomeActivity.this, CalendarActivity.class);
                startActivity(intent);
                //startActivity(new Intent(MainActivity.this, MusicPlayerActivity.class));
            }
        });


    }
}