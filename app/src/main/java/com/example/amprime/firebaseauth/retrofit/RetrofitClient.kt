package com.example.amprime.firebaseauth.retrofit


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by amprime on 11/14/17.
 */

object RetrofitClient {
    private var retrofit: Retrofit? = null
    fun getRetrofit(BaseUrl: String): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build()

        }
        return retrofit
    }
}