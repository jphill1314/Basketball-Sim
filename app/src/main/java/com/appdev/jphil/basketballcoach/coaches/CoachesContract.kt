package com.appdev.jphil.basketballcoach.coaches

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.MVPContract

interface CoachesContract {

    interface View : MVPContract.View {
        fun displayCoaches(coaches: List<Coach>)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onCoachesLoaded(coaches: List<Coach>)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun loadCoaches()
    }
}