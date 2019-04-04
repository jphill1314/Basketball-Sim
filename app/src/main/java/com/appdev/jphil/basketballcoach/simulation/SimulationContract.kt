package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketballcoach.database.game.GameEntity

interface SimulationContract {

    interface GameSimRepository {
        fun attachPresenter(presenter: SimulationContract.GameSimPresenter)
        fun startNextGame()
        fun simToGame(gameId: Int)
        fun simGame(gameId: Int)
    }

    interface GameSimPresenter {
        fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean)
        fun onSimCompleted()
        fun onSeasonCompleted()
    }
}