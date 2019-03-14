package com.example.amprime.firebaseauth.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.util.SparseBooleanArray
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.example.amprime.firebaseauth.helper.FlipAnimator
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.model.User

import java.util.ArrayList
import java.util.Collections.addAll

/**
 * Created by amprime on 10/26/17.
 */

class UserAdapter(private val context: Context, internal var userArrayList: ArrayList<User>, private val adapterlistner: MessageAdapterlistner) : androidx.recyclerview.widget.RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    internal var getAllUser: ArrayList<User>? = null
    private val selectItemIndex: SparseBooleanArray
    // array used to perform multiple animation at once
    private val animationItemsIndex: SparseBooleanArray

    private var reverseAllAnimations = false

    val selectedItemCount: Int
        get() = selectItemIndex.size()

    val selectedItem: List<Int>
        get() {
            val item = ArrayList<Int>(selectItemIndex.size())
            for (i in 0 until selectItemIndex.size()) {
                item.add(selectItemIndex.keyAt(i))
            }
            return item
        }


    init {
        selectItemIndex = SparseBooleanArray()
        animationItemsIndex = SparseBooleanArray()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_list_user, null)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userArrayList[position]

        holder.usernamelist.text = user.fullname
        holder.usernameEmailID.text = user.address
        holder.userDesignation.text = user.designation
        holder.registeredDate.text = user.date.toString()
        holder.registeredTime.text = user.time.toString()
        // displaying first letter from the Text icon
        holder.iconText.text = user.fullname.substring(0, 1)

        if (selectItemIndex.get(position, false)) {

        }

        holder.itemView.isActivated = selectItemIndex.get(position, false)
        applyIconAnimation(holder, position)

        //apply icon Animation


        applyClickEvents(holder, position)

        Log.d("itemchagen", position.toString())
    }

    private fun applyIconAnimation(holder: UserViewHolder, position: Int) {
        Log.d("visiblity $position", holder.iconFront.visibility.toString())

        val selected = selectItemIndex.get(position, false)

        if (selected) {
            val flip = if (holder.iconFront.visibility == View.VISIBLE) true else false

            holder.iconFront.visibility = View.GONE
            resetYaxis(holder.iconBack)
            holder.iconBack.visibility = View.VISIBLE
            holder.iconBack.alpha = 1f

            if (flip) {
                //if(currentSelectedIndex==position){
                FlipAnimator.flipView(context, holder.iconBack, holder.iconFront, true)
                resetIndex()
            }
        } else {
            val flip = if (holder.iconBack.visibility == View.VISIBLE) true else false

            holder.iconBack.visibility = View.GONE
            resetYaxis(holder.iconFront)
            holder.iconFront.visibility = View.VISIBLE
            holder.iconBack.alpha = 1f
            if (flip) {
                // if(reverseAllAnimations&&animationItemsIndex.get(position,false)||currentSelectedIndex==position){
                FlipAnimator.flipView(context, holder.iconBack, holder.iconFront, false)
                resetIndex()
            }
        }
    }

    fun selectedAll() {
        addAll(getAllUser)
        notifyDataSetChanged()
    }

    private fun resetYaxis(view: View) {
        if (view.rotationY != 0f) {
            view.rotationY = 0f
        }
    }

    private fun applyClickEvents(holder: UserViewHolder, position: Int) {
        holder.iconContainer.setOnClickListener {
            adapterlistner.iconClick(position)
            Log.d("TAG", "iconContainer()")
        }
        holder.cardView_container.setOnClickListener { adapterlistner.MessageRowClicked(position) }
        holder.cardView_container.setOnLongClickListener { v ->
            adapterlistner.onRowLongClicked(position)
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            true
        }
    }

    override fun getItemId(position: Int): Long {
        return java.lang.Long.parseLong(userArrayList[position].fullname)
    }

    override fun getItemCount(): Int {
        return userArrayList.size

    }

    fun toggleSelection(position: Int) {
        currentSelectedIndex = position
        if (selectItemIndex.get(position, false)) {
            selectItemIndex.delete(position)
            Log.d("Tag", "deleted")
        } else {
            selectItemIndex.put(position, true)

            Log.d("Tag", "")

        }
        notifyItemChanged(position)
    }

    fun resetIndex() {
        currentSelectedIndex = 0
    }

    fun clearSelection() {
        reverseAllAnimations = true
        selectItemIndex.clear()
        animationItemsIndex.clear()
        notifyDataSetChanged()
    }

    fun removeUser(position: Int) {
        userArrayList.removeAt(position)

        resetIndex()
    }

    fun selectUser(position: Int) {
        userArrayList[position]
    }

    interface MessageAdapterlistner {
        fun MessageRowClicked(position: Int)

        fun iconClick(position: Int)

        fun onRowLongClicked(position: Int)
    }

    inner class UserViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnLongClickListener {
        internal var usernamelist: TextView
        internal var usernameEmailID: TextView
        internal var registeredDate: TextView
        internal var registeredTime: TextView
        internal var iconText: TextView
        internal var userDesignation: TextView
        internal var cardView_container: LinearLayout
        internal var iconContainer: RelativeLayout
        internal var iconFront: RelativeLayout
        internal var iconBack: RelativeLayout

        init {
            usernamelist = itemView.findViewById(R.id.RV_username)
            usernameEmailID = itemView.findViewById(R.id.RV_userEmail)
            registeredDate = itemView.findViewById(R.id.RV_date)
            registeredTime = itemView.findViewById(R.id.RV_time)
            userDesignation = itemView.findViewById(R.id.RV_designation)
            cardView_container = itemView.findViewById(R.id.container)
            iconText = itemView.findViewById(R.id.icon_text)
            iconFront = itemView.findViewById(R.id.icon_front)
            iconBack = itemView.findViewById(R.id.icon_back)
            iconContainer = itemView.findViewById(R.id.icon_container)
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View): Boolean {
            adapterlistner.onRowLongClicked(adapterPosition)
            //The user has performed a long press on an object that is resulting in an action being performed.
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            return false
        }
    }

    companion object {

        private var currentSelectedIndex = -1
    }
}
