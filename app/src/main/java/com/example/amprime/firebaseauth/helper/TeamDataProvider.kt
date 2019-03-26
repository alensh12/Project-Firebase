package com.example.amprime.firebaseauth.helper

import com.example.amprime.firebaseauth.TeamViewInterface
import com.example.amprime.firebaseauth.fragments.FootballFragment
import com.example.amprime.firebaseauth.model.TeamListModel
import com.example.amprime.firebaseauth.model.TeamNameModel
import com.example.amprime.firebaseauth.presenter.TeamPresenter
import java.util.*
import kotlin.collections.ArrayList

class TeamDataProvider : TeamNameModel() {
    var mData: ArrayList<TeamsConcreteData> = arrayListOf()
    var mLastRemovedData: TeamsConcreteData? = null
    private var mlastRemovedPosition: Int = -1
    var teamPresenter: TeamPresenter? = null


    init {
        teamPresenter = TeamPresenter(object : TeamViewInterface {
            override fun showTeamInfo(teamListModel: TeamListModel) {
                for (i in 0 until teamListModel.result!!.size) {
                    val id: Long = teamListModel.result!!.size.toLong()
                    val viewType = 0
                    val teamName: String = teamListModel.result!![i].teamName!!

                    mData.add(TeamsConcreteData(id, false, viewType, teamName))
                }

            }

            override fun showError() {
            }

        })


    }

    override fun getCount(): Int {
        return mData.size
}

    override fun getItem(index: Int): Data {
        return mData[index]
    }

    override fun removeItem(position: Int) {
        val removedItem: TeamsConcreteData = mData.removeAt(position)
        mLastRemovedData = removedItem
        mlastRemovedPosition = position

    }

    override fun moveItem(fromPosition: Int, toPosition: Int) {
        if (fromPosition == toPosition) {
            return
        }

        val item: TeamsConcreteData = mData.removeAt(fromPosition)

        mData.add(toPosition, item)
        mlastRemovedPosition = -1

    }

    override fun swapItem(fromPosition: Int, toPosition: Int) {
        if (fromPosition == toPosition) {
            return
        }
        Collections.swap(mData, toPosition, fromPosition)
        mlastRemovedPosition = -1
    }

    override fun undoLastRemoval(): Int {
        if (mLastRemovedData != null) {
            val insertedPosition: Int = if (mlastRemovedPosition >= 0 && mlastRemovedPosition < mData.size) {
                mlastRemovedPosition
            } else {
                mData.size
            }

            mData.add(insertedPosition, mLastRemovedData!!)

            mLastRemovedData = null
            mlastRemovedPosition = -1

            return insertedPosition
        } else {
            return -1
        }
    }
}

class TeamsConcreteData(override val id: Long, override val isSectionHeader: Boolean, override val viewType: Int, override val text: String) : TeamNameModel.Data()