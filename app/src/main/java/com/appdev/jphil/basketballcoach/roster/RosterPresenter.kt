package com.appdev.jphil.basketballcoach.roster

import android.util.Log
import com.appdev.jphil.basketball.Team
import javax.inject.Inject

class RosterPresenter @Inject constructor(private val repository: RosterContract.Repository): RosterContract.Presenter {

    private var view: RosterContract.View? = null

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        repository.fetchData()
    }

    override fun onDataFetched(team: Team) {
        view?.updateTeamAndConference(team.teamId, team.conferenceId)
        view?.displayData(team.roster)
    }

    override fun onViewAttached(view: RosterContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
    }

    override fun onDestroyed() {
        Log.d("test", "presenter onDestroyed")
    }
}