package com.appdev.jphil.basketballcoach.game

import android.arch.lifecycle.ViewModel
import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.*

class GameViewModel(
    private val dbHelper: DatabaseHelper
): ViewModel() {
    var gameId = -1
    var simSpeed = 1000L
    var pauseGame = false
    private var nullGame: Game? = null
    private lateinit var gameSim: Job
    private var saveGame: Job? = null

    fun simulateGame(updateGame: (game: Game) -> Unit) {
        var initialLoad = true
        GlobalScope.launch(Dispatchers.Main) {
            if (nullGame == null) {
                if (saveGame != null && !saveGame!!.isCompleted) {
                    saveGame?.join()
                }
                // Load game from db if it doesn't currently exist
                launch(Dispatchers.IO) {
                    nullGame = dbHelper.loadGameById(gameId)
                }.join()
            }
            nullGame?.let { game ->
                updateGame(game)

                // Simulate the game play-by-play updating the view each time
                gameSim = launch(Dispatchers.IO) {
                    if (!game.inProgress) {
                        game.setupGame()
                    } else {
                        game.resumeGame()
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
                            Thread.sleep(simSpeed)
                            while (pauseGame) {
                                Thread.sleep(100)
                            }
                        }
                        if (isActive) {
                            game.half++
                        }
                    }
                    if (isActive) {
                        game.half--
                        game.finishGame()
                        withContext(Dispatchers.Main) {
                            // Game is over, update the view
                            updateGame(game)
                        }
                    }
                    // save game to db
                    dbHelper.saveGames(listOf(game))
                }
                gameSim.join()
            }
        }
    }

    fun pauseSim() {
        saveGame = GlobalScope.launch(Dispatchers.IO) {
            if (gameSim.isActive) {
                pauseGame = false
                gameSim.cancelAndJoin()
            }
        }
    }
}