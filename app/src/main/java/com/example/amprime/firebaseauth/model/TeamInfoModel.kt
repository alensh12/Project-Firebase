package com.example.amprime.firebaseauth.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.example.amprime.firebaseauth.model.TeamListModel.Coach
import com.example.amprime.firebaseauth.model.TeamListModel.Player




class TeamInfoModel {

    @SerializedName("success")
    @Expose
    var success: Int? = null
    @SerializedName("result")
    @Expose
    var result: List<Result>? = null

    inner class Result {

        @SerializedName("team_key")
        @Expose
        var teamKey: String? = null
        @SerializedName("team_name")
        @Expose
        var teamName: String? = null
        @SerializedName("team_logo")
        @Expose
        var teamLogo: String? = null
        @SerializedName("players")
        @Expose
        var players: List<Player>? = null
        @SerializedName("coaches")
        @Expose
        var coaches: List<Coach>? = null

         inner class Player {

            @SerializedName("player_key")
            @Expose
            var playerKey: Long? = null
            @SerializedName("player_name")
            @Expose
            var playerName: String? = null
            @SerializedName("player_number")
            @Expose
            var playerNumber: String? = null
            @SerializedName("player_country")
            @Expose
            var playerCountry: String? = null
            @SerializedName("player_type")
            @Expose
            var playerType: String? = null
            @SerializedName("player_age")
            @Expose
            var playerAge: String? = null
            @SerializedName("player_match_played")
            @Expose
            var playerMatchPlayed: String? = null
            @SerializedName("player_goals")
            @Expose
            var playerGoals: String? = null
            @SerializedName("player_yellow_cards")
            @Expose
            var playerYellowCards: String? = null
            @SerializedName("player_red_cards")
            @Expose
            var playerRedCards: String? = null

        }

        inner class Coach {

            @SerializedName("coach_name")
            @Expose
            var coachName: String? = null
            @SerializedName("coach_country")
            @Expose
            var coachCountry: String? = null
            @SerializedName("coach_age")
            @Expose
            var coachAge: String? = null

        }

    }

}