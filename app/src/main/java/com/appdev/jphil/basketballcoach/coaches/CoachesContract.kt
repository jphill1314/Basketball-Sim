package com.appdev.jphil.basketballcoach.coaches

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.MVPContract

interface CoachesContract {

    interface View : MVPContract.View {
        fun displayCoaches(coaches: List<Coach>)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
    }

    interface Repository : MVPContract.Repository<Presenter> {
        suspend fun loadCoaches(): List<Coach>
    }
}
