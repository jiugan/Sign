package com.example.sign.ui.entity;

public class LoginResponse {
    public int code;
    public String msg;
    public Data data;

    public class Data{
        public String id;
        public String appKey;
        public String userName;
        public int roleId;
        public String realName;
        public String idNumber;
        public String collegeName;
        public String gender;
        public String phone;
        public String email;
        public String avatar;
        public String inSchoolTime;
        public String createTime;
        public String lastUpdateTime;
    }
}
