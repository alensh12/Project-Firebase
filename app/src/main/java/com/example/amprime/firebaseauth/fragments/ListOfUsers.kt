package com.example.amprime.firebaseauth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.amprime.firebaseauth.EmailAndPasswordActivity
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.adapter.UserAdapter
import com.example.amprime.firebaseauth.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListOfUsers : Fragment(), UserAdapter.MessageAdapterlistner {
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    lateinit var userAdapter: UserAdapter
    private val entries = arrayListOf<User>()
    private lateinit var mlistener: FirebaseAuth.AuthStateListener
    private var mAuth: FirebaseAuth? = null
    private var databaseReference: Query? = null


    companion object {
        fun newInstance(): ListOfUsers = ListOfUsers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list_user_recycler_view)
        userAdapter = UserAdapter(this.activity!!, entries, this)
        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        mlistener = FirebaseAuth.AuthStateListener {
            if (mAuth!!.currentUser == null) {
                startActivity(Intent(this.activity, EmailAndPasswordActivity::class.java))
            } else {
                getUser()
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_user, container, false)
    }

    private fun getUser() {
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val items: Iterator<DataSnapshot> = p0.children.iterator()
                entries.clear()
                while (items.hasNext()) {
                    val item: DataSnapshot = items.next()
                    val date: String
                    val time: String
                    val token: String
                    val designation: String
                    val uid = item.key
                    val username = item.child("fullname").value.toString()
                    val address = item.child("address").value.toString()
                    date = item.child("date").value.toString()
                    time = item.child("time").value.toString()
                    token = item.child("token").value.toString()
                    designation = item.child("userType").value.toString()
                    entries.add(User(uid!!, username, address, date, time, token, designation))

                }
                entries.reverse()
                recyclerView.apply {
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                    recyclerView.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(recyclerView.context, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
                    adapter = userAdapter
                    (adapter as UserAdapter).notifyDataSetChanged()

                }

            }

        })
    }

    override fun MessageRowClicked(position: Int) {

    }

    override fun iconClick(position: Int) {
        userAdapter.toggleSelection(position)
    }


    override fun onRowLongClicked(position: Int) {
    }

    override fun onStart() {
        mAuth!!.addAuthStateListener(mlistener)
        super.onStart()
    }

    override fun onStop() {
        mAuth!!.addAuthStateListener(mlistener)
        super.onStop()
    }
}