package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val teamId: Int,
    private val database: BasketballDatabase
) : ScheduleContract.Repository {

    private lateinit var presenter: ScheduleContract.Presenter

    override fun fetchSchedule() {
        GlobalScope.launch(Dispatchers.IO) {
            val gameEntities = GameDatabaseHelper.loadAllGameEntities(database)
            val team = TeamDatabaseHelper.loadUserTeam(database)
            withContext(Dispatchers.Main) { presenter.onScheduleLoaded(gameEntities, team?.teamId == teamId) }
        }
    }

    override fun simulateNextGame() {
        GlobalScope.launch(Dispatchers.IO) {
            var gameId = GameDatabaseHelper.loadAllGameEntities(database).sortedBy { it.id }.first().id ?: 1
            var homeName = ""
            var awayName = ""
            var userIsHomeTeam = false
            var continueSim = true
            var gameLoaded = false
            while(continueSim) {
                val game = GameDatabaseHelper.loadGameById(gameId++, database)
                if (game == null) {
                    continueSim = false
                }
                else {
                    if (!game.isFinal) {
                        continueSim = game.homeTeam.teamId != teamId && game.awayTeam.teamId != teamId
                        if (continueSim) {
                            game.simulateFullGame()
                            GameDatabaseHelper.saveGames(listOf(game), database)
                        } else {
                            gameLoaded = true
                            homeName = game.homeTeam.name
                            awayName = game.awayTeam.name
                            userIsHomeTeam = game.homeTeam.isUser
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) {
                if (gameLoaded) {
                    presenter.startGameFragment(gameId - 1, homeName, awayName, userIsHomeTeam)
                } else {
                    presenter.onSeasonOver()
                }
            }
        }
    }

    override fun simulateToGame(gameId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            var id = GameDatabaseHelper.loadAllGameEntities(database).sortedBy { it.id }.first().id ?: 1
            while (id < gameId) {
                GameDatabaseHelper.loadGameById(id++, database)?.let { game ->
                    if (!game.isFinal) {
                        game.simulateFullGame()
                        GameDatabaseHelper.saveGames(listOf(game), database)
                    }
                }
            }
            fetchSchedule()
        }
    }

    override fun simulateGame(gameId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            var id = GameDatabaseHelper.loadAllGameEntities(database).sortedBy { it.id }.first().id ?: 1
            while (id <= gameId) {
                GameDatabaseHelper.loadGameById(id++, database)?.let { game ->
                    if (!game.isFinal) {
                        game.simulateFullGame()
                        GameDatabaseHelper.saveGames(listOf(game), database)
                    }
                }
            }
            fetchSchedule()
        }
    }

    override fun attachPresenter(presenter: ScheduleContract.Presenter) {
        this.presenter = presenter
    }
}