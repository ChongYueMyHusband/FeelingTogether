package com.example.feelingtogethertestone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.feelingtogethertestone.R;

import java.util.ArrayList;
import java.util.List;


public class DiarymessageActivity extends AppCompatActivity {
    private String msg;
    private ListView listView;
    private List<String> resultList;
    private final String TAG = "DiaryMessage";
    private AlertDialog alertDialog;
    public DiarymessageActivity() {}
    public DiarymessageActivity(String msg){
        this.msg=msg;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diarymessage);
        MySqliteHelper dbHelper = new MySqliteHelper(this);
        // 初始化视图
        listView = findViewById(R.id.diaryListView);
        // 存储从数据库中检索出的最近的日记
        resultList = new ArrayList<>();
        resultList = dbHelper.getRecentResults();


        // 创建适配器并将结果显示在ListView中
        // 创建了一个 ArrayAdapter 对象，它用于将日记的文本内容（resultList 中的字符串）与 ListView 控件关联起来
        // android.R.layout.simple_list_item_1 参数指定了列表项的布局，通常是一个简单的文本项。
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // // onItemClick 方法在用户点击列表项时触发
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取用户点击的日记内容
                final String selectedDiary = resultList.get(position);
                Log.i(TAG, "" + position);  // 最上方为0（最早的信息为0），一次往下递增
                Log.i(TAG, selectedDiary);  // 成功返回文本内容

                // 创建一个AlertDialog.Builder对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(DiarymessageActivity.this);

                // 获取自定义的视图布局
                // 用于获取自定义对话框的布局
                // R.layout.custom_dialog_layout 是一个 XML 布局文件，它定义了对话框的外观，包括背景图片、按钮等。
                View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_layout, null);
                builder.setView(dialogView);

//                // 设置背景图片
//                // 可以使用ImageView来展示背景图片
//                ImageView backgroundImage = dialogView.findViewById(R.id.backgroundImage);// 获取对话框布局中的 ImageView 控件
//                backgroundImage.setImageResource(R.drawable.boyfriend);

                // 获取TextView控件
                TextView diaryTextView = dialogView.findViewById(R.id.diaryTextView);
                // 设置TextView的文本内容
                diaryTextView.setText(selectedDiary);

                // 设置 "关闭控件" 按钮
                Button closeButton = dialogView.findViewById(R.id.closeButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 关闭控件的操作
                        alertDialog.dismiss(); // 关闭AlertDialog
                    }
                });

                // 设置 "删除当前日记" 按钮
                Button deleteButton = dialogView.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 删除当前日记的操作
                        dbHelper.deleteResultById(position + 1); // 注意检查ID匹配
                        forceUpdateIds(dbHelper);
                        resultList.remove(position);
                        adapter.notifyDataSetChanged();
                        alertDialog.dismiss(); // 关闭AlertDialog
                    }
                });

                // 创建AlertDialog
                alertDialog = builder.create();

                // 设置对话框的边距
                // 设置对话框的宽度和高度


                alertDialog.show();
            }
        });


        listView.setAdapter(adapter);
    }


    private void forceUpdateIds(MySqliteHelper dbHelper) {
        // 获取一个可写的数据库对象 (db)，以便进行数据库操作。
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 查询数据库中的所有记录，按照 id 升序排序
        String[] columns = { "id" };
        Cursor cursor = db.query("calculations", columns, null, null, null, null, "id ASC");

        int newId = 1;

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int currentId = cursor.getInt(cursor.getColumnIndex("id"));
                // 更新记录的 id 值为新值
                ContentValues values = new ContentValues();
                values.put("id", newId);

                String whereClause = "id = ?";
                String[] whereArgs = { String.valueOf(currentId) };

                db.update("calculations", values, whereClause, whereArgs);
                newId++;
            } while (cursor.moveToNext());
        }

        // 重置数据库表的 id 列为 AUTOINCREMENT 形式
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'calculations'");

        cursor.close();
        db.close();
    }


}