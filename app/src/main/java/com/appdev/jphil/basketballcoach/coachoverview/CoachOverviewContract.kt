package com.appdev.jphil.basketballcoach.coachoverview

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.MVPContract

interface CoachOverviewContract {

    interface View : MVPContract.View {
        fun displayCoach(coach: Coach)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onCoachLoaded(coach: Coach)
        fun positionToggled(position: Int)
        fun setMinRating(minRating: Int)
        fun setMaxRating(maxRating: Int)
        fun setMinPotential(minPotential: Int)
        fun setMaxPotential(maxPotential: Int)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun loadCoach()
        fun saveCoach(coach: Coach)
    }
}