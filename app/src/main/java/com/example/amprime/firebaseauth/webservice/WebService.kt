package com.example.amprime.firebaseauth.webservice

import com.example.amprime.firebaseauth.model.BbcSportsNewModel
import com.example.amprime.firebaseauth.model.CountryFootballModel
import com.example.amprime.firebaseauth.model.TeamListModel
import com.example.amprime.firebaseauth.model.WeatherModel
import retrofit2.Call
import retrofit2.http.*

class WebService {
    interface WeatherWebService {
        @GET("/data/2.5/weather")
        fun fetchWeatherData(
                @Query("q") city: String,
                @Query("appid") apiKey: String
        ): Call<WeatherModel>
    }

    interface LiveScoreWebService {
        @GET("/api/football")
        fun fetchCountryData(
                @Query("met") country: String,
                @Query("APIkey") apiKey: String
        ): Call<CountryFootballModel>
    }
    interface TeamListWebSevice{
        @GET("/api/football")
        fun fetchTeamData(
                @Query("met") teams:String,
                @Query("teamId") leagueId:String,
                @Query("APIkey") apiKey: String

        ):Call<TeamListModel>
    }
    interface BbcSportsWebService{
        @GET("/v2/top-headlines")
        fun fetchSportNews(
                @Query("sources") source:String,
                @Query("apiKey") apiKey: String
        ):Call<BbcSportsNewModel>
    }
}