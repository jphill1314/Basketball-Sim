package com.appdev.jphil.basketballcoach.game

import android.arch.lifecycle.ViewModel
import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val dbHelper: DatabaseHelper
): ViewModel() {
    var gameId = -1
    lateinit var game: Game

    fun simulateGame(updateGame: (game: Game) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            // Load game from db and update the view
            launch(Dispatchers.IO) {
                game = dbHelper.loadGameById(gameId)
            }.join()
            updateGame(game)

            // Simulate the game play-by-play updating the view each time
            launch(Dispatchers.IO) {
                while (!game.isFinal) {
                    game.setupGame()

                    while (game.half < 3 || game.homeScore == game.awayScore) {
                        game.startHalf()

                        while (game.timeRemaining > 0) {
                            game.simPlay()
                            withContext(Dispatchers.Main) {
                                updateGame(game)
                            }
                            Thread.sleep(1000)
                        }
                        game.half++
                    }
                    game.half--
                    game.finishGame()

                    // save game to db
                    dbHelper.saveGames(listOf(game))
                }
            }

            // Game is over, update the view
            updateGame(game)
        }
    }
}