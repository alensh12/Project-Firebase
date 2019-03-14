package com.example.amprime.firebaseauth.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LeagueFootballModel {
    @SerializedName("success")
    @Expose
    var success: Int? = null
    @SerializedName("result")
    @Expose
    var result: List<Result>? = null

    inner class Result {

        @SerializedName("league_key")
        @Expose
        var leagueKey: String? = null
        @SerializedName("league_name")
        @Expose
        var leagueName: String? = null
        @SerializedName("country_key")
        @Expose
        var countryKey: String? = null
        @SerializedName("country_name")
        @Expose
        var countryName: String? = null

    }
}
