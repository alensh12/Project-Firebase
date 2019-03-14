package com.example.amprime.firebaseauth

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.amprime.firebaseauth.adapter.PlayerAdapter
import com.example.amprime.firebaseauth.adapter.SportNewsAdapter
import com.example.amprime.firebaseauth.fragments.FootballFragment
import com.example.amprime.firebaseauth.fragments.ListOfUsers
import com.example.amprime.firebaseauth.fragments.SportsNewsFragment
import com.example.amprime.firebaseauth.fragments.WeatherForecastFragment
import com.example.amprime.firebaseauth.model.BbcSportsNewModel
import com.example.amprime.firebaseauth.model.Players
import com.example.amprime.firebaseauth.model.TeamInfoModel
import com.example.amprime.firebaseauth.model.TeamListModel
import com.google.gson.Gson
import java.util.*
import android.content.Intent


class ListUserMainActivity : AppCompatActivity(), FootballFragment.OnCallBack {
    var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user_main)
        val valueFragment = intent.getIntExtra("FootballFragment", 0)
        if (valueFragment == 1) {
            openFragment(FootballFragment.newInstance())
        } else {
            openFragment(WeatherForecastFragment.newInstance())
        }

        bottomNavigationView = this.findViewById(R.id.navigationView)
        bottomNavigationView!!.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.weather_report -> {
                    Log.e("bottomnav", "logout")
                    val mainUserFragment = WeatherForecastFragment.newInstance()
                    openFragment(mainUserFragment)
                }
                R.id.football_detail -> {
                    val footballFragment = FootballFragment.newInstance()
                    openFragment(footballFragment)
                    Log.e("bottomnav", "changePass")
                }
                R.id.list_of_users -> {
                    val listofUserFragment = ListOfUsers.newInstance()
                    openFragment(listofUserFragment)
                }
                R.id.sports_detail -> {
                    val sportsNewFragment = SportsNewsFragment.newInstance()
                    openFragment(sportsNewFragment)
                }

            }
            return@setOnNavigationItemSelectedListener true
        }

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
//                .addToBackStack("fragment_home")
                .commit()
    }

    override fun onBackPressed() {
        Log.e("back", "backpressed")
        if (bottomNavigationView?.selectedItemId == R.id.weather_report) {
            super.onBackPressed()
            finish()
        } else {
            bottomNavigationView?.selectedItemId = R.id.weather_report
        }
    }

    override fun update(teamKey: String) {
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra("FootballFragment", 1)
        finish()
        startActivity(intent)

    }

}