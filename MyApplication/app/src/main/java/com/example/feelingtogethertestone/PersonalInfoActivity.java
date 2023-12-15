package com.example.feelingtogethertestone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.HTTPUtils.PersonalInfoAsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class PersonalInfoActivity extends AppCompatActivity implements PersonalInfoAsyncTask.OnPersonalInfoResultListener {
    private EditText nameET;//用户名控件
    private EditText idiographET;//个人签名控件
    private EditText sexET;//性别控件
    private EditText ageET;//年龄控件
    private Button photoBtn;//头像按钮
    private Button save;//保存按钮
    private Button collectPersonImage;//采集人像按钮
    private String account;  // 用户名，账号，唯一的账号
    private String pwd;       // 密码
    private String username;    // 昵称，账号的昵称
    private String userIdiograph;   // 个性签名
    private String userSex; // 性别
    private String userAge; // 年龄
    private final String TAG = "PersonalInfoActivity";
    private SharedPreferences sp; // 获取账号密码
    private static final int PICK_IMAGE_REQUEST = 1;    // 请求访问图像常量=1
    private ImageView pictureStored;
    private Uri imageUri;
    Bitmap bitmap = null; // 储存头像的bitmap
    private String imageString = "null123"; // 头像的string表达
    private ImageView femaleIV;
    private ImageView maleIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // 获取所有的 EditText 控件和保存按钮的引用
        nameET = findViewById(R.id.nameET);
        idiographET = findViewById(R.id.idiographET);
        sexET = findViewById(R.id.sexET);
        ageET = findViewById(R.id.ageET);
        photoBtn = findViewById(R.id.Photo);
        save = findViewById(R.id.SaveInfo);
        pictureStored = (ImageView) findViewById(R.id.pictureStored);
        collectPersonImage = findViewById(R.id.CollectPersonImage);
        femaleIV = (ImageView)findViewById(R.id.femaleIV);
        maleIV = (ImageView)findViewById(R.id.maleIV);

        //SharedPreferences是一种轻型的数据存储方式
        //将数据显示到UI控件
        //把config.xml文件中的数据取出来显示到EditText控件中
        //如果没找到key键对应的值，会返回第二个默认的值
        sp = getSharedPreferences("user_info", 0);
        // 从sp中读取存储的username与password， 并自动填写在EditText中
        // 从 SharedPreferences 中读取已保存的数据并设置到相应的控件中
        // 如果之前没有保存过这些数据，将返回默认的空字符串
        // "data":{"id":0,"account":"2387171466","password":"fan123123","headPortraitBase64Code":null,"age":0,"gender":null,"username":null,"userIdiograph":null}}result
        String account = sp.getString("account", "");
        String pwd = sp.getString("password", "");
        String savedUserName = sp.getString("username", "");
        String savedIdiograph = sp.getString("userIdiograph", "");
        String savedSex = sp.getString("gender", "");
        String savedAge = sp.getString("age", "");
        String savedPhoto = sp.getString("headPortraitBase64Code", "");

        // 设置 EditText 控件的文本为从 SharedPreferences 中读取的数据
        nameET.setText(savedUserName);
        idiographET.setText(savedIdiograph);
        sexET.setText(savedSex);
        ageET.setText(savedAge);

        Log.i(TAG, "account" + account + "pwd" + pwd);
        imageString = savedPhoto;
//        Log.i(TAG, savedPhoto); // E/Head/2387171466png

        // 将 Base64 编码的字符串解码为字节数组
        byte[] imageBytes = Base64.decode(savedPhoto, Base64.DEFAULT);
        // 使用 BitmapFactory 解码字节数组为 Bitmap
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        // 将 Bitmap 显示在 ImageView 中
        pictureStored.setImageBitmap(decodedBitmap); // 替换 imageView 为你的 ImageView 对象

        // 创建时，自动获取信息


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            // 当用户点击保存按钮时，获取 EditText 控件中的文本并保存到对应的变量中。
            public void onClick(View view) {
                username = nameET.getText().toString();
                userIdiograph = idiographET.getText().toString();
                userSex = sexET.getText().toString();
                userAge = ageET.getText().toString();

                // 将这些数据合并成一个字符串
                String personalInfoString = "account=" + account + "&password=" + pwd + "&userName=" + username + "&userIdiograph=" + userIdiograph + "&userSex=" + userSex + "&userAge=" + userAge;
                Log.i(TAG, "account=" + account + "&password=" + pwd + "&userName=" + username + "&userIdiograph=" + userIdiograph + "&userSex=" + userSex + "&userAge=" + userAge);

                PersonalInfoAsyncTask personalInfoAsyncTask = new PersonalInfoAsyncTask();
                personalInfoAsyncTask.setCallback(PersonalInfoActivity.this);
                // 执行新的实例
                personalInfoAsyncTask.execute(MainActivity.getUsername(), MainActivity.getPsw(), username, userAge, userSex, userIdiograph, imageString);


                // 将新的信息存储在本地文件中
                //获取sp的编辑器
                SharedPreferences.Editor editor = sp.edit();
                //存储用户输入的数据
                // 如果之前没有存储这个键对应的值，它会创建一个新的键值对。如果已经存在 "username" 键，那么它会将存储在这个键下的值更新为 newUsername。
                editor.putString("username", username);
                editor.putString("userIdiograph", userIdiograph);
                editor.putString("gender", userSex);
                editor.putString("age", userAge);
                //提交editor
                editor.commit();
                Toast.makeText(PersonalInfoActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(PersonalInfoActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });


        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建 Intent 来启动系统相册
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 当你使用 startActivityForResult 启动一个新的 Intent 以等待用户选择图片时，系统会在用户选择完成后自动调用 onActivityResult 函数，并将选择的图片数据包含在 data 参数中返回给你的活动（Activity）
                startActivityForResult(intent, PICK_IMAGE_REQUEST); // 启动 Intent，并等待用户选择图片
            }
        });

        collectPersonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 调用
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(PersonalInfoActivity.this,TakePhotoActivity.class);
                startActivity(intent);
            }
        });

        femaleIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击事件发生时，将 sex 字符串赋值为 "male"
                userSex = "Female";
                sexET.setText(userSex);
                // 同时更新 ImageView 的背景，你可以根据需要更改
                femaleIV.setBackgroundResource(R.drawable.gender_female_clicked);
                maleIV.setBackgroundResource(R.drawable.gender_male);
            }
        });

        maleIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击事件发生时，将 sex 字符串赋值为 "male"
                userSex = "Male";
                sexET.setText(userSex);
                // 同时更新 ImageView 的背景，你可以根据需要更改
                maleIV.setBackgroundResource(R.drawable.gender_male_clicked);
                femaleIV.setBackgroundResource(R.drawable.gender_female);
            }
        });

    }

    // 在 onActivityResult 方法中处理选择图片后的返回结果
    // onActivityResult 方法是 Android 中的一个回调方法，用于处理活动（Activity）之间的数据传递和结果返回。
    // 这个方法是由 Android 系统自动调用的，通常在一个活动启动另一个活动并等待它返回结果时使用。
    // 用于处理用户选择图片后的返回结果
    @Override
    // 请求码（requestCode）、结果码（resultCode）和包含返回数据的 Intent 对象（data）。
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // resultCode == RESULT_OK：这部分检查结果码是否等于 RESULT_OK，它表示用户已经成功选择了一张图片。
        // data != null：这部分检查 data 对象是否不为空，以确保包含所选图片的数据。
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // 用户已成功选择图片
            // 从 data 中获取所选图片的 Uri（Uniform Resource Identifier）。Uri 是一个标识资源的统一标识符，可以用于访问所选图片。
            Uri selectedImageUri = data.getData();
            Log.i(TAG, "Picture selected!");
            // 根据 selectedImageUri 来处理所选的图片
            //将拍摄到的照片显示在ImageView picture;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                pictureStored.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            //encode image to base64 string
            //ByteArrayOutputStream 是一个 OutputStream 的子类，用于在内存中写入二进制数据
            //它通过一个缓冲区来实现，可以将数据写入内部缓冲区中，并提供了许多将缓冲区中的数据转换为各种数据类型的方法
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //compress 方法，将图像压缩为 JPEG 格式，并将压缩后的图像数据写入 ByteArrayOutputStream 中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //toByteArray 方法获取 ByteArrayOutputStream 中的字节数组，并将其转换为 Base64 编码的字符串
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            SendImageMqttUtil sendImageMqtt = new SendImageMqttUtil(imageString, getApplicationContext());
            sendImageMqtt.send();
            Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();

            Log.i(TAG, imageString);

            SharedPreferences.Editor editor = sp.edit();
            //存储用户输入的数据
            // 如果之前没有存储这个键对应的值，它会创建一个新的键值对。如果已经存在 "username" 键，那么它会将存储在这个键下的值更新为 newUsername。
            editor.putString("headPortraitBase64Code", imageString);
            editor.commit(); // 或者使用 editor.apply();
        }
    }


    @Override
    public void onPersonalInfoResult(boolean isSuccess) {

    }
}