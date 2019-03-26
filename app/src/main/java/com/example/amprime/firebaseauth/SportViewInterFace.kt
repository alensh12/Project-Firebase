package com.example.amprime.firebaseauth

import com.example.amprime.firebaseauth.model.BbcSportsNewModel

interface SportViewInterFace {
    fun displayNews(sportsNewModel: BbcSportsNewModel)
    fun displayError()
}