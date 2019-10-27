package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketball.game.Game

interface SimulationContract {

    interface GameSimRepository {
        fun attachPresenter(presenter: GameSimPresenter)
        fun startNextGame()
        fun simToGame(gameId: Int)
        fun simGame(gameId: Int)
        fun finishSeason()
    }

    interface GameSimPresenter {
        fun updateSchedule(finishedGame: Game)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean)
        fun onSimCompleted()
        fun onSeasonCompleted(conferenceTournamentIsFinished: Boolean)
    }
}