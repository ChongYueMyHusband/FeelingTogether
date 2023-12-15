package com.example.fourm.Comment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

public class SQLiteCommentHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Comment.db";

    // 记事本
    public static final String TABLE_Comment = "tb_Comment";

    public static final String Comment_ID = "CommentId";
    public static final String CONTENT = "content";
    public static final String EDIT_TIME = "editTime";
    public static final String USERNAME = "username";
    public static final String Like="likes";

    public SQLiteCommentHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表SQL语句
        String sqlStore = "create table if not exists " + TABLE_Comment +
                " (" +
                Comment_ID + " integer primary key ," +
                CONTENT + " varchar ," +
                EDIT_TIME + " integer ," +
                USERNAME + " TEXT ," +
                Like + " integer " +
                ")";

        // 执行建store表语句
        db.execSQL(sqlStore);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}