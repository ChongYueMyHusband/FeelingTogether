package com.example.HTTPUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FacialRecAsyncTask extends AsyncTask<String, Void, String> {
    private OnFacialRecResultListener callback;
    private SharedPreferences sp;
    private Context context; // Add this field

    // 构造器
    public FacialRecAsyncTask(Context context){
        this.context = context;
    }

    // Interface to handle registration result callback
    public interface OnFacialRecResultListener {
        void onFacialRecrResult(boolean isSuccess);
    }

    // Set the callback for handling registration result
    public void setCallback(OnFacialRecResultListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        // 在后台线程中执行网络请求
        return FacialRecHTTPUtil.sendPost(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the result of the network request
        // Parse the JSON result to determine success
        try {
            // Assume your JSON response looks like {"success":true,"message":"Registration successful"}
            // You may need to adjust this based on your actual response structure
            JSONObject jsonResult = new JSONObject(result);
            boolean isSuccess = jsonResult.getBoolean("success") || jsonResult.getString("success").equalsIgnoreCase("true");

            // Notify the callback with the registration result
            if (callback != null) {
                callback.onFacialRecrResult(isSuccess);
            }

            // If login is successful, trigger page transition and save user info to SharedPreferences
            // 统一命名规范：账号account密码password年龄age性别gender个性签名userIdiograph昵称username头像headPortraitBase64Code
            if (isSuccess) {
                // Extract user information from the JSON response
                JSONObject userData = jsonResult.getJSONObject("data");
                String userAccount = userData.getString("account");
                String userName = userData.optString("username", ""); // Replace with the actual field name
                String userAge = userData.optString("age", ""); // Replace with the actual field name
                String userSex = userData.getString("gender");
                String userIdiograph = userData.getString("userIdiograph");
                String headPortraitBase64Code = userData.getString("headPortraitBase64Code");
                Log.i("TAG", headPortraitBase64Code);
                // Add other fields as needed

                // Save user information to SharedPreferences
                saveUserInfoToSharedPreferences(userAccount, userName, userAge, userIdiograph, userSex, headPortraitBase64Code);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error, if needed
        }
    }

    // Method to save user information to SharedPreferences
    private void saveUserInfoToSharedPreferences(String userAccount, String userName, String userAge, String userIdiograph, String userSex, String imageString ) {
        // 存储个人信息
        // SharedPreferences 是一个用于存储简单键值对数据的 Android API。这些数据可以持久保存，以便在应用程序关闭后仍然可用。
        //  创建了一个名为 "user_info" 的 SharedPreferences 文件，并指定了访问模式为 Context.MODE_PRIVATE，表示只有你的应用可以访问这个数据。
        // Context.MODE_PRIVATE，表示只有你的应用可以访问这个数据。
        // SharedPreferences 的数据是与特定的 Context 相关的。不同的 Context 对象会创建不同的 SharedPreferences 数据文件，
        // 这意味着如果你在一个活动中使用了一个 Context 对象来存储数据，然后在另一个活动中使用不同的 Context 对象来访问相同的数据文件，
        // 它们将无法访问相同的数据。
        // 此处的context就是主活动的，构造器传递了主活动的context
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        // 对象 editor 用于编辑和存储数据。你可以使用 editor 来添加、修改或删除数据，然后将更改应用到 SharedPreferences 文件。
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 将用户的用户名信息存储在 SharedPreferences 中。这行代码将 "username" 作为键，receivedUsername 作为值存储。
        editor.putString("username", userName);
        editor.putString("userIdiograph", userIdiograph);
        editor.putString("age", userAge);
        editor.putString("gender", userSex);
        editor.putString("headPortraitBase64Code", imageString);
        // Add other fields as needed
        editor.apply();
    }
}
