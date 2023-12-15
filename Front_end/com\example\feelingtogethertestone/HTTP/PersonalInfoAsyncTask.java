package com.example.HTTPUtils;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;


public class PersonalInfoAsyncTask extends AsyncTask<String, Void, String> {
    private OnPersonalInfoResultListener callback;

    // Interface to handle PersonalInfo result callback
    public interface OnPersonalInfoResultListener {
        void onPersonalInfoResult(boolean isSuccess);
    }
    // Set the callback for handling registration result
    public void setCallback(OnPersonalInfoResultListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        // 在后台线程中执行网络请求
        return PersonalInfoHTTPUtil.sendPost(params[0], params[1], params[2], params[3], params[4] ,params[5], params[6]);
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
