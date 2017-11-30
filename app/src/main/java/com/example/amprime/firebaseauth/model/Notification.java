package com.example.amprime.firebaseauth.model;

import java.io.StringBufferInputStream;

/**
 * Created by amprime on 11/14/17.
 */

public class Notification {
    public String title;
    public String body;

    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
