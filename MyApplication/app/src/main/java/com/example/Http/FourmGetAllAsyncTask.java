package com.example.Http;


import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FourmGetAllAsyncTask extends AsyncTask<String, Void, String> {
    private OnFourmGetAllListener callback;

    // Interface to handle PersonalInfo result callback
    public interface OnFourmGetAllListener {
        void onFourmGetAllResult(List<String>id,List<String> userAccountList,List<String> content,List<String> like,List<String> remark,List<String> created_at);
    }
    // Set the callback for handling registration result
    public void setCallback(OnFourmGetAllListener callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        // 在后台线程中执行网络请求
        return FourmGetAllHttpUtil.sendPost(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("GetAll","res: "+result);

        List<String> userAccountList = new ArrayList<>();
        List<String> content = new ArrayList<>();
        List<String> like = new ArrayList<>();
        List<String> remark = new ArrayList<>();
        List<String> created_at = new ArrayList<>();
        List<String> id=new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{id=(.*?), user_account=(.*?), content=(.*?), like=(.*?), remark=(.*?), created_at=(.*?)\\}");
        Matcher matcher = pattern.matcher(result);

        while (matcher.find()) {
            id.add(matcher.group(1).trim());
            userAccountList.add(matcher.group(2).trim());
            content.add(matcher.group(3).trim());
            like.add(matcher.group(4).trim());
            remark.add(matcher.group(5).trim());
            created_at.add(matcher.group(6).trim());

        }
        System.out.println(userAccountList);
        System.out.println(content);
        System.out.println(like);
        System.out.println(remark);
        System.out.println(created_at);
        System.out.println(id);
        if(callback!=null){
            Log.i("tag","enter");
            callback.onFourmGetAllResult(id,userAccountList,content,like,remark,created_at);
        }

    }

}