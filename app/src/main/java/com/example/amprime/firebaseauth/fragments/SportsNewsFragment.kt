package com.example.amprime.firebaseauth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amprime.firebaseauth.helper.Constant
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.adapter.SportNewsAdapter
import com.example.amprime.firebaseauth.model.BbcSportsNewModel
import com.example.amprime.firebaseauth.model.News
import com.example.amprime.firebaseauth.webservice.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SportsNewsFragment : Fragment() {
    var recyclerView: androidx.recyclerview.widget.RecyclerView? = null
    var sportAdapter: SportNewsAdapter? = null
    var listofNew = arrayListOf<News>()
    var bbcWebService: WebService.BbcSportsWebService? = null

    companion object {
        fun newInstance(): SportsNewsFragment = SportsNewsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_sports_news, container, false)
        fetchBBCSportNews()
        recyclerView = view.findViewById(R.id.list_recycleView)
        return view

    }

    private fun fetchBBCSportNews() {
        val retrofit1: Retrofit = Retrofit.Builder().baseUrl(Constant.BBC_SPORT_URL).addConverterFactory(GsonConverterFactory.create()).build()
        bbcWebService = retrofit1.create(WebService.BbcSportsWebService::class.java)
        bbcWebService!!.fetchSportNews("bbc-sport", "78ef1815867e41afbddd884505db1981").enqueue(object : Callback<BbcSportsNewModel> {
            override fun onFailure(call: Call<BbcSportsNewModel>, t: Throwable) {

            }
            override fun onResponse(call: Call<BbcSportsNewModel>, response: Response<BbcSportsNewModel>) {
                if (response.isSuccessful) {
                    val bbcSportsNewModel = response.body()
                    Log.e("totalResult", bbcSportsNewModel?.totalResults.toString())
                    for (i in 0 until bbcSportsNewModel?.articles!!.size) {
                        val bbcImage = bbcSportsNewModel.articles[i].urlToImage
                        val title = bbcSportsNewModel.articles[i].title
                        val content = bbcSportsNewModel.articles[i].content
                        listofNew.add(News(bbcImage, title, content))
                    }
                    sportAdapter = SportNewsAdapter(context!!.applicationContext, listofNew)
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
            }
        })
    }
}