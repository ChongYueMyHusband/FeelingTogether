package com.example.Http;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class  FourmLikeAsyncTask extends AsyncTask<String, Void, String> {
    private  OnFourmLikeListener callback;

    // Interface to handle PersonalInfo result callback
    public interface OnFourmLikeListener {
        void OnFourmLikeListener(String isSuccess);
    }
    // Set the callback for handling registration result
    public void setCallback( OnFourmLikeListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        // 在后台线程中执行网络请求
        return FourmLikeHttpUtil.sendPost(params[0],params[1]);
    }

    @Override
    protected void onPostExecute(String result)
    {
        Log.i("like","res: "+result);
        String regex = "\"message\":\"([^\"]*)\"";

        // 使用正则表达式进行匹配
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);

        // 如果匹配成功，提取 "message" 字段的值
        if (matcher.find()) {
            String message = matcher.group(1);
           if(callback!=null){
               callback.OnFourmLikeListener(message);
           }

        }
        Log.i("FourmLikeAsync","msg:"+result);
    }
}