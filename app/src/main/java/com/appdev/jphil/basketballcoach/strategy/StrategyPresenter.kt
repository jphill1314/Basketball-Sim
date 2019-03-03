package com.appdev.jphil.basketballcoach.strategy

import com.appdev.jphil.basketball.Team
import javax.inject.Inject

class StrategyPresenter @Inject constructor(
    private val repository: StrategyContract.Repository
) : StrategyContract.Presenter {

    private var view: StrategyContract.View? = null
    private lateinit var team: Team

    init {
        repository.attachPresenter(this)
    }

    override fun fetchStrategy() {
        repository.loadStrategy()
    }

    override fun onStrategyLoaded(team: Team) {
        this.team = team

        view?.updateStrategy(
            StrategyDataModel(
            team.pace - minimumPace,
                team.offenseFavorsThrees,
                team.aggression,
                team.defenseFavorsThrees,
                team.pressFrequency,
                team.pressAggression
        ))
    }

    override fun onPaceChanged(pace: Int) {
        team.pace = pace + minimumPace
    }

    override fun onOffenseFavorsThreesChanged(favorsThrees: Int) {
        team.offenseFavorsThrees = favorsThrees
    }

    override fun onAggressionChanged(aggression: Int) {
        team.aggression = aggression
    }

    override fun onDefenseFavorsThreesChanged(favorsThrees: Int) {
        team.defenseFavorsThrees = favorsThrees
    }

    override fun onPressFrequencyChanged(frequency: Int) {
        team.pressFrequency = frequency
    }

    override fun onPressAggressionChanged(aggression: Int) {
        team.pressAggression = aggression
    }

    override fun onViewAttached(view: StrategyContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
        repository.saveStrategy(team)
    }

    override fun onDestroyed() { }

    private companion object {
        const val minimumPace = 60
    }
}