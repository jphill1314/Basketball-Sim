package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketball.game.Game

interface SimulationContract {

    interface GameSimRepository {
        fun attachPresenter(presenter: GameSimPresenter)
        fun playGame(gameId: Int)
        fun simGame(gameId: Int)
        fun finishRegularSeason()
        fun finishTournaments()
        fun cancelSim()
    }

    interface GameSimPresenter {
        fun onSimulationStarted(totalGames: Int)
        fun updateSchedule(finishedGame: Game)
        fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean)
        fun onSimCompleted()
        fun onSeasonCompleted(conferenceTournamentIsFinished: Boolean)
    }
}