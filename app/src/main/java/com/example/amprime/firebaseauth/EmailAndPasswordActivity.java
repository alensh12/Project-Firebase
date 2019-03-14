package com.example.amprime.firebaseauth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import androidx.annotation.NonNull;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class EmailAndPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG =EmailAndPasswordActivity.class.getSimpleName() ;
    private FirebaseAuth mAuth;
    private CircularProgressButton SignInButton;
    private Button RegisterUserButton;
    private EditText usernameText;
    private EditText passwordText;
    private TextView forgotPasswordText;
    private TextView SignInSignOut;
    private ProgressDialog progressDialog;
    private ConnectivityManager connectivityManager;
    private ImageView googleImage;
    private ImageView fbImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_and_pasword);
        progressDialog = new ProgressDialog(this);
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        Log.d(TAG,"timestamp"+ ts);


        viewsAndButton();

        /*** Initialization of FirebaseAuth **/
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        Log.d("TAG","here");

        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);


        boolean isLogin = preferences.getBoolean("islogin",false);
        Log.d("login", String.valueOf(isLogin));
        if(isLogin){
            startActivity(new Intent(getApplicationContext(), ProfileInformationActivity.class));
        }
       

     super.onResume();
    }

    private void viewsAndButton() {
        usernameText = findViewById(R.id.userField);
        passwordText = findViewById(R.id.passwordField);
        SignInButton = findViewById(R.id.sign_in_button);
        RegisterUserButton = findViewById(R.id.register_user_button);

        forgotPasswordText = findViewById(R.id.forgot_password_text);
        SignInSignOut = findViewById(R.id.sign_in_sign_out);
        SignInButton.setOnClickListener(this);
        RegisterUserButton.setOnClickListener(this);
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(),ResetPasswordActivity.class);
                        startActivity(intent);

            }
        });


        googleImage = findViewById(R.id.google_login);
        googleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Toast.makeText(getApplicationContext(),"Activity under construction",Toast.LENGTH_SHORT).show();
            }
        });
        fbImage = findViewById(R.id.fb_login);



    }

    @Override
    public void onClick(View v) {
    if(v == SignInButton){
        SignInButton.startAnimation();
        existingAccount();

    }

    if(v == RegisterUserButton){
        try {
            createUserAccount();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    }


    /**Sign with existing User**/
    private void existingAccount() {
        final String email = usernameText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();
        progressDialog.setMessage("Logging in");
        progressDialog.show();
        try {
            if (TextUtils.isEmpty(email)) {
               // Toast.makeText(this, "username empty", Toast.LENGTH_SHORT).show();
                usernameText.setError("username empty");
                progressDialog.dismiss();
            }
            if (TextUtils.isEmpty(password)) {
               // Toast.makeText(this, "password not entered", Toast.LENGTH_SHORT).show();
                passwordText.setError("password not entered");
                progressDialog.dismiss();
            }

            else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    SignInSignOut.setText("Successfully Signed In");
                                    SignInSignOut.setTextColor(Color.GREEN);
                                    progressDialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), ProfileInformationActivity.class));
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("islogin",true);
                                    editor.commit();
                                    Log.d("TAG", "user_email " + user.getEmail());
                                    Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                                } else if(!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected())){
                                    Toast.makeText(EmailAndPasswordActivity.this, "No Internet Access", Toast.LENGTH_SHORT).show();
                                }

                                    else{

                                    //startActivity(new Intent(getApplicationContext(), EmailAndPasswordActivity.class));
                                    SignInSignOut.setText("Failed");
                                    SignInSignOut.setTextColor(Color.RED);
                                    Log.w("TAG", "signInWithEmail:failed", task.getException());
                                    Toast.makeText(EmailAndPasswordActivity.this, "Incorrect Password/Username",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                            }
                        });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /***Sign up new users**/
    private void createUserAccount()throws Exception {
        checkNetworkConnectivity();
        Log.d("TAG","here");

    }

    /***Check Network is Connected or not ***/
    private void checkNetworkConnectivity() {
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
////            Intent intent = new Intent(this,RegistrationUserActivity.class);
//            Log.d("TAG",""+intent);
//            startActivity(intent);
        }
        else {
//            Toast.makeText(getApplicationContext(),"No Internet Access",Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(this,RegistrationUserActivity.class);
//            Log.d("TAG",""+intent);
//            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }
}
