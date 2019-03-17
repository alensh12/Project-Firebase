package com.example.amprime.firebaseauth.webservice

import com.example.amprime.firebaseauth.model.CoinList

interface CryptoViewInterface {
    fun showCoins(coinList: CoinList)
}