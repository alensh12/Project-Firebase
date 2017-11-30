package com.example.amprime.firebaseauth.model;

/**
 * Created by amprime on 11/14/17.
 */

public class Sender {
    public String to;
    public Notification notification;

    public Sender() {
    }

    public Sender(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }
}
