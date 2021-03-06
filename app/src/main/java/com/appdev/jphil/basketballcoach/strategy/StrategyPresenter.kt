package com.appdev.jphil.basketballcoach.strategy

import android.content.res.Resources
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.arch.BasePresenter
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class StrategyPresenter @Inject constructor(
    private val repository: StrategyContract.Repository,
    private val resources: Resources,
    dispatcherProvider: DispatcherProvider
) : BasePresenter(dispatcherProvider), StrategyContract.Presenter {

    private var view: StrategyContract.View? = null
    private lateinit var coach: Coach

    init {
        repository.attachPresenter(this)
    }

    override fun fetchStrategy() {
        coroutineScope.launch {
            coach = repository.loadStrategy()
            // TODO: only show strategy of user
            view?.updateStrategy(
                StrategyDataModel.generateDataModels(coach, resources, false)
            )
        }
    }

    override fun onPaceChanged(pace: Int) {
        coach.pace = pace + Coach.minimumPace
    }

    override fun onOffenseFavorsThreesChanged(favorsThrees: Int) {
        coach.offenseFavorsThrees = favorsThrees
    }

    override fun onAggressionChanged(aggression: Int) {
        coach.aggression = aggression
    }

    override fun onDefenseFavorsThreesChanged(favorsThrees: Int) {
        coach.defenseFavorsThrees = favorsThrees
    }

    override fun onPressFrequencyChanged(frequency: Int) {
        coach.pressFrequency = frequency
    }

    override fun onPressAggressionChanged(aggression: Int) {
        coach.pressAggression = aggression
    }

    override fun onIntentionallyFoulToggled(isChecked: Boolean) {
        // NO OP
    }

    override fun onMoveQuicklyToggled(isChecked: Boolean) {
        // NO OP
    }

    override fun onWasteTimeToggled(isChecked: Boolean) {
        // NO OP
    }

    override fun onViewAttached(view: StrategyContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
        coroutineScope.launch {
            repository.saveStrategy(coach)
        }
    }
}
