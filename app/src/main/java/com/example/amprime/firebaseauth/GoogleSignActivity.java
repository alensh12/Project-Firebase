package com.example.amprime.firebaseauth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by amprime on 10/16/17.
 */

public class GoogleSignActivity extends AppCompatActivity {




    private TextView view;
    Long tsLong = System.currentTimeMillis();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlesign);

        view = findViewById(R.id.date);




        String ts = tsLong.toString();
        Log.d("date",ts);

        try {
            view.setText(getDate(tsLong));
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    private String getDate(long time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, ''yy");
        Date netDate = new Date(time);
        String formattedDate= formatter.format(netDate);
        Log.d("formated date",formattedDate);
        return formattedDate;
    }

}