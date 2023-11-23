package com.example.project_ee297.Object;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table (name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String user_account;
    private String content;
    private Timestamp timestamp;
    private  int like;
    private  int remark;
    private String LikeUserAccount;

    public String getLikeUserAccount() {
        return LikeUserAccount;
    }

    public void setLikeUserAccount(String likeUserAccount) {
        LikeUserAccount = likeUserAccount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public int getRemark() {
        return remark;
    }

    public void setRemark(int remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
