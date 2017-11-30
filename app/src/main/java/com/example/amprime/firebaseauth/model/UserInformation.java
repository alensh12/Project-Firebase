package com.example.amprime.firebaseauth.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by amprime on 10/18/17.
 */

public class UserInformation {
    public String fullname;
    private String imgUrl;
    public String address;
    public int age;
    public String country;
    public String gender;
    private String date;
    private String time;
    private String token;
    private String emailId;
    private String userType;

    private String editedUserDesignation;
    private String editedUserAddress;
    Object createdTimestamp;

    public UserInformation() {
    }


    public UserInformation(String Fullname
            , String address
            , int age
            , String country
            , String Gender
            , String ImgUrl
            , String date
            , String time
            , String token
            , String emailId
            , String userType
            ) {
        this.fullname = Fullname;
        this.address = address;
        this.age = age;
        this.token =token;
        this.country = country;
        this.gender = Gender;
        this.imgUrl = ImgUrl;
        this.date = date;
        this.time = time;
        this.emailId = emailId;
        this.userType = userType;

        createdTimestamp = ServerValue.TIMESTAMP;

    }

    public UserInformation(String userDesignation, String userAddress) {
    this.editedUserDesignation =userDesignation;
    this.editedUserAddress = userAddress;
    }

    public String getEditedUserDesignation() {
        return editedUserDesignation;
    }

    public void setEditedUserDesignation(String editedUserDesignation) {
        this.editedUserDesignation = editedUserDesignation;
    }

    public String getEditedUserAddress() {
        return editedUserAddress;
    }

    public void setEditedUserAddress(String editedUserAddress) {
        this.editedUserAddress = editedUserAddress;
    }

    @Exclude
    public long getCreatedTimestampLong(){
        return (long)createdTimestamp;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //public Map<String, Boolean> mapping= new HashMap<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }



}
