package com.example.amprime.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by amprime on 10/27/17.
 */

public class ResetPasswordActivity extends AppCompatActivity {
    EditText resetEmail;
    Button resetButton;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);

        resetEmail = findViewById(R.id.reset_password);
        resetButton = findViewById(R.id.send_reset_email);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                final String email = resetEmail.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
                   resetEmail.setError("empty");


                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), EmailAndPasswordActivity.class));
                                finish();
                            }
                            if (!task.isSuccessful()) {
                                if (!isvalidEmail(email)) {
                                    resetEmail.setError("Invalid email Address");

                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "Username Not Registered", Toast.LENGTH_SHORT).show();

                                }
                                resetEmail.setText("");
                            }
                        }

                        private boolean isvalidEmail(String email) {
                            return email.matches(String.valueOf(Patterns.EMAIL_ADDRESS));
                        }
                    });


                } else if (TextUtils.isEmpty(email)) {
                    Log.d("TAg", "empty " + TextUtils.isEmpty(email));
                    resetEmail.setError("email required");
                }
            }
        });


        }
}
