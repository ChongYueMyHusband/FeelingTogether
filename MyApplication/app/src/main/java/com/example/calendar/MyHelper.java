package com.example.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MyHelper extends SQLiteOpenHelper {

    public MyHelper(Context context) {
        super(context, "incast.db", null, 1);
    }

    // 数据库第一次被创建时调用该方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化数据库的表结构，执行一条建表的SQL语句
        db.execSQL("CREATE TABLE information(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20), phone VARCHAR(20))");
    }
    //当数据库的版本号增加时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}