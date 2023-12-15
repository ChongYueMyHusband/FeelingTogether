package com.example.fourm;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.List;
import static com.example.fourm.SQLiteHelper.CONTENT;
import static com.example.fourm.SQLiteHelper.EDIT_TIME;
import static com.example.fourm.SQLiteHelper.Like;
import static com.example.fourm.SQLiteHelper.NOTEBOOK_ID;
import static com.example.fourm.SQLiteHelper.POST_ID;
import static com.example.fourm.SQLiteHelper.TABLE_NOTEBOOK;
import static com.example.fourm.SQLiteHelper.USERNAME;
import static com.example.fourm.SQLiteHelper.COMMENT;

// 管理数据库操作的类，主要负责插入、更新、查询和删除记事本（NotebookBean）数据的功能

/**
 * 数据库管理类
 */

public class DBManager {

    private static final String TAG = DBManager.class.getSimpleName();
    private SQLiteHelper helper;

    // 构造方法接收一个 Context 对象作为参数，用于初始化数据库帮助类 SQLiteHelper。这个类在创建时就会初始化一个 SQLiteOpenHelper 对象。
    public  DBManager(Context context){
        if(helper==null){
            helper=new SQLiteHelper(context);
        }
    }



    // 插入数据到记事本表中 - 方法接收一个 NotebookBean 对象，将其内容插入到记事本表中。
    public boolean insertNotebook(NotebookBean notebookBean) {
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            // 使用 helper.getWritableDatabase() 获取可写数据库
            db = helper.getWritableDatabase();
            db.beginTransaction();
            // 通过 ContentValues 封装数据，然后使用 insert 方法插入数据
            ContentValues cv = new ContentValues();
            cv.put(SQLiteHelper.CONTENT, notebookBean.getContent());
            cv.put(SQLiteHelper.EDIT_TIME, notebookBean.getEditTime());
            cv.put(SQLiteHelper.USERNAME, notebookBean.getUsername());
            cv.put(SQLiteHelper.Like, notebookBean.getLikeSpecial());
            cv.put(SQLiteHelper.COMMENT,notebookBean.getCommentNum());
            cv.put(SQLiteHelper.POST_ID,notebookBean.getPost_id());
            Log.i("insert","like: "+notebookBean.getLikeSpecial());
            long num = db.insert(SQLiteHelper.TABLE_NOTEBOOK, null, cv);
            Log.d(TAG, "insert_num:" + num);
            // beginTransaction() 和 setTransactionSuccessful() 用于事务操作，确保插入操作的原子性
            db.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "insert:" + e.toString());
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return flag;
    }
    public boolean insertNotebook1(NotebookBean notebookBean,int like) {
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            // 使用 helper.getWritableDatabase() 获取可写数据库
            db = helper.getWritableDatabase();
            db.beginTransaction();
            // 通过 ContentValues 封装数据，然后使用 insert 方法插入数据
            ContentValues cv = new ContentValues();
            cv.put(SQLiteHelper.CONTENT, notebookBean.getContent());
            cv.put(SQLiteHelper.EDIT_TIME, notebookBean.getEditTime());
            cv.put(SQLiteHelper.USERNAME, notebookBean.getUsername());
            cv.put(SQLiteHelper.Like, like);
            long num = db.insert(SQLiteHelper.TABLE_NOTEBOOK, null, cv);
            Log.d(TAG, "insert_num:" + num);
            // beginTransaction() 和 setTransactionSuccessful() 用于事务操作，确保插入操作的原子性
            db.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "insert:" + e.toString());
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return flag;
    }


    // 更新数据到记事本表中 - 当你已经有一条存在的记事本数据，但是需要更新它的内容时，使用更新操作
    public boolean updateNotebook(NotebookBean notebookBean,int position,String like) {
        boolean flag = false;
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CONTENT, notebookBean.getContent());
            values.put(EDIT_TIME, notebookBean.getEditTime());

            db.update(TABLE_NOTEBOOK, values, "notebookId = ?", new String[] { notebookBean.getNotebookId() + "" });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "updateNotebook:" + e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return flag;
    }



    //查询商店列表信息 - 查询记事本表中的数据，并返回一个 List<NotebookBean>
    @SuppressLint("Range")
    public List<NotebookBean> selectNotebookList() {
        ArrayList<NotebookBean> list = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            String sql = "select * from " + SQLiteHelper.TABLE_NOTEBOOK + " order by " + EDIT_TIME + " desc ";
            cursor = db.rawQuery(sql, null);
            list = new ArrayList<>();
            while (cursor.moveToNext()) {
                NotebookBean notebookBean = new NotebookBean();
                notebookBean.setNotebookId(cursor.getInt(cursor.getColumnIndex(NOTEBOOK_ID)));
                notebookBean.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                notebookBean.setEditTime(cursor.getLong(cursor.getColumnIndex(EDIT_TIME)));
                notebookBean.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME)));
                notebookBean.setLikeSpecial(cursor.getInt(cursor.getColumnIndex(Like)));
                notebookBean.setCommentNum(cursor.getInt(cursor.getColumnIndex(COMMENT)));
                notebookBean.setPost_id(cursor.getInt(cursor.getColumnIndex(POST_ID)));
                list.add(notebookBean);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "selectNotebookList:" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    /**
     * 删除一条记事 - 删除的条件是 NOTEBOOK_ID = notebookId
     * @param notebookId
     */
    public void deleteNotebook(int notebookId){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            // 构建 SQL 语句，其中使用 WHERE 子句指定删除条件。
            String sql = "delete  from " + SQLiteHelper.TABLE_NOTEBOOK + " where " + NOTEBOOK_ID + " = " + notebookId;
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor)
                cursor.close();
            if (null != db)
                db.close();
        }
    }
    public void deleteAllNotebooks() {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            // 执行 DELETE 语句，删除表中的所有数据
            db.delete(SQLiteHelper.TABLE_NOTEBOOK, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "deleteAllNotebooks:" + e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

}
