package com.example.sign.ui.entity;

public class StudentCourseDetailsResponse {
    public int code;
    public String msg;
    public show data;
    public class show{
        public int id;
        public String courseName;
        public String introduce;
        public String startTime;
        public String endTime;
        public String realName;
        public Boolean hasSelect;
    }
}
