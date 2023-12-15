package com.example.fourm;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


// 这是一个实现了 Parcelable 接口的 Java 类 NotebookBean，用于在 Android 应用中传递笔记本相关的数据。
// 这个类的设计目的是将笔记本的相关数据打包成一个可传递的对象，通过实现 Parcelable 接口，可以方便地在不同组件之间传递这些数据。
// 在 Android 中，Parcelable 接口常用于通过 Intent 或 Bundle 传递对象数据。

public class NotebookBean implements Parcelable {
    public static int number;
    private String username;
    private int notebookId;     // 笔记本的唯一标识符
    private String content;     // 笔记本的内容
    private long editTime;      // 笔记本的编辑时间
    private  int likes;         // 笔记本的点赞数量
    private  int likeSpecial;
    public int CommentNum;
    private int post_id;


    public NotebookBean(){

    }
    public void setPost_id(int id){
        this.post_id=id;
    }
    public int getPost_id(){
        return this.post_id;
    }
    public void setCommentNum(int comment){
        this.CommentNum=comment;
    }
    public int getCommentNum(){
        return this.CommentNum;
    }
    public void setLikeSpecial(int likeS){
        this.likeSpecial=likeS;
    }
    public int getLikeSpecial(){
        return this.likeSpecial;
    }

   public static void setNumber(int num){
        number=num;
   }
   public static int getNumber(){
        return number;
   }
    // 通过 Parcel 对象构造 NotebookBean 对象。从 Parcel 中读取数据并赋值给相应的成员变量。
    protected NotebookBean(Parcel in) {
        notebookId = in.readInt();
        content = in.readString();
        editTime = in.readLong();
        likes=in.readInt();
        likeSpecial=in.readInt();
    }

    public static final Creator<NotebookBean> CREATOR = new Creator<NotebookBean>() {
        @Override
        public NotebookBean createFromParcel(Parcel in) {
            return new NotebookBean(in);
        }

        @Override
        public NotebookBean[] newArray(int size) {
            return new NotebookBean[size];
        }
    };

    public int getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(int notebookId) {
        this.notebookId = notebookId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getEditTime() {
        return editTime;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public String getUsername(){
        return this.username;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }

    // @Override 注解的 describeContents 和 writeToParcel 方法，用于支持 Parcelable 接口。
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(notebookId);
        parcel.writeString(content);
        parcel.writeLong(editTime);
        parcel.writeInt(likes);
        parcel.writeInt(likeSpecial);
    }
}
