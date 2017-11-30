package com.example.amprime.firebaseauth.Retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amprime on 11/14/17.
 */

public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofit(String BaseUrl){
        if(retrofit==null){
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}