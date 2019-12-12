package com.appdev.jphil.basketballcoach.rankings

import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel
import javax.inject.Inject

class RankingsPresenter @Inject constructor(
    private val repository: RankingsContract.Repository
): RankingsContract.Presenter {

    private var view: RankingsContract.View? = null

    override fun onData(teams: List<TeamStatsDataModel>) {
        view?.displayData(teams)
    }

    override fun onViewAttached(view: RankingsContract.View) {
        this.view = view
        repository.attachPresenter(this)
        repository.fetchData()
    }

    override fun onViewDetached() {
        view = null
    }
}