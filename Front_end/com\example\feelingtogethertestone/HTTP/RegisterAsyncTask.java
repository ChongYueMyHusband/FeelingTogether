package com.example.HTTPUtils;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterAsyncTask extends AsyncTask<String, Void, String> {
    private OnRegisterResultListener callback;

    // Interface to handle registration result callback
    public interface OnRegisterResultListener {
        void onRegisterResult(boolean isSuccess);
    }
    // Set the callback for handling registration result
    public void setCallback(OnRegisterResultListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        // 在后台线程中执行网络请求
        return RegisterHTTPUtil.sendPost(params[0], params[1]);
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
                callback.onRegisterResult(isSuccess);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error, if needed
        }
    }
}
