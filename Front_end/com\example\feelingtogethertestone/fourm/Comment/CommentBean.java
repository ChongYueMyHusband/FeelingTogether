package com.example.fourm.Comment;


import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;


// 这是一个实现了 Parcelable 接口的 Java 类 NotebookBean，用于在 Android 应用中传递笔记本相关的数据。
// 这个类的设计目的是将笔记本的相关数据打包成一个可传递的对象，通过实现 Parcelable 接口，可以方便地在不同组件之间传递这些数据。
// 在 Android 中，Parcelable 接口常用于通过 Intent 或 Bundle 传递对象数据。

public class CommentBean implements Parcelable {
    public static int number;
    private String username;
    private int CommentId;     // 笔记本的唯一标识符
    private String content;     // 笔记本的内容
    private long editTime;      // 笔记本的编辑时间
    private  int likes;         // 笔记本的点赞数量
    private  int likeSpecial;
    private static List<Integer> likeCount=new ArrayList<Integer>();
    private static List<Integer>likenumIsv=new ArrayList<Integer>();
    public CommentBean(){

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
    protected CommentBean(Parcel in) {
        CommentId = in.readInt();
        content = in.readString();
        editTime = in.readLong();
        likes=in.readInt();
        likeSpecial=in.readInt();
    }

    public static final Creator<CommentBean> CREATOR = new Creator<CommentBean>() {
        @Override
        public CommentBean createFromParcel(Parcel in) {
            return new CommentBean(in);
        }

        @Override
        public CommentBean[] newArray(int size) {
            return new CommentBean[size];
        }
    };
    public static void removeItem(int position){
        likeCount.remove(position);
        likenumIsv.remove(position);
    }
    public static int getLikesIsv(int position){
        return likenumIsv.get(position);
    }
    public static void setLikesIsv(int position, int isVisit){
        likenumIsv.set(position,isVisit);
    }
    public int getNotebookId() {
        return CommentId;
    }

    public void setNotebookId(int notebookId) {
        this.CommentId = CommentId;
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
    public static int getLikeCount(int position){return likeCount.get(position);}
    public static void setLikeCount(int position, int number){likeCount.set(position,number);}
    // @Override 注解的 describeContents 和 writeToParcel 方法，用于支持 Parcelable 接口。
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(CommentId);
        parcel.writeString(content);
        parcel.writeLong(editTime);
        parcel.writeInt(likes);
        parcel.writeInt(likeSpecial);
    }
}