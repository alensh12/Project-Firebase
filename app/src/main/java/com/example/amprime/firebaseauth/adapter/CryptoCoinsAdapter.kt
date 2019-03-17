package com.example.amprime.firebaseauth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.model.CoinModel
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemState
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder


class CryptoCoinsAdapter(var context: Context, var coinList: ArrayList<CoinModel>) : RecyclerView.Adapter<CryptoCoinsAdapter.CcViewHolder>(), DraggableItemAdapter<CryptoCoinsAdapter.CcViewHolder> {
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return coinList[position].id
    }

    override fun onGetItemDraggableRange(holder: CcViewHolder, position: Int): ItemDraggableRange? {
        return null
    }

    override fun onCheckCanStartDrag(holder: CcViewHolder, position: Int, x: Int, y: Int): Boolean {
//        var itemView: View = holder.itemView
//        var dragItem: View = holder.dragView
//        val handleWidth = dragItem.width
//        val handleHeight = dragItem.height
//        val handleLeft = dragItem.left
//        val handleTop = dragItem.top
//
//        return x >= handleLeft && x < handleLeft + handleWidth &&
//                y >= handleTop && y < handleTop + handleHeight
        return true

    }

    override fun onItemDragStarted(position: Int) {
        notifyDataSetChanged()
    }

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        val removed: CoinModel = coinList.removeAt(fromPosition)
        coinList.add(toPosition, removed)
    }

    override fun onCheckCanDrop(draggingPosition: Int, dropPosition: Int): Boolean {
        return true
    }

    override fun onItemDragFinished(fromPosition: Int, toPosition: Int, result: Boolean) {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CcViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.activity_list_coin, parent, false)
        return CcViewHolder(view)
    }

    override fun getItemCount(): Int {
        return coinList.size
    }

    override fun onBindViewHolder(holder: CcViewHolder, position: Int) {
        holder.coinTextView.text = coinList[position].coinName
        val draggableState: DraggableItemState = holder.dragState
        if (draggableState.isUpdated) {
            val bgResId: Int = when {
                draggableState.isActive -> R.drawable.bg_item_active_state
                draggableState.isDragging -> R.drawable.bg_item_dragging_state
                else -> R.drawable.bg_item_normal_state
            }
            holder.mainLayout.setBackgroundResource(bgResId)
        }


    }

    inner class CcViewHolder(itemView: View) : AbstractDraggableItemViewHolder(itemView) {
        var mainLayout = itemView.findViewById<FrameLayout>(R.id.main_coontainer)
        var coinTextView: TextView = itemView.findViewById(R.id.coin)
        var dragView: View = itemView.findViewById(R.id.drag_handle)

    }
}