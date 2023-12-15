package com.example.feelingtogethertestone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuruiyin.richeditor.RichEditText;
import com.yuruiyin.richeditor.callback.OnImageClickListener;
import com.yuruiyin.richeditor.enumtype.RichTypeEnum;
import com.yuruiyin.richeditor.model.BlockImageSpanVm;
import com.yuruiyin.richeditor.model.StyleBtnVm;
import com.yuruiyin.richeditor.span.BlockImageSpan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Richeditortest extends AppCompatActivity {

    private final int OPEN_ALBUM_REQUESTCODE = 1; //请求码

    private RichEditText richEditText;
    private Button btn_bold, btn_underline, btn_italic, btn_image, btn_content, btn_content2;
    private EditText editTextContent;
    private Uri selectedImageUri;
    private Bitmap selectedBitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_richeditor);

        richEditText = findViewById(R.id.richEditText);
        btn_bold = findViewById(R.id.btn_bold);
        btn_underline = findViewById(R.id.btn_underline);
        btn_italic = findViewById(R.id.btn_italic);
        btn_image = findViewById(R.id.btn_image);
        btn_content = findViewById(R.id.btn_content);
        btn_content2 = findViewById(R.id.btn_content2);

        initBold();
        initItalic();
        initUnderline();

//        btn_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String editTextContent = richEditText.getText().toString();
//                editTextContent.setText(content);
//                //Toast.makeText(Richeditortest.this, "EditText Content: " + editTextContent, Toast.LENGTH_SHORT).show();
//            }
//        });


        btn_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取RichEditText中的文本内容
                Editable editable = richEditText.getText();
                String content = editable.toString();

                // 将文本内容显示在TextView中
                TextView textView = findViewById(R.id.richEditText2);
                textView.setText(content);
            }
        });

        btn_content2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取RichEditText中的文本内容
                Editable editable = richEditText.getText();
                String content = editable.toString();

                // 删除可能存在的 [image] 标记
                content = content.replace("[image]", "");

                // 将文本内容显示在RichEditText中
                RichEditText richEditText2 = findViewById(R.id.richEditText2);
                richEditText2.setText(content);

                if (selectedBitmap != null) {
                    // Insert the selectedBitmap into RichEditText2
                    BlockImageSpanVm imageSpanVm = new BlockImageSpanVm(null, selectedBitmap.getWidth(), selectedBitmap.getWidth());
                    richEditText2.insertBlockImage(selectedBitmap, imageSpanVm, new OnImageClickListener() {
                        @Override
                        public void onClick(BlockImageSpan blockImageSpan) {
                            // Handle image click if needed
                        }
                    });
                } else {
                    // Handle the case when no image is selected
                    Toast.makeText(Richeditortest.this, "未选择图像", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(Richeditortest.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {// 没有权限。
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Richeditortest.this, Manifest.permission.READ_CONTACTS)) {
                            // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                        } else {
                            // 申请授权。
                            ActivityCompat.requestPermissions(Richeditortest.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                        }
                    } else {
                        openAlbum();
                    }
                } else {
                    openAlbum();
                }
            }
        });
    }


    /**
     * 粗体
     */
    private void initBold() {
        StyleBtnVm styleBtnVm = new StyleBtnVm.Builder()
                .setType(RichTypeEnum.BOLD)
                .setClickedView(btn_bold)
                .build();

        richEditText.initStyleButton(styleBtnVm);
    }

    /**
     * 斜体
     */
    private void initItalic() {
        StyleBtnVm styleBtnVm = new StyleBtnVm.Builder()
                .setType(RichTypeEnum.ITALIC)
                .setClickedView(btn_italic)
                .build();

        richEditText.initStyleButton(styleBtnVm);
    }

    /**
     * 下划线
     */
    private void initUnderline() {
        StyleBtnVm styleBtnVm = new StyleBtnVm.Builder()
                .setType(RichTypeEnum.UNDERLINE)
                .setClickedView(btn_underline)
                .build();

        richEditText.initStyleButton(styleBtnVm);
    }

    private void openAlbum() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK); //打开相册
        openAlbumIntent.setType("image/*");     //选择全部照片
        startActivityForResult(openAlbumIntent, OPEN_ALBUM_REQUESTCODE); //发送请求
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意。
                    openAlbum();
                } else {
                    // 权限被用户拒绝了。
                    Toast.makeText(Richeditortest.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * 处理打开相册请求
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
}