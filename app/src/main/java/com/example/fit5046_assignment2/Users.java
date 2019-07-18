package com.example.fit5046_assignment2;

import java.util.Date;

public class Users
{
    private Integer userId;
    private String userFirstname;
    private String userSurname;
    private String userEmail;
    private Date userDob;
    private Double userHeight;
    private Double userWeight;
    private String userGender;
    private String userAddress;
    private Integer userPostcode;
    private Integer levelOfActivity;
    private Double stepsPerMile;

    public Users(Integer userId, String firstName, String lastName, String email, Date dob, Double height, Double weight, String gender, String address, Integer postcode, Integer levelOfActivity, Double stepsPerMile)
    {
        this.userId = userId;
        this.userFirstname = firstName;
        this.userSurname = lastName;
        this.userEmail = email;
        this.userDob = dob;
        this.userHeight = height;
        this.userWeight = weight;
        this.userGender = gender;
        this.userAddress = address;
        this.userPostcode = postcode;
        this.levelOfActivity = levelOfActivity;
        this.stepsPerMile = stepsPerMile;
    }

    public Users()
    {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getUserDob() {
        return userDob;
    }

    public void setUserDob(Date userDob) {
        this.userDob = userDob;
    }

    public Double getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(Double userHeight) {
        this.userHeight = userHeight;
    }

    public Double getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(Double userWeight) {
        this.userWeight = userWeight;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Integer getUserPostcode() {
        return userPostcode;
    }

    public void setUserPostcode(Integer userPostcode) {
        this.userPostcode = userPostcode;
    }

    public Integer getLevelOfActivity() {
        return levelOfActivity;
    }

    public void setLevelOfActivity(Integer levelOfActivity) {
        this.levelOfActivity = levelOfActivity;
    }

    public Double getStepsPerMile() {
        return stepsPerMile;
    }

    public void setStepsPerMile(Double stepsPerMile) {
        this.stepsPerMile = stepsPerMile;
    }
}
