package com.example.amprime.firebaseauth.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.amprime.firebaseauth.fragments.ListOfUsers
import com.example.amprime.firebaseauth.fragments.WeatherForecastFragment

class ViewPageAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return WeatherForecastFragment()
            }
            1 -> {
                return null
            }
            2 -> {
                return ListOfUsers()
            }
        }
        return null

    }

    override fun getCount(): Int {
        return 3
    }
}