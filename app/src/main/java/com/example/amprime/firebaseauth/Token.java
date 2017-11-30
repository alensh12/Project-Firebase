package com.example.amprime.firebaseauth;

import com.example.amprime.firebaseauth.Retrofit.APIService;
import com.example.amprime.firebaseauth.Retrofit.RetrofitClient;

/**
 * Created by amprime on 11/11/17.
 */

public class Token {
    public static String UserToken ="";

    private static String  baseUrl = "https://fcm.googleapis.com/";

    public static APIService getFCMClient(){

        return RetrofitClient.getRetrofit(baseUrl).create(APIService.class);
    }
}
