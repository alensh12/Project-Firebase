package com.example.amprime.firebaseauth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.amprime.firebaseauth.helper.Constant
import com.example.amprime.firebaseauth.R
import com.example.amprime.firebaseauth.model.WeatherModel
import com.example.amprime.firebaseauth.webservice.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherForecastFragment : Fragment() {
    var retrofit: Retrofit? = null
    private var toolbar: Toolbar? = null
    var weatherCard: androidx.cardview.widget.CardView? = null
    var cityName: TextView? = null
    var degree: TextView? = null
    var searchitemLayout: LinearLayout? = null
    private var weatherWebService: WebService.WeatherWebService? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(com.example.amprime.firebaseauth.R.layout.fragment_weather_forecast, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar!!.title = "Weather Forecast"
        searchitemLayout = view.findViewById(R.id.search_linear_layout)
        weatherCard = view.findViewById(R.id.weather_cardView)
        cityName = view.findViewById(R.id.city_textView)
        degree = view.findViewById(R.id.temp_textView)
        setHasOptionsMenu(true)
        return view

    }

    companion object {
        fun newInstance(): WeatherForecastFragment = WeatherForecastFragment()
    }

    private fun fetchWeatherdata(city: String) {
        retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        weatherWebService = retrofit!!.create(WebService.WeatherWebService::class.java)
        weatherWebService!!.fetchWeatherData(city, "4dcece6c86afb941cf3bc01d1c9ac70a").enqueue(object : Callback<WeatherModel> {
            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {

            }

            //            b22781aea4588f441ff115ea5b79d27ae2d3226304dba8ca9a16ec390d3f1dd2
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                if (response.isSuccessful) {
                    Log.e("response", response.body().toString())
                    val weatherModel: WeatherModel? = response.body()
                    Log.e("log", weatherModel!!.coord!!.lat.toString())
                    Log.e("lon", weatherModel.coord!!.lon.toString())
                    cityName!!.text = weatherModel.name.toString()
                    val degreeStr = weatherModel.main!!.temp?.let { tempToCelsius(it.toInt()) }.toString()
                    val finalDegree = "$degreeStr${getString(R.string.celsius)}"
                    degree!!.text = finalDegree

                    weatherCard!!.visibility = View.VISIBLE
                    searchitemLayout!!.visibility = View.GONE
                }


            }


        })

    }

    fun tempToCelsius(temp: Int): Int {
        return temp - 273
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val menuInflater: MenuInflater = activity!!.menuInflater
        menuInflater.inflate(R.menu.search_action, menu)
        val menuItem: MenuItem = menu!!.findItem(R.id.action_search)
        val searchView: SearchView?
        searchView = menuItem.actionView as SearchView
        searchView.apply {
            orientation = SearchView.VERTICAL
            maxWidth = Int.MAX_VALUE
            queryHint = "Enter the city name"
        }
        val searchAutoComplete: SearchView.SearchAutoComplete = searchView.findViewById(R.id.search_src_text)
        searchAutoComplete.setHintTextColor(resources.getColor(R.color.colorAccent))
        searchAutoComplete.setTextColor(resources.getColor(R.color.colorAccent))
        val imageView: ImageView = searchView.findViewById(R.id.search_mag_icon)


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.e("searchQuery", query)
                if (!searchView.isIconified) searchView.isIconified = true
                fetchWeatherdata(query.toString())
                menuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

}


