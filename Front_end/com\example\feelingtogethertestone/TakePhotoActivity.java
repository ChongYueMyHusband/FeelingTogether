package com.example.feelingtogethertestone;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.HTTPUtils.FacialCollectAsyncTask;
import com.example.HTTPUtils.FacialRecAsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TakePhotoActivity extends AppCompatActivity implements FacialCollectAsyncTask.OnFacialCollectResultListener {

    private static final int TAKE_PHOTO = 1;
    private ImageView picture;
    private Uri imageUri;
    private final String TAG = "TakePhotoActivity";

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        Button takePhoto = (Button) findViewById(R.id.take_photo);
        picture = (ImageView) findViewById(R.id.picture);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建File对象，用于存储拍照后的图片
                // 将图片命名为output_image.jpg，并将它存放在手机SD卡的应用关联缓存目录下
                // getExternalCacheDir()可以得到这个目录
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try {
                    if (outputImage.exists()){
                        //执行删除操作将已有的同名文件删除
                        outputImage.delete();
                    }
                    //在outputImage路径瞎创建文件
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }

                if (Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(TakePhotoActivity.this, "com.example.feelingtogethertestone.fileprovider", outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }

                //启用相机程序 并设置拍摄照片的输出路径为之前获取到的 imageUri
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//创建一个 Intent 对象，指定要启动的是拍照的操作
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //照片的输出路径 imageUri 传递给启动的相机程序。相机程序会将拍摄的照片数据保存到 imageUri 指定的路径中。
                startActivityForResult(intent, TAKE_PHOTO);//方法启动相机程序，并传入一个请求码 TAKE_PHOTO，以便在后续处理拍摄照片的返回结果时，可以根据这个请求码进行区分
                //当拍摄完成后，系统会将照片数据返回给调用应用，并调用 onActivityResult() 方法，以便应用可以获取拍摄的照片数据，进而进行后续的处理
            }
        });

    }

    //报错 Overriding method should call super.onActivityResult
    //不影响结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data); // 添加此行代码
        Bitmap bitmap = null;
        switch (requestCode){
            //请求码 TAKE_PHOTO -- 处理拍摄照片的返回结果时，可以根据这个请求码进行区分
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){ //拍摄照片成功并返回了结果
//                    try {
//                        //将拍摄到的照片显示在ImageView picture;
//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        picture.setImageBitmap(bitmap);
//                    }catch (FileNotFoundException e){
//                        e.printStackTrace();
//                    }
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.kunkun);
                    picture.setImageBitmap(bitmap);
                }
                break;
            default:
                break;
        }

        //encode image to base64 string
        //ByteArrayOutputStream 是一个 OutputStream 的子类，用于在内存中写入二进制数据
        //它通过一个缓冲区来实现，可以将数据写入内部缓冲区中，并提供了许多将缓冲区中的数据转换为各种数据类型的方法
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //compress 方法，将图像压缩为 JPEG 格式，并将压缩后的图像数据写入 ByteArrayOutputStream 中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //toByteArray 方法获取 ByteArrayOutputStream 中的字节数组，并将其转换为 Base64 编码的字符串
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        // 将拍照得到的图片通过HTTP传输给服务器
        FacialCollectAsyncTask facialCollectAsyncTask = new FacialCollectAsyncTask(TakePhotoActivity.this);
        facialCollectAsyncTask.setCallback(TakePhotoActivity.this);
        facialCollectAsyncTask.execute("2387171466", imageString);
        Log.i(TAG, "imageString");  // 测试结果：正常打印图片编码

        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void OnFacialCollectResultListener(String result) {
        // do nothing~~
    }
}