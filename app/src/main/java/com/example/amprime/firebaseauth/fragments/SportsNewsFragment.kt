package com.example.amprime.firebaseauth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.SportViewInterFace
import com.example.amprime.firebaseauth.adapter.SportNewsAdapter
import com.example.amprime.firebaseauth.helper.CustomDialog
import com.example.amprime.firebaseauth.model.BbcSportsNewModel
import com.example.amprime.firebaseauth.model.News
import com.example.amprime.firebaseauth.webservice.SportsPresenter

class SportsNewsFragment : Fragment(), SportViewInterFace {


    var recyclerView: androidx.recyclerview.widget.RecyclerView? = null
    var sportAdapter: SportNewsAdapter? = null
    var listofNew = arrayListOf<News>()
    var sportsPresenter: SportsPresenter? = null

    companion object {
        fun newInstance(): SportsNewsFragment = SportsNewsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_sports_news, container, false)
        sportsPresenter = SportsPresenter(this)
        fetchBBCSportNews()


        return view

    }

    private fun setupView() {
        recyclerView = view?.findViewById(R.id.list_recycleView)
        recyclerView?.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context.applicationContext)
            val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(context.applicationContext, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL)
            dividerItemDecoration.apply {
                setDrawable(resources.getDrawable(R.drawable.divider_line))
            }
            recyclerView!!.addItemDecoration(dividerItemDecoration)
            adapter = sportAdapter
            adapter!!.notifyDataSetChanged()

        }
    }

    private fun fetchBBCSportNews() {
        CustomDialog.showDialog(activity!!)
        sportsPresenter?.getSportData()

    }

    override fun displayNews(sportsNewModel: BbcSportsNewModel) {
        for (i in 0 until sportsNewModel.articles.size) {
            val bbcImageView = sportsNewModel.articles[i].urlToImage
            val title = sportsNewModel.articles[i].title
            val content = sportsNewModel.articles[i].content
            listofNew.add(News(bbcImageView, title, content))
        }
        sportAdapter = SportNewsAdapter(context!!.applicationContext, listofNew)
        setupView()
        CustomDialog.hideDialog()
    }

    override fun displayError() {

    }
}