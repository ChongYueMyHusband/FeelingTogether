package com.example.feelingtogethertestone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.HTTPUtils.RegisterAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity implements RegisterAsyncTask.OnRegisterResultListener {
    private Button register2;
    private EditText id;
    private EditText pwd_1;
    private EditText pwd_2;
    private EditText emails;

    private String username;
    private String pwd1;
    private String email;
    private String pwd2;
    private final String TAG = "RegisterActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register2 = (Button) findViewById(R.id.regester2);
        id = (EditText) findViewById(R.id.id_edit);
        pwd_1 = (EditText) findViewById(R.id.passward_edit);
        pwd_2 = (EditText) findViewById(R.id.pass_edit_1);
        emails = (EditText) findViewById(R.id.email_edit);

        // 使用 textPassword 类型会使输入的文本以密码形式显示，通常是以圆点或星号等字符来替代实际的文本。
        pwd_1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwd_2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = id.getText().toString().trim();
                email = emails.getText().toString().trim();
                pwd1 = pwd_1.getText().toString().trim();
                pwd2 = pwd_2.getText().toString().trim();

                if (!pwd1.equals(pwd2)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "两次输入密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // 判断邮箱是否合规
                    if (isValidEmail(email)) {
                        // 邮箱合法，可以进行下一步操作
                        try {
                            // 创建 JSON 对象并添加数据
                            JSONObject accountJson = new JSONObject();
                            try {
                                accountJson.put("account", username);
                                accountJson.put("password", pwd1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // 将 JSON 对象转换成字符串
                            String data = accountJson.toString();
                            //  {"account":"2387171466","password":"fan123123"}
                            Log.i(TAG, data);

                            // 使用HTTP进行数据传输
                            // 创建一个新的 RegisterAsyncTask 实例
                            // 这个错误通常发生在一个 AsyncTask 实例已经被执行过一次后，尝试再次执行的情况下。
                            // 在你的代码中，你创建了一个 RegisterAsyncTask 实例，并在点击事件中执行了它。
                            // 由于 AsyncTask 实例只能执行一次，再次尝试执行相同的实例会导致这个错误。
                            //为了解决这个问题，你可以创建一个新的 RegisterAsyncTask 实例，然后执行它。确保每次点击按钮时，都使用一个新的实例。
                            RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask();
                            registerAsyncTask.setCallback(RegisterActivity.this);

                            // 执行新的实例
                            registerAsyncTask.execute(username, pwd1);
                            // 成功对接：输出的可能形式 ; 已添加至数据库中
                            //  {"message":"该用户名已存在！","success":false,"data":null}result
                            // {"message":"注册用户成功！","success":true,
                            // "data":{"id":0,"account":"6666436","password":"123","headPortraitBase64Code":null,"age":0,"gender":null,"username":null,"userIdiograph":null}}result

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 邮箱不合法，显示错误信息或者进行其他处理
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "邮箱不合规范，请重新输入！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        });
    }


    @Override
    public void onRegisterResult(boolean isSuccess) {
        // Handle the registration result
        if (isSuccess) {
            // Registration successful, perform UI transition or other actions
            //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // Registration failed, show a toast or other error message
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

