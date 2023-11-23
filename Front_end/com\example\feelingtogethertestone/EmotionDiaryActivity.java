package com.example.feelingtogethertestone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import com.example.feelingtogethertestone.MainActivity;
import com.example.feelingtogethertestone.R;


public class EmotionDiaryActivity extends AppCompatActivity {

    private EditText diaryTextET;   //日记内容控件
    private Button photoInputBtn;   //插入图片按钮
    private Button audioInputBtn;   //插入语音按钮
    private Button saveDiaryBtn;    //保存日记按钮
    private Button historyDiary;    //历史日记按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_diary);
        historyDiary=findViewById(R.id.historyDiary);
        photoInputBtn=findViewById(R.id.photoDiary);
        diaryTextET=findViewById(R.id.content);
        audioInputBtn=findViewById(R.id.audioDiary);
        saveDiaryBtn=findViewById(R.id.saveDiary);


        saveDiaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentimentPolarity = "positive";
                String editTextContent = "\n" + diaryTextET.getText().toString();
                MySqliteHelper mySqliteHelper = new MySqliteHelper(EmotionDiaryActivity.this);

                // 思路：发送账号+文本内容给服务器，服务器返回情感极性

                // 将文本内容发送至服务器



//                if(sendDiaryMqttUtil.getEmotionPolarity() == "positive"){
//                    // 情感极性为积极，弹出积极的对话框
//                    Log.i("EmotionDiaryActivity", "Poistive detected!");
//                }else{
//                    // 情感极性为消极，弹出消极的对话框
//                    Log.i("EmotionDiaryActivity", "Negative detected!");
//                }

//                mySqliteHelper.insertDiaryContent(editTextContent);
                mySqliteHelper.insertDiaryContent(editTextContent, sentimentPolarity);
            }
        });
        historyDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(EmotionDiaryActivity.this, DiarymessageActivity.class);
                startActivity(intent);
            }
        });
    }

}