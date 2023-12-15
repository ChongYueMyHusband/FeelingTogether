package com.example.feelingtogethertestone;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.HTTPUtils.FacialRecAsyncTask;
import com.example.HTTPUtils.LoginAsyncTask;
import com.example.musicPlayer.MusicPlayerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity implements LoginAsyncTask.OnLoginResultListener,FacialRecAsyncTask.OnFacialRecResultListener{
    //控件的声明：
    private EditText id;//用户名控件
    private EditText passward1;//密码控件
    private Button login;//注册按钮
    private Button register;//注册按钮
    public CheckBox cb;//勾选框
    //变量声明：
    public static String account;//用户名
    private String pwd;//密码
    //记住账号密码
    private SharedPreferences sp;
    private String result;
    private int RequestCode = 1;
    private static final int TAKE_PHOTO = 1;
    private Button ficialRec;//人像识别按钮
    private ImageView facialPicture;  // IV控件，显示拍照得到的图像
    private Uri imageUri;
    private final String TAG = "MainActivity";
    private Button guide;
    private AlertDialog alertDialog;
    View dialogView;
    //需要申请的运行时权限
    private String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static String getUsername(){
        return account;
    }


    ////请求用户授权几个权限，调用后系统会显示一个请求用户授权的提示对话框，App不能配置和修改这个对话框，
    // 如果需要提示用户这个权限相关的信息或说明，需要在调用 requestPermissions() 之前处理
    //int requestCode: 会在回调onRequestPermissionsResult()时返回，用来判断是哪个授权申请的回调。
    //个人理解：requestCode 相当于ID，当申请多个权限的时候，区分是那个权限---这里似乎没有达到区分的目的：）
    private static final int MY_PERMISSIONS_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ask for permissions
        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);


        id = (EditText) findViewById(R.id.id);
        passward1 = (EditText) findViewById(R.id.passward);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.regester);
        guide = (Button) findViewById(R.id.guide);

        // 使用 textPassword 类型会使输入的文本以密码形式显示，通常是以圆点或星号等字符来替代实际的文本。
        passward1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // 创建一个AlertDialog.Builder对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


        //记住账号密码
        cb = (CheckBox) findViewById(R.id.check);
        //SharedPreferences是一种轻型的数据存储方式
        //将数据显示到UI控件
        //把config.xml文件中的数据取出来显示到EditText控件中
        //如果没找到key键对应的值，会返回第二个默认的值
        sp = getSharedPreferences("user_info", 0);
        // 从sp中读取存储的username与password， 并自动填写在EditText中
        String usernameRAM = sp.getString("account", "");
        String passwordRAM = sp.getString("password", "");
        id.setText(usernameRAM);
        this.passward1.setText(passwordRAM);

        ficialRec = (Button) findViewById(R.id.facialRecognition);
        facialPicture = (ImageView) findViewById(R.id.facialPicture);

        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //监听按钮，如果点击，就跳转
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //读取EV中的数据并储存
                account = id.getText().toString().trim();
                pwd = passward1.getText().toString().trim();
                //检测账号密码不能为空
                if(TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)){
                    Toast.makeText(MainActivity.this, "Account or passward can't be empty", Toast.LENGTH_LONG).show();
                }else{
                    //登录账户：
                    // HTTP进行前后端通信
                    // 创建 JSON 对象并添加数据
                    JSONObject accountJson = new JSONObject();
                    try {
                        accountJson.put("account", account);
                        accountJson.put("password", pwd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 将 JSON 对象转换成字符串
                    String accountString = accountJson.toString();
                    // 打印生成的 JSON 字符串
                    Log.i(TAG, accountString); // 正确传输，传输内容：{"id":"123","password":"123"}


                    if(cb.isChecked()){//判断是否勾选
                        //使用sharePreferences区保存数据 拿到sp实例
                        //参数：  name生成xml文件 ; mode 模式
                        //获取SharedPreferences对象
                        Context ctx = MainActivity.this;
                        //SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
                        //获取sp的编辑器
                        SharedPreferences.Editor editor = sp.edit();
                        //存储用户输入的数据
                        editor.putString("account", account);
                        editor.putString("password", pwd);
                        //提交editor
                        editor.commit();
                    }
                    //登录账户结束

                    // 使用HTTP进行数据传输
                    LoginAsyncTask loginAsyncTask = new LoginAsyncTask(MainActivity.this);
                    loginAsyncTask.setCallback(MainActivity.this);
                    loginAsyncTask.execute(account, pwd);
//                    new LoginAsyncTask().execute("123", "456");
                    // 成功对接：输入{"account":"123","password":"456"} ； 输出id=123&password=456
                    // 输出两种情况：
                    // {"message":"登录成功！","success":true,
                    // "data":{"id":0,"userName":null,"password":"fan123123","age":0,"idiograph":null,
                    // "account":"2387171466","sex":null}}result
                    // {"message":"用户名或者密码错误！","success":false,"data":null}result

//                    // 登录后进行页面跳转
//                    //监听按钮，如果点击，就跳转
//                    Intent intent = new Intent();
//                    //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
//                    intent.setClass(MainActivity.this, HomeActivity.class);
//                    // 在 MainActivity 中传递的账号和密码
//                    startActivity(intent);
                }
            }
        });

        ficialRec.setOnClickListener(new View.OnClickListener(){
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
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.feelingtogethertestone.fileprovider", outputImage);
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


        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //监听按钮，如果点击，就跳转
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(MainActivity.this, GuideActivity.class);
                // 在 MainActivity 中传递的账号和密码
                startActivity(intent);
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
                    //将拍摄到的照片显示在ImageView picture;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        facialPicture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    // 这里原本是拍照的逻辑，现在改为加载固定的图片
                    // 加载固定的图片 giegie.jpeg
//                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gege);
//                    facialPicture.setImageBitmap(bitmap);
                }
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
        FacialRecAsyncTask facialRecAsyncTask = new FacialRecAsyncTask(MainActivity.this);
        facialRecAsyncTask.setCallback(MainActivity.this);
        account = id.getText().toString().trim();
        facialRecAsyncTask.execute(account, imageString);
        Log.i(TAG, "imageString");  // 测试结果：正常打印图片编码

        Toast.makeText(this, "操作成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            // 检查授权结果
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 用户已授权相应的权限
                    Toast.makeText(this, permissions[i] + " 权限已授权", Toast.LENGTH_SHORT).show();
                } else {
                    // 用户未授权相应的权限，可以根据需要进行处理
                    Toast.makeText(this, permissions[i] + " 权限未授权", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onLoginResult(boolean isSuccess) {
        Log.i("HTTP", isSuccess+"");
        // Handle the registration result
        if (isSuccess) {
            // Registration successful, perform UI transition or other actions
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            // Registration failed, show a toast or other error message
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onFacialRecrResult(boolean isSuccess) {
        Log.i("HTTP", isSuccess+"");
        // Handle the registration result
        if (isSuccess) {
            // Registration successful, perform UI transition or other actions
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            // Registration failed, show a toast or other error message
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}