package com.example.amprime.firebaseauth.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.model.Players
import kotlinx.android.synthetic.main.layout_player_list.view.*

class PlayerAdapter(private var context: Context, private var arrayList: ArrayList<Players>) : androidx.recyclerview.widget.RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_player_list, parent, false)
        return PlayerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder!!.playerName.text = arrayList[position].playerName
    }

    inner class PlayerViewHolder(itemView: View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView!!) {
        val playerName = itemView?.findViewById<TextView>(R.id.player_name)!!
    }
}