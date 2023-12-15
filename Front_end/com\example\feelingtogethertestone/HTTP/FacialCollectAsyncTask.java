package com.example.HTTPUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;


public class FacialCollectAsyncTask extends AsyncTask<String, Void, String> {
    private OnFacialCollectResultListener callback;
    private SharedPreferences sp;
    private String TAG = "FacialCollectAsyncTask";
    private Context context; // Add this field

    // 构造器
    public FacialCollectAsyncTask(Context context){
        this.context = context;
    }

    // Interface to handle registration result callback
    public interface OnFacialCollectResultListener {
        void OnFacialCollectResultListener(String result);
    }

    // Set the callback for handling registration result
    public void setCallback(OnFacialCollectResultListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        // 在后台线程中执行网络请求
        return FacialCollectHTTPUtil.sendPost(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(String result) {
//        Log.i(TAG, result);
        if (callback != null) {
            callback.OnFacialCollectResultListener(result);
        }
    }

}
