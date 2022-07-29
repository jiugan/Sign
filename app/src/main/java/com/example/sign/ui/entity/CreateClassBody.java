package com.example.sign.ui.entity;

public class CreateClassBody {
    public String collegeName;
    public String courseName;
    public String coursePhoto;
    public int endTime;
    public String introduce;
    public String realName;
    public int startTime;
    public int userId;
    public String userName;

    public CreateClassBody(String collegeName, String courseName, String coursePhoto, int endTime, String introduce, String realName, int startTime, int userId, String userName) {
        this.collegeName = collegeName;
        this.courseName = courseName;
        this.coursePhoto = coursePhoto;
        this.endTime = endTime;
        this.introduce = introduce;
        this.realName = realName;
        this.startTime = startTime;
        this.userId = userId;
        this.userName = userName;
    }
}
