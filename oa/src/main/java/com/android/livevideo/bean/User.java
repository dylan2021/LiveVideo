package com.android.livevideo.bean;

import java.io.Serializable;

/**
 * Gool Lee
 */
public class User implements Serializable {

    public int id;
    public String userCode;
    public String nickName;
    public String loginName;
    public String password;
    public String phoneNumber;
    public String gender;
    public String headPortrait;
    public int age;
    public long registerTime;
    public String qqNumber;
    public String weChat;
    public String email;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userCode='" + userCode + '\'' +
                ", username='" + nickName + '\'' +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                ", age=" + age +
                ", registerTime=" + registerTime +
                ", qqNumber=" + qqNumber +
                ", email='" + email + '\'' +
                '}';
    }
}
