package com.appdev.jphil.basketballcoach.game

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.*

class GameViewModel(
    private val dbHelper: DatabaseHelper
): ViewModel() {
    var gameId = -1
    private lateinit var game: Game
    private lateinit var gameSim: Job
    private var saveGame: Job? = null

    fun simulateGame(updateGame: (game: Game) -> Unit) {
        var initialLoad = true
        GlobalScope.launch(Dispatchers.Main) {
            if (saveGame != null && !saveGame!!.isCompleted) {
                saveGame?.join()
            }

            // Load game from db if it doesn't currently exist
            launch(Dispatchers.IO) {
                game = dbHelper.loadGameById(gameId)
            }.join()

            updateGame(game)

            // Simulate the game play-by-play updating the view each time
            gameSim = launch(Dispatchers.IO) {
                if (!game.inProgress) {
                    game.setupGame()
                }

                while (isActive && (game.half < 3 || game.homeScore == game.awayScore)) {
                    if (!initialLoad || !game.inProgress) {
                        game.startHalf()
                    }
                    initialLoad = false

                    while (isActive && game.timeRemaining > 0) {
                        game.simPlay()
                        withContext(Dispatchers.Main) {
                            updateGame(game)
                        }
                        Thread.sleep(1500)
                    }
                    if (isActive) {
                        game.half++
                    }
                }
                if (isActive) {
                    game.half--
                    game.finishGame()
                }

                // save game to db
                dbHelper.saveGames(listOf(game))
            }
            gameSim.join()
            // Game is over, update the view
            updateGame(game)
        }
    }

    fun pauseSim() {
        saveGame = GlobalScope.launch(Dispatchers.IO) {
            if (gameSim.isActive) {
                gameSim.cancelAndJoin()
                dbHelper.saveGames(listOf(game))
            }
        }
    }
}