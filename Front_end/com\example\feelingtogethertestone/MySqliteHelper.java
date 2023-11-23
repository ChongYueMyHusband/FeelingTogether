package com.example.feelingtogethertestone;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MySqliteHelper extends SQLiteOpenHelper {
    public static int DIARY_NUM  = 0;
    public static final String KEY_ID = "id";
    private static final String DATABASE_NAME = "DiaryMessageDataBase.db";
    private static final int DATABASE_VERSION = 1;
//    public static final String TABLE_HISTORY = "emotionDiary_history";
    public static final String TABLE_HISTORY = "calculations";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
//    public static final String COLUMN_DIARY_CONTENT = "diaryContent";
    public static final String COLUMN_DIARY_CONTENT = "result";
    public static final String COLUMN_SENTIMENT_POLARITY = "sentiPolarity";
    SQLiteDatabase db = this.getWritableDatabase();
    public MySqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // 获取数据库中的最近的记录（不包括当前记录），然后返回该记录。
    public String recur(String temp){
        // 从数据库中获取最近的记录，并将结果存储在名为 resultList 的字符串列表中。
        List<String> resultList = getRecentResults();
        if (resultList.size() > 1) {
            // 获取 resultList 中索引为 1 的记录
            String prevResult = resultList.get(1);
            return prevResult;
        } else {
            return "NULL";
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the calculations table
        // 需要在 "CREATE TABLE" 之后添加一个空格，以确保生成正确的 SQL 语句。
        // 在SQL语句中，空格通常用于分隔不同的关键字和标识符，以确保数据库可以正确解释和执行SQL语句。
        // 在这种情况下，"CREATE TABLE" 是SQL语句的一个关键字，后面紧跟着表的名称和表的列定义。如果没有空格来分隔这些部分，SQL解释器可能会将它们视为一个单词，导致语法错误。
        String CREATE_RESULT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                COLUMN_SENTIMENT_POLARITY + " TEXT, " +
                COLUMN_DIARY_CONTENT + " TEXT )";
        db.execSQL(CREATE_RESULT_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    // Insert a result into the database
    // 将一条"diaryContent"插入到数据库表"TABLE_HISTORY"中
    public void insertDiaryContent(String diaryContent, String sentimentPolarity) {
        DIARY_NUM++;    // 跟踪每个插入操作的计数
        // 创建一个可写的数据库对象
        SQLiteDatabase db = this.getWritableDatabase();
        // 创建了ContentValues对象，用于存储数据库表中的键值对数据
        ContentValues values = new ContentValues();

        // 将名为"result"的键和相应的值"result"插入到ContentValues对象中
        values.put(COLUMN_DIARY_CONTENT, diaryContent);
        values.put(COLUMN_SENTIMENT_POLARITY, sentimentPolarity);
        // 将ContentValues对象中的数据插入到名为"calculations"的数据库表中
        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }


    // 从数据库表中删除一行数据
    public void deleteOne(int num) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 创建了一个字符串数组 whereArgs，其中包含一个元素，即要删除的行的标识号 num 被转换为字符串并放入数组中。这个数组将用于指定要删除的行，即指定删除哪些数据。
        String[] whereArgs = { String.valueOf(num) };
        // 从名为 "calculations" 的数据库表中删除数据
        // id=?" 是用于指定删除条件的部分，其中 ? 是一个占位符，它将被 whereArgs 中的实际值替代
        db.delete(TABLE_HISTORY, "id=?", whereArgs);
        db.close();
    }

    // 从数据库表中删除一行数据
    public void deleteResultById(int idNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM calculations WHERE id = " + idNum;
        db.execSQL(deleteQuery);
        db.close();
    }

    public void deleteAllResults() {
        SQLiteDatabase db = this.getWritableDatabase();
        // 第二个参数 null 表示删除的条件，即没有特定的条件限制，而第三个参数也是 null，表示没有需要替代的占位符值。
        //传递 null 给第二和第三个参数，意味着删除表中的所有数据行，而不受任何条件的限制。
        // 因此，这行代码的执行效果是删除 "calculations" 表中的所有数据行，清空了整个表。
        db.delete(TABLE_HISTORY, null, null);
        db.close();
    }

    @SuppressLint("Range")
    public List<String> getRecentResults() {
        // 存储获取到的最近记录。
        List<String> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                this.TABLE_HISTORY,
                new String[]{"result"},     //要检索的列
                null,                    //不添加任何筛选条件
                null,                    //选择条件的参数
                null,                    //分组方式
                null,                    //筛选组的筛选条件
                "id ASC",                // 按照 "id ASC"（升序排列）的方式返回
                "30"                     // 限制获取的历史记录数量，这里限制为10
        );

        // 检查 cursor 是否成功移动到查询结果的第一行。如果 cursor 成功移动到第一行，就表示查询结果非空，进入条件块执行以下操作。
        if (cursor.moveToFirst()) {
            do {
                results.add(cursor.getString(cursor.getColumnIndex("result")));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return results;
    }
}