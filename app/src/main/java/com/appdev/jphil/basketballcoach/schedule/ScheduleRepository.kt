package com.appdev.jphil.basketballcoach.schedule

import android.util.Log
import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val teamId: Int,
    private val dbHelper: DatabaseHelper
) : ScheduleContract.Repository {

    private lateinit var presenter: ScheduleContract.Presenter
    private var games: List<Game>? = null

    override fun fetchSchedule() {
        GlobalScope.launch(Dispatchers.IO) {
            games = dbHelper.loadGamesForTeam(teamId)
            withContext(Dispatchers.Main) { presenter.onScheduleLoaded(games!!) }
        }
    }

    override fun simulateNextGame() {
        GlobalScope.launch(Dispatchers.IO) {
            var gameId = 1
            var homeName = ""
            var awayName = ""
            var continueSim = true
            while(continueSim) {
                val game = dbHelper.loadGameById(gameId++)
                if (!game.isFinal) {
                    continueSim = game.homeTeam.teamId != teamId && game.awayTeam.teamId != teamId
                    if (continueSim) {
                        game.simulateFullGame()
                        dbHelper.saveGames(listOf(game))
                    } else {
                        homeName = game.homeTeam.name
                        awayName = game.awayTeam.name
                    }
                }
            }
            withContext(Dispatchers.Main) { presenter.startGameFragment(gameId-1, homeName, awayName) }
        }
    }

    override fun attachPresenter(presenter: ScheduleContract.Presenter) {
        this.presenter = presenter
    }
}