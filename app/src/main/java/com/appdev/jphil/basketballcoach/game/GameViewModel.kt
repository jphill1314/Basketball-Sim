package com.appdev.jphil.basketballcoach.game

import android.arch.lifecycle.ViewModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import kotlinx.coroutines.*

class GameViewModel(
    private val dbHelper: DatabaseHelper
): ViewModel() {
    var gameId = -1
    var simSpeed = 1000L
    var pauseGame = false
    var lastPlay = 0
    var totalEvents = 0
    private var nullGame: Game? = null
    private lateinit var gameSim: Job
    private var saveGame: Job? = null
    private val gameEvents = mutableListOf<GameEventEntity>()

    fun simulateGame(updateGame: (game: Game, newEvents: List<GameEventEntity>) -> Unit) {
        var initialLoad = true
        GlobalScope.launch(Dispatchers.IO) {
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
                // Simulate the game play-by-play updating the view each time
                gameSim = launch(Dispatchers.IO) {
                    if (!game.inProgress) {
                        game.setupGame()
                    } else {
                        game.resumeGame()
                    }

                    if (gameEvents.isEmpty()) {
                        gameEvents.addAll(dbHelper.loadGameEvents(gameId))
                    }
                    withContext(Dispatchers.Main) {
                        updateGame(game, gameEvents)
                    }

                    while (isActive && (game.half < 3 || game.homeScore == game.awayScore)) {
                        if (!initialLoad || game.timeRemaining == 0) {
                            game.startHalf()
                        }
                        initialLoad = false

                        while (isActive && game.timeRemaining > 0) {
                            game.simPlay()
                            withContext(Dispatchers.Main) {
                                updateGame(game, getNewPlayEvents(game))
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
                            updateGame(game, getNewPlayEvents(game))
                        }
                    }
                    // save game to db
                    dbHelper.saveGames(listOf(game))
                    dbHelper.saveGameEvents(gameEvents)
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

    private fun getNewPlayEvents(game: Game): List<GameEventEntity> {
        val newEvents = mutableListOf<GameEventEntity>()
        val plays = game.gamePlays

        if (plays.size > lastPlay) {
            val newPlays = plays.subList(lastPlay, plays.size)

            for (index in newPlays.indices) {
                val play = newPlays[index]
                if (index == 0) {
                    newEvents.add(GameEventEntity(
                        totalEvents++,
                        gameId,
                        play.timeRemaining,
                        play.shotClock,
                        play.playAsString,
                        play.homeTeamStartsWithBall
                    ))
                } else {
                    val lastPlay = newPlays[index - 1]
                    if (lastPlay.timeRemaining == play.timeRemaining) {
                        newEvents.last().event += "\n${play.playAsString}"
                    } else {
                        newEvents.add(GameEventEntity(
                            totalEvents++,
                            gameId,
                            play.timeRemaining,
                            play.shotClock,
                            play.playAsString,
                            play.homeTeamStartsWithBall
                        ))
                    }
                }
            }
        }

        lastPlay = plays.size
        gameEvents.addAll(newEvents)
        return newEvents
    }
}