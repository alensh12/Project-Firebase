package com.example.amprime.firebaseauth.webservice

import com.example.amprime.firebaseauth.SportPresenterInterface
import com.example.amprime.firebaseauth.SportViewInterFace
import com.example.amprime.firebaseauth.model.BbcSportsNewModel
import com.example.amprime.firebaseauth.retrofit.SportNewsApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class SportsPresenter(var sportViewInterFace: SportViewInterFace) : SportPresenterInterface {
    override fun getSportData() {
        getObservable().subscribe(getObserver())
    }

    private fun getObservable(): Observable<BbcSportsNewModel> {
        return SportNewsApi.getSportClient()?.create(WebService.BbcSportsWebService::class.java)?.fetchSportNews("bbc-sport", "78ef1815867e41afbddd884505db1981")!!.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
    }

    private fun getObserver(): DisposableObserver<BbcSportsNewModel> {
        return object : DisposableObserver<BbcSportsNewModel>() {
            override fun onComplete() {

            }

            override fun onNext(t: BbcSportsNewModel) {
                sportViewInterFace.displayNews(t)
            }

            override fun onError(e: Throwable) {

            }

        }
    }
}