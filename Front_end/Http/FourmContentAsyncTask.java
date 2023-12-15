package com.example.Http;

import android.os.AsyncTask;
import android.util.Log;


public class FourmContentAsyncTask extends AsyncTask<String, Void, String> {
    private OnFourmListener callback;

    // Interface to handle PersonalInfo result callback
    public interface OnFourmListener {
        void onFourmContentResult(boolean isSuccess);
    }
    // Set the callback for handling registration result
    public void setCallback(OnFourmListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        // 在后台线程中执行网络请求
        return FourmContentHttpUtil.sendPost(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(String result) {
    Log.i("FourmContentAsync","msg:"+result);
    }
}