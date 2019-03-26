package com.example.amprime.firebaseauth.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.amprime.firebaseauth.ChangePassword
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.TeamViewInterface
import com.example.amprime.firebaseauth.adapter.TeamsAdapter
import com.example.amprime.firebaseauth.helper.Constant
import com.example.amprime.firebaseauth.helper.TeamDataProvider
import com.example.amprime.firebaseauth.model.CountryFootballModel
import com.example.amprime.firebaseauth.model.LeagueFootballModel
import com.example.amprime.firebaseauth.model.TeamListModel
import com.example.amprime.firebaseauth.model.TeamNameModel
import com.example.amprime.firebaseauth.presenter.TeamPresenter
import com.example.amprime.firebaseauth.webservice.WebService
import com.google.gson.Gson
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.snappydb.DB
import com.snappydb.DBFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FootballFragment : Fragment(), View.OnClickListener {
    var teamPresenter: TeamPresenter? = null
    var retrofit: Retrofit? = null
    private var footballWebService: WebService.LiveScoreWebService? = null
    private var teamsAdapter: TeamsAdapter? = null
    private var teamNameModel: TeamNameModel? = null
    var teamDataProvider: TeamDataProvider? = null
    var countryNameList = arrayListOf<String>()
    private var leagueNameList = arrayListOf<String>()
    var countryHashMap: HashMap<String, String> = HashMap()
    var leagueHashMap: HashMap<String, String> = HashMap()
    var teamHashMap: HashMap<String, String> = HashMap()
    var snappyDb: DB? = null
    private var detailButton: Button? = null
    var selectedTeamKey: String? = null
    private var onCallBack: OnCallBack? = null
    var recyclerView: RecyclerView? = null
    var wrapperAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    var recyclerViewDragAndDropManager: RecyclerViewDragDropManager? = null


    companion object {
        fun newInstance(): FootballFragment = FootballFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_football, container, false)
        retrofit = Retrofit.Builder()
                .baseUrl(Constant.SOCCER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        snappyDb = DBFactory.open(activity, "teamDb")
        detailButton = view.findViewById(R.id.view_detail)
        detailButton?.setOnClickListener(this)
        fetchFootballCountry()

        return view

    }

    private fun getDataProvider(): TeamDataProvider? {
        return teamDataProvider
    }


    private fun fetchFootballCountry() {

        footballWebService = retrofit!!.create(WebService.LiveScoreWebService::class.java)
        footballWebService!!.fetchCountryData("Countries", "b22781aea4588f441ff115ea5b79d27ae2d3226304dba8ca9a16ec390d3f1dd2").enqueue(object : Callback<CountryFootballModel> {
            override fun onFailure(call: Call<CountryFootballModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<CountryFootballModel>, response: Response<CountryFootballModel>) {
                try {
                    if (response.isSuccessful) {
                        val countryFootballModel: CountryFootballModel? = response.body()
                        for (i in 0 until countryFootballModel?.result!!.size) {
                            val countryKey: String = countryFootballModel.result!![i].countryKey.toString()
                            val countryName: String = countryFootballModel.result!![i].countryName.toString()
                            countryHashMap[countryName] = countryKey
                            countryNameList.add(countryName)
                        }
                        val listCountrySpinner = view?.findViewById<Spinner>(R.id.country_list)
                        val arrayAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, countryNameList)
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        listCountrySpinner?.adapter = arrayAdapter
                        listCountrySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }

                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                val countryName = p0!!.getItemAtPosition(p2).toString()
                                Log.e("countryKey", countryHashMap[countryName].toString())
                                val countryKey = countryHashMap[countryName].toString()

                                fetchLeagueData(countryKey)
                            }
                        }
                    }
                } catch (e: Exception) {

                }
            }
        })

    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        try {
            onCallBack = activity as OnCallBack
        } catch (e: Exception) {

        }
    }

    private fun fetchLeagueData(countryKey: String) {
        val queue = Volley.newRequestQueue(activity)
        val url = "https://allsportsapi.com/api/football/?met=Leagues&APIkey=b22781aea4588f441ff115ea5b79d27ae2d3226304dba8ca9a16ec390d3f1dd2&countryId=$countryKey"
        val stringRequest = StringRequest(Request.Method.GET, url, com.android.volley.Response.Listener { response ->
            val strRespone = response.toString()
            val gs = Gson()
            val leagueFootballModel = gs.fromJson(strRespone, LeagueFootballModel::class.java)
            Log.e("success", leagueFootballModel.success.toString())
            leagueNameList.clear()
            for (i in 0 until leagueFootballModel!!.result!!.size) {
                val leagueName = leagueFootballModel.result!![i].leagueName.toString()
                val leagueId = leagueFootballModel.result!![i].leagueKey.toString()
                leagueHashMap[leagueName] = leagueId
                leagueNameList.add(leagueName)
            }
            val leagueSpinner = view?.findViewById<Spinner>(R.id.league_list)
            val arrayAdapter = ArrayAdapter(context!!.applicationContext, android.R.layout.simple_spinner_item, leagueNameList)

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            leagueSpinner?.apply {
                adapter = arrayAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        Log.e("leagueSelected", p0?.getItemAtPosition(p2).toString())

                        selectedTeamKey = leagueHashMap[p0?.getItemAtPosition(p2).toString()]
                        fetchTeamList()
                    }
                }
            }


        }, com.android.volley.Response.ErrorListener { })
        queue.add(stringRequest)

    }

    fun fetchTeamList() {
        val teamsItemAdapter = TeamsAdapter(activity!!, getDataProvider()!!)
        teamsAdapter = teamsItemAdapter
        wrapperAdapter = recyclerViewDragAndDropManager?.createWrappedAdapter(teamsItemAdapter)
        if (teamDataProvider?.mData?.size != null) {
            recyclerView = view?.findViewById(R.id.football_recycler_view)
            recyclerView?.apply {
                layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
            }
            recyclerViewDragAndDropManager = RecyclerViewDragDropManager()
            recyclerViewDragAndDropManager?.setInitiateOnLongPress(true)

            val animatior: GeneralItemAnimator = DraggableItemAnimator()
            recyclerView?.adapter = wrapperAdapter
            recyclerView?.itemAnimator = animatior
            recyclerViewDragAndDropManager?.attachRecyclerView(recyclerView!!)
            wrapperAdapter?.notifyDataSetChanged()
        }


    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            detailButton?.id -> {
//                val intent = Intent(activity, ChangePassword::class.java)
//                intent.putExtra("key", selectedTeamKey)
//                startActivity(intent)
            }
        }
    }

    interface OnCallBack {
        fun update(teamKey: String)
    }


}