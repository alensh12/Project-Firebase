package com.example.amprime.firebaseauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.amprime.firebaseauth.adapter.CryptoCoinsAdapter
import com.example.amprime.firebaseauth.model.CoinList
import com.example.amprime.firebaseauth.model.CoinModel
import com.example.amprime.firebaseauth.presenter.CryptoPresenter
import com.example.amprime.firebaseauth.webservice.CryptoViewInterface
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager

class CryptoCurrencyActivity : AppCompatActivity(), CryptoViewInterface {
    var cryptoPresenter: CryptoPresenter? = null
    private var listCryptoCurrency: List<CoinList.CryptoData>? = null
    private lateinit var coinLists: ArrayList<CoinModel>
    private var coinsAdapter: CryptoCoinsAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerViewDragManager: RecyclerViewDragDropManager? = null
    private var wrapperAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cryptocurrency)
        cryptoPresenter = CryptoPresenter(this)
        coinLists = ArrayList()
        getCoinList()
        getView()


    }

    private fun getView() {
        recyclerView = findViewById(R.id.crypto_recycler_view)


        recyclerView?.apply {
            layoutManager = LinearLayoutManager(this.context)
            val dividerItemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)
            recyclerView?.adapter?.notifyDataSetChanged()
        }
    }

    private fun getCoinList() {
        cryptoPresenter?.getCoins()
    }

    override fun showCoins(coinList: CoinList) {
        listCryptoCurrency = ArrayList<CoinList.CryptoData>(coinList.data!!.values)
        for (i in 0 until listCryptoCurrency!!.size) {
            coinLists.add(CoinModel(i.toLong(),listCryptoCurrency!![i].coinName.toString()))
        }
        recyclerViewDragManager = RecyclerViewDragDropManager()
        coinsAdapter = CryptoCoinsAdapter(this, coinLists)
        wrapperAdapter = recyclerViewDragManager!!.createWrappedAdapter(coinsAdapter!!)
        recyclerView?.adapter = wrapperAdapter
        (recyclerView?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerViewDragManager?.setInitiateOnTouch(false)
        recyclerViewDragManager?.setInitiateOnLongPress(true)
        recyclerViewDragManager?.setInitiateOnMove(false)
        recyclerViewDragManager!!.attachRecyclerView(recyclerView!!)

    }
}