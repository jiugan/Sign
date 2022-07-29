package com.example.sign.ui.entity;

public class SignDataBody {
    public int beginTime;
    public String courseAddr;
    public int courseId;
    public String courseName;
    public int endTime;
    public int signCode;
    public int total;
    public int userId;

    public SignDataBody(int beginTime, String courseAddr, int courseId, String courseName, int endTime, int signCode, int total, int userId) {
        this.beginTime = beginTime;
        this.courseAddr = courseAddr;
        this.courseId = courseId;
        this.courseName = courseName;
        this.endTime = endTime;
        this.signCode = signCode;
        this.total = total;
        this.userId = userId;
    }
}
