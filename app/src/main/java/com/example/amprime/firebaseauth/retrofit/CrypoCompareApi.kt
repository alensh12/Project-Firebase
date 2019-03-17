package com.example.amprime.firebaseauth.retrofit

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CrypoCompareApi {

    companion object {
        private val BASE_URL = "https://min-api.cryptocompare.com/"
        private var retrofit: Retrofit? = null
        fun getClient(): Retrofit? {
            retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
            return retrofit
        }
    }

}