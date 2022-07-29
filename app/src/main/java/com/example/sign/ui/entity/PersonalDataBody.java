package com.example.sign.ui.entity;

public class PersonalDataBody {
    public String avatar;
    public String collegeName;
    public String email;
    public String gender;
    public int id;
    public int idNumber;
    public String phone;
    public String realName;
    public String userName;

    public PersonalDataBody(String avatar, String collegeName, String email, String gender, int id, int idNumber, String phone, String realName, String userName) {
        this.avatar = avatar;
        this.collegeName = collegeName;
        this.email = email;
        this.gender = gender;
        this.id = id;
        this.idNumber = idNumber;
        this.phone = phone;
        this.realName = realName;
        this.userName = userName;
    }


    public PersonalDataBody(String collegeName, String email, int id, String phone, String realName) {
        this.collegeName = collegeName;
        this.email = email;
        this.id = id;
        this.phone = phone;
        this.realName = realName;
    }
}
