package com.example.amprime.firebaseauth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by amprime on 10/27/17.
 */

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    EditText changePassword, reChangePassword;
    Button changeButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_layout);
        auth = FirebaseAuth.getInstance();
        changePassword = findViewById(R.id.change_password_field);
        reChangePassword = findViewById(R.id.re_changePassword);
        changeButton = findViewById(R.id.change_password_button);

        changeButton.setOnClickListener(this);
        SharedPreferences preferences = getSharedPreferences("my_pref",MODE_PRIVATE);
        String role = preferences.getString("role","");
        Log.d("Role: ",role);


    }

    @Override
    public void onClick(View v) {

        checkValidChangedPassword();

    }

    private void checkValidChangedPassword() {
        String cPassword = changePassword.getText().toString().trim();
        String rcPassword = reChangePassword.getText().toString().trim();

        if (!TextUtils.equals(cPassword, rcPassword)) {

            reChangePassword.setError("Not Matched");
        } else if (TextUtils.isEmpty(cPassword)) {
            changePassword.setError("Empty");
        } else if (TextUtils.isEmpty(rcPassword)) {
            reChangePassword.setError("Empty");

        }
        else if(!isPasswordEnough(cPassword)){
            changePassword.setError("Not Enough Character");
        }
        else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.updatePassword(changePassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(ChangePassword.this, "Password changed", Toast.LENGTH_SHORT).show();
                           // auth.signOut();
                          //   finish();
                            startActivity(new Intent(getApplicationContext(), EmailAndPasswordActivity.class));
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(ChangePassword.this, "failed", Toast.LENGTH_SHORT).show();

                        }
                        finish();
                    }

                });
            }
        }
    }

    private boolean isPasswordEnough(String cPassword) {
        return cPassword.length()>5;
    }
}
