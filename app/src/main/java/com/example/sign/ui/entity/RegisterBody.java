package com.example.sign.ui.entity;

public class RegisterBody {
    public String password;
    public String userName;
    public int roleId;

    public RegisterBody(String password, String userName, int roleId) {
        this.password = password;
        this.userName = userName;
        this.roleId = roleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
