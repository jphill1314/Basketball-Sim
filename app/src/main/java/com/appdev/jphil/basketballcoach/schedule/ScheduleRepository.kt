package com.appdev.jphil.basketballcoach.schedule

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

    override fun fetchSchedule() {
        GlobalScope.launch(Dispatchers.IO) {
            val gameEntities = dbHelper.loadAllGameEntities()
            val team = dbHelper.loadUserTeam()
            withContext(Dispatchers.Main) { presenter.onScheduleLoaded(gameEntities, team?.teamId == teamId) }
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

    override fun simulateToGame(gameId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            var id = 1
            var continueSim = true
            while (continueSim) {
                continueSim = gameId != id
                if (continueSim) {
                    val game = dbHelper.loadGameById(id++)
                    if (!game.isFinal) {
                        game.simulateFullGame()
                        dbHelper.saveGames(listOf(game))
                    }
                }
            }
            fetchSchedule()
        }
    }

    override fun simulateGame(gameId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            var id = 1
            while (id <= gameId) {
                val game = dbHelper.loadGameById(id++)
                game.simulateFullGame()
                dbHelper.saveGames(listOf(game))
            }
            fetchSchedule()
        }
    }

    override fun attachPresenter(presenter: ScheduleContract.Presenter) {
        this.presenter = presenter
    }
}