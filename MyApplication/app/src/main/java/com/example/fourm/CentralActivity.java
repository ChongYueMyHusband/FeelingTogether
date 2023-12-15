package com.example.fourm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.feelingtogethertestone.HomeActivity;
import com.example.feelingtogethertestone.R;

public class CentralActivity extends AppCompatActivity {
    private Button postive;
    private Button negative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);
        postive=findViewById(R.id.POSITIVE);
        negative=findViewById(R.id.NEGATIVE);
        postive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(CentralActivity.this, FourmmainActivity.class);
                intent.putExtra("emo", "positive");
                startActivity(intent);
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(CentralActivity.this, FourmmainActivity.class);
                intent.putExtra("emo", "negative");
                startActivity(intent);
            }
        });
    }


}