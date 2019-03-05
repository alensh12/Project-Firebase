package com.example.amprime.firebaseauth

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.amprime.firebaseauth.fragments.ListOfUsers
import com.example.amprime.firebaseauth.fragments.MainUserFragment

class ListUserMainActivity : AppCompatActivity() {
    lateinit var actionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user_main)
//        actionBar = supportActionBar!!
        val bottomNavigationView: BottomNavigationView = this.findViewById(R.id.navigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.log_out_profile -> {
                    Log.e("bottomnav", "logout")
                    val mainUserFragment = MainUserFragment.newInstance()
                    openFragment(mainUserFragment)
                }
                R.id.change_password -> {
                    Log.e("bottomnav", "changePass")
                }
                R.id.list_of_users -> {
                    val listofUserFragment = ListOfUsers.newInstance()
                    openFragment(listofUserFragment)
                }

            }
            return@setOnNavigationItemSelectedListener true
        }

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }


}