package com.example.amprime.firebaseauth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.helper.TeamDataProvider
import com.example.amprime.firebaseauth.model.TeamNameModel
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder

class TeamsAdapter(var context: Context, var teamDataProvider: TeamDataProvider) : RecyclerView.Adapter<TeamsAdapter.TeamViewHolder>(), DraggableItemAdapter<TeamsAdapter.TeamViewHolder> {

    var itemMoveMode: Int = RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return teamDataProvider.mData[position].id
    }

    override fun onGetItemDraggableRange(holder: TeamViewHolder, position: Int): ItemDraggableRange? {
        return null
    }

    override fun onCheckCanStartDrag(holder: TeamViewHolder, position: Int, x: Int, y: Int): Boolean {
        return true
    }

    override fun onItemDragStarted(position: Int) {
        notifyDataSetChanged()
    }

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        when (itemMoveMode) {
            RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT -> {
                teamDataProvider.moveItem(fromPosition, toPosition)
            }
            else -> {
                teamDataProvider.swapItem(fromPosition, toPosition)
            }
        }
    }

    override fun onCheckCanDrop(draggingPosition: Int, dropPosition: Int): Boolean {
        return true
    }

    override fun onItemDragFinished(fromPosition: Int, toPosition: Int, result: Boolean) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_team, parent, false))
    }

    override fun getItemCount(): Int {
        return teamDataProvider.getCount()
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.teamText.text = teamDataProvider.getItem(position).text
    }

    inner class TeamViewHolder(itemView: View) : AbstractDraggableItemViewHolder(itemView) {
        val mainContainer: FrameLayout = itemView.findViewById(R.id.main_coontainer)
        val teamText: TextView = itemView.findViewById(R.id.teams)
        val draggableItem: View = itemView.findViewById(R.id.drag_handle)

    }
}