package com.example.amprime.firebaseauth

import com.example.amprime.firebaseauth.model.TeamListModel

interface TeamViewInterface {
    fun showTeamInfo(teamListModel: TeamListModel)
    fun showError()
}