package com.appdev.jphil.basketballcoach.coaches

import com.appdev.jphil.basketball.coaches.Coach
import javax.inject.Inject

class CoachesPresenter @Inject constructor(
    private val repository: CoachesContract.Repository
) : CoachesContract.Presenter {

    private var view: CoachesContract.View? = null

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        repository.loadCoaches()
    }

    override fun onCoachesLoaded(coaches: List<Coach>) {
        view?.displayCoaches(coaches)
    }

    override fun onViewAttached(view: CoachesContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
    }
}