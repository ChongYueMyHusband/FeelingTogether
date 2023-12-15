package com.example.HTTPUtils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class EmotionDiarySaveAsyncTask extends AsyncTask<String, Void, String> {
    private OnEmotionDiarySaveResultListener callback;

    // Interface to handle save result callback
    public interface OnEmotionDiarySaveResultListener {
        void onEmotionDairySaverResult(String result);
    }
    // Set the callback for handling registration result
    public void setCallback(OnEmotionDiarySaveResultListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        // 在后台线程中执行网络请求
        return EmotionDiarySaveHTTPUtil.sendPost(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("EmotionDiaryActivity", result);
        // 处理接收到的情感极性 - 直接传递
        // 数据形式：data中存储sentimentPolarity
        if (callback != null) {
            callback.onEmotionDairySaverResult(result);
        }
    }
}
