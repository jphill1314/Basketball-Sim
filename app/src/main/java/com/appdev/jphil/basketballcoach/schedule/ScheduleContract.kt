package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.MVPContract

interface ScheduleContract {

    interface View : MVPContract.View {
        fun displaySchedule(games: List<Game>)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchSchedule()
        fun onScheduleLoaded(games: List<Game>)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String)
        fun onFABClicked()
        fun simulateToGame(gameId: Int)
        fun simulateGame(gameId: Int)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchSchedule()
        fun simulateNextGame()
        fun simulateToGame(gameId: Int)
        fun simulateGame(gameId: Int)
    }
}