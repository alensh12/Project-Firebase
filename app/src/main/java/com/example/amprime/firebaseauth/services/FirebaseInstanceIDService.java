package com.example.amprime.firebaseauth.services;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.amprime.firebaseauth.helper.Token;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by amprime on 10/27/17.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
//        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
      //  Log.d("tag","refreshed token"+refreshedToken);
        if(!refreshedToken.isEmpty()){
            Log.d("Tag","here Token: "+refreshedToken);}
        else
        {
            Log.d("Tag","token Not created");
        }

        Token.UserToken = refreshedToken;


        //Send Token to server
  //          sendRefreshedToServer(refreshedToken);
        
        storeTokenServer(refreshedToken);

    }

    private void storeTokenServer(String refreshedToken) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("this reference",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Token",refreshedToken);
        editor.apply();
    }

    private void sendRefreshedToServer(final String Token) {
        Log.d("Token","Token"+Token);
    }
        
    

}
