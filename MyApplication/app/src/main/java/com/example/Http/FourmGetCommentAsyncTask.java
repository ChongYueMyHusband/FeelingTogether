package com.example.Http;


import android.os.AsyncTask;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FourmGetCommentAsyncTask extends AsyncTask<String, Void, String> {
    private OnFourmGetCommentListener callback;

    // Interface to handle PersonalInfo result callback
    public interface OnFourmGetCommentListener {
        void onFourmGetCommentResult(List<String> userAccountList, List<String> post_id, List<String> contentList,List<String> timestamp);
    }
    // Set the callback for handling registration result
    public void setCallback(OnFourmGetCommentListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        // 在后台线程中执行网络请求
        return FourmGetCommentHttpUtil.sendPost(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        List<String> userAccountList = new ArrayList<>();
        List<String> post_id = new ArrayList<>();
        List<String> contentList = new ArrayList<>();
        List<String> timestamp = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{user_account=(.*?), post_id=(.*?), content=(.*?), timestamp=(.*?)\\}");
        Matcher matcher = pattern.matcher(result);

        while (matcher.find()) {
            userAccountList.add(matcher.group(1).trim());
            post_id.add(matcher.group(2).trim());
            contentList.add(matcher.group(3).trim());
            timestamp.add(matcher.group(4).trim());
        }
        System.out.println(userAccountList);
        System.out.println(post_id);
        System.out.println(contentList);
        System.out.println(timestamp);
        if(callback!=null){
            Log.i("tag","enter");
            callback.onFourmGetCommentResult(userAccountList,post_id,contentList,timestamp);
        }

    }

}