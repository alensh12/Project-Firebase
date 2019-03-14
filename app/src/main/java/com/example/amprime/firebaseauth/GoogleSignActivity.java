package com.example.amprime.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by amprime on 10/16/17.
 */

public class GoogleSignActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 100 ;
    private Button googleSignButton;
    private TextView view;

    Long tsLong = System.currentTimeMillis();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlesign);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
            

        
        
        view = findViewById(R.id.date);

          googleSignButton = findViewById(R.id.google_sign_in);
          googleSignButton.setOnClickListener(this);


        String ts = tsLong.toString();
        Log.d("date",ts);

        try {
            view.setText(getDate(tsLong));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private String getDate(long time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, ''yy");
        Date netDate = new Date(time);
        String formattedDate= formatter.format(netDate);
        Log.d("formatted date",formattedDate);
        return formattedDate;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Activity Under Construction", Toast.LENGTH_SHORT).show();
    }
}