package com.example.feelingtogethertestone;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.feelingtogethertestone.audio.AudioUtil;
import com.example.feelingtogethertestone.audio.History;
import com.example.HTTPUtils.EmotionDiarySaveAsyncTask;
import com.example.feelingtogethertestone.MainActivity;
import com.example.feelingtogethertestone.R;
import com.example.feelingtogethertestone.audio.PcmToWavUtil;
import com.yuruiyin.richeditor.RichEditText;
import com.yuruiyin.richeditor.callback.OnImageClickListener;
import com.yuruiyin.richeditor.enumtype.RichTypeEnum;
import com.yuruiyin.richeditor.model.BlockImageSpanVm;
import com.yuruiyin.richeditor.model.StyleBtnVm;
import com.yuruiyin.richeditor.span.BlockImageSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class EmotionDiaryActivity extends AppCompatActivity implements EmotionDiarySaveAsyncTask.OnEmotionDiarySaveResultListener {

    private EditText diaryTextET;   //日记内容控件
    private RichEditText richEditText;
    private Button photoInputBtn;   //插入图片按钮
    private Button bold;
    private Button italic;
    private Button baseline;
    private Button photo;
    private Button audioInputBtn;   //插入语音按钮
    private Button audioPlay;       // 语音播放按钮
    private Button saveDiaryBtn;    //保存日记按钮
    private Button historyDiary;    //历史日记按钮
    private SharedPreferences sp;
    private final int OPEN_ALBUM_REQUESTCODE = 1; //请求码
    private AlertDialog alertDialog;
    private int count = 0;
    private Uri selectedImageUri;
    private Bitmap selectedBitmap;
    View dialogView;
    private String TAG = "EmotionDiaryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_diary);
        richEditText = findViewById(R.id.content);
        historyDiary=findViewById(R.id.historyDiary);
        photoInputBtn=findViewById(R.id.photoDiary);
        diaryTextET=findViewById(R.id.content);
        audioInputBtn=findViewById(R.id.audioDiary);
        audioPlay = findViewById(R.id.audioPlay);
        bold = findViewById(R.id.bold);
        italic = findViewById(R.id.italic);
        baseline = findViewById(R.id.baseline);
        photo = findViewById(R.id.photoDiary);
        saveDiaryBtn=findViewById(R.id.saveDiary);


        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StyleBtnVm styleBtnVm = new StyleBtnVm.Builder()
                        .setType(RichTypeEnum.BOLD)
                        .setClickedView(bold)
                        .build();

                richEditText.initStyleButton(styleBtnVm);
            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StyleBtnVm styleBtnVm = new StyleBtnVm.Builder()
                        .setType(RichTypeEnum.ITALIC)
                        .setClickedView(italic)
                        .build();

                richEditText.initStyleButton(styleBtnVm);
            }
        });

        baseline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StyleBtnVm styleBtnVm = new StyleBtnVm.Builder()
                        .setType(RichTypeEnum.UNDERLINE)
                        .setClickedView(baseline)
                        .build();

                richEditText.initStyleButton(styleBtnVm);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbum();
            }
        });

        saveDiaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentimentPolarity = "";
                String editTextContent = diaryTextET.getText().toString();

