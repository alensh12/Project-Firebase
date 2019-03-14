package com.example.amprime.firebaseauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.amprime.firebaseauth.fragments.FootballFragment

class TeamInfoActivity: AppCompatActivity(),FootballFragment.OnCallBack {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_info)

    }
    override fun update(teamKey: String) {
        Log.e("key", teamKey)
    }
}