package com.appdev.jphil.basketballcoach.schedule

import android.util.Log
import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val dbHelper: DatabaseHelper
) : ScheduleContract.Repository {

    private lateinit var presenter: ScheduleContract.Presenter
    private var games: List<Game>? = null
    private val teamId = 1
    private var gameId = 1

    override fun fetchSchedule() {
        GlobalScope.launch(Dispatchers.Main) {
            val job = launch(Dispatchers.IO) {
                games = dbHelper.loadGamesForTeam(teamId)
            }
            job.join()
            presenter.onScheduleLoaded(games!!)
        }
    }

    override fun simulateNextGame() {
        GlobalScope.launch(Dispatchers.Main) {
            launch(Dispatchers.IO) {
                var continueSim = false
                while(!continueSim) {
                    val game = dbHelper.loadGameById(gameId++)
                    Log.d("Schedule Repo", "Game: ${game.id}")
                    if (!game.isFinal) {
                        game.simulateFullGame()
                        continueSim = game.homeTeam.teamId == teamId || game.awayTeam.teamId == teamId
                        dbHelper.saveGames(listOf(game))
                    }
                }
            }.join()
            fetchSchedule()
        }
    }

    override fun attachPresenter(presenter: ScheduleContract.Presenter) {
        this.presenter = presenter
    }
}