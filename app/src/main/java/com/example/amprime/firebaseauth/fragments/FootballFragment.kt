package com.example.amprime.firebaseauth.fragments


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
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
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.helper.Constant
import com.example.amprime.firebaseauth.model.CountryFootballModel
import com.example.amprime.firebaseauth.model.LeagueFootballModel
import com.example.amprime.firebaseauth.model.SnappyTeamModel
import com.example.amprime.firebaseauth.model.TeamListModel
import com.example.amprime.firebaseauth.webservice.WebService
import com.google.gson.Gson
import com.snappydb.DB
import com.snappydb.DBFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FootballFragment : Fragment(), View.OnClickListener {


    var retrofit: Retrofit? = null
    var footballWebService: WebService.LiveScoreWebService? = null

    var countryNameList = arrayListOf<String>()
    var leagueNameList = arrayListOf<String>()
    var teamNameList = arrayListOf<String>()
    var teamKeyList = arrayListOf<String>()
    var countryHashMap: HashMap<String, String> = HashMap()
    var leagueHashMap: HashMap<String, String> = HashMap()
    var teamHashMap: HashMap<String, String> = HashMap()
    var snappyDb: DB? = null
    var detailButton: Button? = null
    var selectedTeamKey: String? = null
    private var onCallBack: OnCallBack? = null


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

//                        Log.e("hashmap", hashMap.toString())
                            countryNameList.add(countryName)
                        }
                        val listCountrySpinner = view?.findViewById<Spinner>(R.id.country_list)
                        val arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, countryNameList)
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
            val arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, leagueNameList)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            leagueSpinner?.apply {
                adapter = arrayAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        Log.e("leagueSelected", p0?.getItemAtPosition(p2).toString())
                        fetchTeamList(leagueHashMap[p0?.getItemAtPosition(p2).toString()])
                    }
                }
            }


        }, com.android.volley.Response.ErrorListener { })
        queue.add(stringRequest)

    }

    private fun fetchTeamList(leagueKey: String?) {
        val queue = Volley.newRequestQueue(activity)
        val url = "https://allsportsapi.com/api/football/?&met=Teams&leagueId=$leagueKey&APIkey=b22781aea4588f441ff115ea5b79d27ae2d3226304dba8ca9a16ec390d3f1dd2"
        val strRequest = StringRequest(Request.Method.GET, url, com.android.volley.Response.Listener { response ->
            run {
                if (response != null) {
                    try {
                        val strrespone = response.toString()
                        val gson = Gson()
                        val teamListModel = gson.fromJson(strrespone, TeamListModel::class.java)
                        teamNameList.clear()
                        Log.e("success", teamListModel.success.toString())
                        for (i in 0 until teamListModel!!.result!!.size) {
                            Log.e("teams names", teamListModel.result!![i].teamName)
                            val teamName = teamListModel.result!![i].teamName.toString()
                            var teamKey = teamListModel.result!![i].teamKey.toString()
                            val teamPlayerModel: List<TeamListModel.Player>? = teamListModel.result!![i].players
                            teamHashMap[teamName] = teamKey
                            teamNameList.add(teamListModel.result!![i].teamName.toString())
                            teamKeyList.add(teamKey)

                        }
                        val snappyTeamModel = SnappyTeamModel()
                        snappyTeamModel.teamName = teamNameList
                        snappyTeamModel.teamKey = teamKeyList
                        snappyTeamModel.hashMap = arrayListOf(teamHashMap)
                        snappyDb!!.put("model", snappyTeamModel)


                        val teamSpinner = view?.findViewById<Spinner>(R.id.team_list)

                        val arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, teamNameList)
                        arrayAdapter.apply {
                            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                        teamSpinner?.apply {
                            adapter = arrayAdapter
                            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(p0: AdapterView<*>?) {

                                }

                                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                    val selectedTeamName = p0?.getItemAtPosition(p2)
                                    selectedTeamKey = teamHashMap[selectedTeamName]
                                    Log.e("key", "$selectedTeamName = $selectedTeamKey")
                                    val snapTeamModel: SnappyTeamModel? = snappyDb!!.getObject("model", SnappyTeamModel::class.java)
                                    Log.e("dataSnappy", snapTeamModel?.hashMap.toString())

//
                                }
                            }
                        }
                    } catch (e: Exception) {

                    }


                }
            }
        }, com.android.volley.Response.ErrorListener { })
        queue.add(strRequest)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            detailButton?.id -> {
                onCallBack?.update(selectedTeamKey!!)
            }
        }
    }

    interface OnCallBack {
        fun update(teamKey: String)
    }


}