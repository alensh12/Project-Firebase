package com.example.amprime.firebaseauth.presenter

import com.example.amprime.firebaseauth.CryptoPresenterInterface
import com.example.amprime.firebaseauth.model.CoinList
import com.example.amprime.firebaseauth.retrofit.CrypoCompareApi
import com.example.amprime.firebaseauth.webservice.CryptoViewInterface
import com.example.amprime.firebaseauth.webservice.WebService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class CryptoPresenter(var cryptoViewInterface: CryptoViewInterface) : CryptoPresenterInterface {


    override fun getCoins() {
       getOservable()?.subscribe(getDisposable())

    }

    private fun getOservable(): Observable<CoinList>? {
        return CrypoCompareApi.getClient()!!.create(WebService.CryptoService::class.java).getCoinList().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
    }

    private fun getDisposable(): DisposableObserver<CoinList> {
        return object : DisposableObserver<CoinList>() {
            override fun onComplete() {

            }

            override fun onNext(t: CoinList) {
                cryptoViewInterface.showCoins(t)
            }

            override fun onError(e: Throwable) {
            }

        }
    }
}
