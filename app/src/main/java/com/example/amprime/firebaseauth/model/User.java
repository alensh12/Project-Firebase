package com.example.amprime.firebaseauth.model;


public  class User implements Comparable<User>{
    public String uid;
    public String Fullname;

    public String Address;

    public Object date;
    public Object time;
    public String designation;

    public String token;

    public User() {
    }

    public String getFullname() {
        return Fullname;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User(String uid,String Fullname, String Address, Object date, Object time, String token,String designation) {
        this.Fullname = Fullname;
        this.uid = uid;
        this.Address = Address;

        this.date =date;

        this.time = time;

        this.designation = designation;

        this.token = token;


    }


    @Override
    public int compareTo(User o) {
        return compareTo((User) o.time);
    }
}