//                // 从本地缓存中获取账号信息
//                sp = getSharedPreferences("user_info", 0);
//                // 从sp中读取存储的username与password， 并自动填写在EditText中
//                String account = sp.getString("account", "");
//                Log.i(TAG, account);

                Log.i(TAG+"1", editTextContent); // 内容正常：你在别的地方打文字，然后长按粘贴过去

                // 思路：发送账号+文本内容给服务器，服务器返回情感极性 ； 根据服务器返回的情感极性弹出相应的对话框，用来显示日记

                // 将文本内容发送至服务器，在回调函数中处理返回值，显示对话框与存储日记
                EmotionDiarySaveAsyncTask emotionDiarySaveAsyncTask = new EmotionDiarySaveAsyncTask();
                emotionDiarySaveAsyncTask.setCallback(EmotionDiaryActivity.this);
                emotionDiarySaveAsyncTask.execute(editTextContent);
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
        audioInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if(count % 2 != 0){
                    Toast.makeText(EmotionDiaryActivity.this, "开始录音",Toast.LENGTH_LONG).show();
                    startRecordAudio();
                }else{
                    Toast.makeText(EmotionDiaryActivity.this, "结束录音",Toast.LENGTH_LONG).show();
                    stopRecordAudio();
                    change();
                }
            }
        });
        audioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(EmotionDiaryActivity.this, History.class);
                startActivity(intent);
            }
        });
    }

    private void openAlbum() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK); //打开相册
        openAlbumIntent.setType("image/*");     //选择全部照片
        startActivityForResult(openAlbumIntent, OPEN_ALBUM_REQUESTCODE); //发送请求
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_ALBUM_REQUESTCODE && resultCode == RESULT_OK){
            if (data!=null){
                Uri uri = data.getData();
                selectedImageUri = uri;
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    BlockImageSpanVm imageSpanVm = new BlockImageSpanVm(null, selectedBitmap.getWidth(), selectedBitmap.getWidth());
                    richEditText.insertBlockImage(selectedBitmap, imageSpanVm, new OnImageClickListener() {
                        @Override
                        public void onClick(BlockImageSpan blockImageSpan) {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onEmotionDairySaverResult(String result) {
        Log.i(TAG+"2", result);  // 内容为空！
        String sentimentPolarity = "positive";
        // 读取情感极性，并将日记内容与情感添加在情感日记中
        String editTextContent = diaryTextET.getText().toString();
//        try {
//            JSONObject jsonResult = new JSONObject(result);
//            sentimentPolarity = jsonResult.getString("sentimentPolarity");;
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }

        sentimentPolarity = result;

        MySqliteHelper mySqliteHelper = new MySqliteHelper(EmotionDiaryActivity.this);
        mySqliteHelper.insertDiaryContent(editTextContent, sentimentPolarity);
        // 创建一个AlertDialog.Builder对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(EmotionDiaryActivity.this);

        // 根据情感极性。创建不同XML布局的对话框
        // 获取自定义的视图布局
        // 用于获取自定义对话框的布局
        // R.layout.custom_dialog_layout 是一个 XML 布局文件，它定义了对话框的外观，包括背景图片、按钮等。
        if (sentimentPolarity.equals("positive")){
            // 情感极性为积极，弹出积极的对话框
            Log.i("EmotionDiaryActivity", "Poistive detected!");
            dialogView = getLayoutInflater().inflate(R.layout.save_dialog_layout_posivite, null);
            builder.setView(dialogView);
        } else {
            // 情感极性为消极，弹出消极的对话框
            Log.i("EmotionDiaryActivity", "Negative detected!");
            dialogView = getLayoutInflater().inflate(R.layout.save_dialog_layout_negative, null);
            builder.setView(dialogView);
        }
        // 获取TextView控件
        TextView diaryTextView = dialogView.findViewById(R.id.diaryTextView);
        // 设置TextView的文本内容
        diaryTextView.setText(editTextContent);
        // 设置 "关闭控件" 按钮
        Button closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭控件的操作
                alertDialog.dismiss(); // 关闭AlertDialog
            }
        });
        // 创建AlertDialog
        alertDialog = builder.create();
        // 设置对话框的边距
        // 设置对话框的宽度和高度
        alertDialog.show();

    }
    private AudioRecord audioRecord;
    public static final int SAMPLE_RATE_INHZ = 44100;
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private Thread recordingAudioThread;
    private boolean isRecording = false;//mark if is recording
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String wavFilePath;
    public String startRecordAudio() {
        String audioCacheFilePath = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + "jerboa_audio_cache.pcm";
        try{
            // 获取最小录音缓存大小，
            int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT);
            this.audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_INHZ, CHANNEL_CONFIG, AUDIO_FORMAT, minBufferSize);


            // 开始录音
            this.isRecording = true;
            audioRecord.startRecording();

            // 创建数据流，将缓存导入数据流
            this.recordingAudioThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(audioCacheFilePath);
                    Log.i(TAG, "audio cache pcm file path:" + audioCacheFilePath);


                    try {
                        if(!file.exists()){
                            file.createNewFile();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.e(TAG, "临时缓存文件未找到");
                    }
                    if (fos == null) {
                        return;
                    }

                    byte[] data = new byte[minBufferSize];
                    int read;
                    if (fos != null) {
                        while (isRecording && !recordingAudioThread.isInterrupted()) {
                            read = audioRecord.read(data, 0, minBufferSize);
                            if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                                try {
                                    fos.write(data);
                                    Log.i("audioRecordTest", "写录音数据->" + read);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    try {
                        // 关闭数据流
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.recordingAudioThread.start();
        }
        catch(IllegalStateException e){
            Log.w(TAG,"需要获取录音权限！");
            this.checkIfNeedRequestRunningPermission();
        }
        catch(SecurityException e){
            Log.w(TAG,"需要获取录音权限！");
            this.checkIfNeedRequestRunningPermission();
        }
        return audioCacheFilePath;
    }
    private void checkIfNeedRequestRunningPermission() {
    }
    public void stopRecordAudio(){
        try {
            this.isRecording = false;
            if (this.audioRecord != null) {
                this.audioRecord.stop();
                this.audioRecord.release();
                this.audioRecord = null;
                this.recordingAudioThread.interrupt();
                this.recordingAudioThread = null;
            }
        }
        catch (Exception e){
            Log.w(TAG,e.getLocalizedMessage());
        }
    }
    public void change(){
        wavFilePath = this.getExternalFilesDir(Environment.DIRECTORY_PODCASTS) + "/wav_" + System.currentTimeMillis() + ".wav";
        PcmToWavUtil ptwUtil = new PcmToWavUtil();
        ptwUtil.pcmToWav(this.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + "jerboa_audio_cache.pcm",wavFilePath,true);
        Log.i(TAG, "audio cache wav file path:" + wavFilePath);
    }
}