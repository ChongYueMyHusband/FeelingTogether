package com.example.project_ee297.Object;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String account;
    private String password;
    private String headPortraitBase64Code;
    private int age;
    private String gender;
    private String username;
    private  String userIdiograph;



    public String getUserIdiograph() {
        return userIdiograph;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public void setSex(String sex) {
        gender = gender;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account=account;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public String getHeadPortraitBase64Code() {
        return headPortraitBase64Code;
    }

    public void setHeadPortraitBase64Code(String headPortraitBase64Code) {
        this.headPortraitBase64Code = headPortraitBase64Code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
// getters and setters
}
