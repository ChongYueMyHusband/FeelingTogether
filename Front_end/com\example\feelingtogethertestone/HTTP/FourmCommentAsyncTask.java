package com.example.Http;


import android.os.AsyncTask;
import android.util.Log;


public class FourmCommentAsyncTask extends AsyncTask<String, Void, String> {
    private OnFourmCommentListener callback;

    // Interface to handle PersonalInfo result callback
    public interface OnFourmCommentListener {
        void onFourmCommentResult(boolean isSuccess);
    }
    // Set the callback for handling registration result
    public void setCallback(OnFourmCommentListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        // 在后台线程中执行网络请求
        return FourmCommentHttpUtil.sendPost(params[0], params[1],params[2]);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("FourmCommentAsync","msg:"+result);
    }
}