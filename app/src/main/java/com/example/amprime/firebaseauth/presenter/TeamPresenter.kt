package com.example.amprime.firebaseauth.presenter

import com.example.amprime.firebaseauth.TeamViewInterface
import com.example.amprime.firebaseauth.TeamsPresenterInterface
import com.example.amprime.firebaseauth.model.TeamListModel
import com.example.amprime.firebaseauth.retrofit.TeamsApi
import com.example.amprime.firebaseauth.webservice.WebService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class TeamPresenter(var teamViewInterface: TeamViewInterface) : TeamsPresenterInterface {
    override fun getTeamsData(league: String) {
        getObservable(league).subscribe(getObserver())

    }

    fun getObservable(leagueId: String): Observable<TeamListModel> {
        return TeamsApi.getTeamClient()!!.create(WebService.TeamListWebSevice::class.java).fetchTeamData("Teams", leagueId, "b22781aea4588f441ff115ea5b79d27ae2d3226304dba8ca9a16ec390d3f1dd2").observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()
        )
    }

    fun getObserver(): DisposableObserver<TeamListModel> {
        return object : DisposableObserver<TeamListModel>() {
            override fun onComplete() {

            }

            override fun onNext(t: TeamListModel) {
                teamViewInterface.showTeamInfo(t)
            }

            override fun onError(e: Throwable) {
            }

        }
    }
}