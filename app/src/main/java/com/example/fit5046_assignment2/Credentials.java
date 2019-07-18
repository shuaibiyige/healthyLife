package com.example.fit5046_assignment2;

import java.util.Date;

public class Credentials
{

    private Integer userId;
    private String userName;
    private String passwordHash;
    private Date signUpDate;

    public Credentials(Integer userId, String username, String passwordHash, Date sign_up_date) {
        this.userId = userId;
        this.userName = username;
        this.passwordHash = passwordHash;
        this.signUpDate = sign_up_date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getSign_up_date() {
        return signUpDate;
    }

    public void setSign_up_date(Date sign_up_date) {
        this.signUpDate = sign_up_date;
    }
}
