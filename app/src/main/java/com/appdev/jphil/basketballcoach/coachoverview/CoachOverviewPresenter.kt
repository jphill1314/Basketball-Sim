package com.appdev.jphil.basketballcoach.coachoverview

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.arch.BasePresenter
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoachOverviewPresenter @Inject constructor(
    private val repository: CoachOverviewContract.Repository,
    dispatcherProvider: DispatcherProvider
) : BasePresenter(dispatcherProvider), CoachOverviewContract.Presenter {

    init {
        repository.attachPresenter(this)
    }

    private var view: CoachOverviewContract.View? = null
    private lateinit var coach: Coach

    override fun fetchData() {
        coroutineScope.launch {
            coach = repository.loadCoach()
            view?.displayCoach(coach)
        }
    }

    override fun positionToggled(position: Int) {
    }

    override fun setMinRating(minRating: Int) {
    }

    override fun setMaxRating(maxRating: Int) {
    }

    override fun setMinPotential(minPotential: Int) {
    }

    override fun setMaxPotential(maxPotential: Int) {
    }

    override fun onViewAttached(view: CoachOverviewContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
        coroutineScope.launch {
            repository.saveCoach(coach)
        }
    }
}
