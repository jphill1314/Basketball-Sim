package com.appdev.jphil.basketballcoach.strategy

import com.appdev.jphil.basketball.Coach
import com.appdev.jphil.basketball.Team
import javax.inject.Inject

class StrategyPresenter @Inject constructor(
    private val repository: StrategyContract.Repository
) : StrategyContract.Presenter {

    private var view: StrategyContract.View? = null
    private lateinit var coach: Coach

    init {
        repository.attachPresenter(this)
    }

    override fun fetchStrategy() {
        repository.loadStrategy()
    }

    override fun onStrategyLoaded(coach: Coach) {
        // TODO: only show strategy of user
        this.coach = coach

        view?.updateStrategy(
            StrategyDataModel(
            coach.paceGame - Coach.minimumPace,
                coach.offenseFavorsThreesGame,
                coach.aggressionGame,
                coach.defenseFavorsThreesGame,
                coach.pressFrequencyGame,
                coach.pressAggressionGame
        ))
    }

    override fun onPaceChanged(pace: Int) {
        coach.paceGame = pace + Coach.minimumPace
    }

    override fun onOffenseFavorsThreesChanged(favorsThrees: Int) {
        coach.offenseFavorsThreesGame = favorsThrees
    }

    override fun onAggressionChanged(aggression: Int) {
        coach.aggressionGame = aggression
    }

    override fun onDefenseFavorsThreesChanged(favorsThrees: Int) {
        coach.defenseFavorsThreesGame = favorsThrees
    }

    override fun onPressFrequencyChanged(frequency: Int) {
        coach.pressFrequencyGame = frequency
    }

    override fun onPressAggressionChanged(aggression: Int) {
        coach.pressAggressionGame = aggression
    }

    override fun onViewAttached(view: StrategyContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
        repository.saveStrategy(coach)
    }

    override fun onDestroyed() { }
}