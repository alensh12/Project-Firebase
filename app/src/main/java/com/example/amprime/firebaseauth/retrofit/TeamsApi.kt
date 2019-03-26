package com.example.amprime.firebaseauth.retrofit

import com.example.amprime.firebaseauth.helper.Constant
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TeamsApi {
    companion object {
        fun getTeamClient(): Retrofit? {
            return Retrofit.Builder()
                    .baseUrl(Constant.SOCCER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }

    }
}
