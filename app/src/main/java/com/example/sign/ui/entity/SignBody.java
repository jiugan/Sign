package com.example.sign.ui.entity;

public class SignBody {
    int signCode;
    int userId;
    int userSignId;

    public SignBody(int signCode, int userId, int userSignId) {
        this.signCode = signCode;
        this.userId = userId;
        this.userSignId = userSignId;
    }
}
