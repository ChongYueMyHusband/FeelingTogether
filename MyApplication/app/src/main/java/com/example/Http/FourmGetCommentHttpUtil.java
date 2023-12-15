package com.example.Http;


import android.util.Log;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FourmGetCommentHttpUtil {
    private static final String TAG = "FourmGetAllHttpUtil";

    public static String sendPost(String post_id) {
        String result = "";
        try {
            String path = "http://101.200.84.147:8081/getRemarkInfo";
            URL url = new URL(path);
            // 1. 打开链接 - 创建一个HttpURLconnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置超时时间
            connection.setConnectTimeout(5*1000);

            // 2. 数据处理
            JSONObject jsonData = new JSONObject();
            jsonData.put("post_id", post_id);
            String data = jsonData.toString();

            // 3. 设置连接信息
            //设置这个请求是get请求或post请求或其他的
            connection.setRequestMethod("POST");

            //至少要设置的两个请求头
            //设置请求的属性--内容的长度--键值对，参数是String Key, String value
            // 这行代码设置请求头的 Content-Type 属性，它告诉服务器请求体的格式。在这里，Content-Type 被设置为 "application/x-www-form-urlencoded"，
            // 表示请求体中的数据将以 URL 编码的形式传递。这是一种常见的 POST 请求的数据格式，特别适用于提交表单数据。
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Type", "application/json");
            // 这行代码设置请求头的 Content-Length 属性，指定了请求体的长度。在这里，data.length() 返回请求体字符串的长度，它告诉服务器请求体有多长。
            // 这是一个必要的设置，因为服务器需要知道请求体的长度来正确解析请求。
            connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes("UTF-8").length));

            //设置conn可以向服务端输出内容 --
            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoInput(true);//输入默认允许
            connection.setDoOutput(true);//输出需要手动开启

            // 4. 获取输出流，并进行输出
            OutputStream outputStream = connection.getOutputStream();
            //以二进制流的形式传输给输出端
            outputStream.write(data.getBytes());

            // 5. 获取服务器的响应结果
            //获得结果码
            int responseCode = connection.getResponseCode();
            //响应码为200，则传输成功 ; 404 网络错误
            if (responseCode == 200) {
                //请求成功
                //获得一个文件的输入流
                InputStream inputStream = connection.getInputStream();
                result = StringTools.readStream(inputStream);
                Log.i(TAG, result+"result");
            } else {
                // 请求失败
                Log.e(TAG, "Server error, Response Code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            Log.e(TAG, "Error in doInBackground: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}