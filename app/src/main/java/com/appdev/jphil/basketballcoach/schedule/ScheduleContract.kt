package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.MVPContract

interface ScheduleContract {

    interface View : MVPContract.View {
        fun displaySchedule(games: List<Game>)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchSchedule()
        fun onScheduleLoaded(games: List<Game>)
        fun onFABClicked()
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchSchedule()
        fun simulateNextGame()
    }
}