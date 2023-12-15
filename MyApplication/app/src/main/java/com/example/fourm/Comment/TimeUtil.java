package com.example.fourm.Comment;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeUtil {

    public static String getTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(time));
    }

    public static String data(int num, int likes) {
        String s="评论数量: "+num+"   点赞数: "+likes;
        return s;
    }

}
