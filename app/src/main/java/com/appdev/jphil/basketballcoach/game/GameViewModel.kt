package com.appdev.jphil.basketballcoach.game

import android.arch.lifecycle.ViewModel
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.game.extensions.makeUserSubsIfPossible
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import com.appdev.jphil.basketballcoach.strategy.StrategyContract
import kotlinx.coroutines.*

class GameViewModel(
    private val database: BasketballDatabase
): ViewModel(), StrategyContract.Adapter.Out {
    var gameId = -1
    var simSpeed = 1000L
    var pauseGame = true
    var lastPlay = 0
    var totalEvents = 0
    lateinit var coach: Coach
    private var nullGame: Game? = null
    private var gameSim: Job? = null
    private var saveGame: Job? = null
    private val gameEvents = mutableListOf<GameEventEntity>()

    fun simulateGame(updateGame: (game: Game, newEvents: List<GameEventEntity>) -> Unit, notifyNewHalf: () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            if (nullGame == null) {
                if (saveGame?.isCompleted == false) {
                    saveGame?.join()
                }
                // Load game from db if it doesn't currently exist
                nullGame = GameDatabaseHelper.loadGameById(gameId, database)
            }

            nullGame?.let { game ->
                game.userIsCoaching = true // allow the user to make their own subs
                coach = if (game.homeTeam.isUser) game.homeTeam.getHeadCoach() else game.awayTeam.getHeadCoach()
                if (gameEvents.isEmpty()) {
                    gameEvents.addAll(GameDatabaseHelper.loadGameEvents(gameId, database))
                    totalEvents = gameEvents.size
                }

                // Simulate the game play-by-play updating the view each time
                gameSim = launch(Dispatchers.IO) {
                    if (!game.inProgress) {
                        game.setupGame()
                    } else {
                        game.resumeGame()
                    }

                    withContext(Dispatchers.Main) {
                        updateGame(game, gameEvents)
                    }

                    while (isActive && (game.half < 3 || game.homeScore == game.awayScore)) {
                        if (game.timeRemaining == 0) {
                            game.startHalf()
                            withContext(Dispatchers.Main) {
                                notifyNewHalf()
                            }
                        }

                        while (pauseGame) {
                            // Wait for the user to press play
                            // Happens between each half and when the sim is first opened
                            Thread.sleep(100)
                        }
                        withContext(Dispatchers.Main) {
                            updateGame(game, getNewPlayEvents(game))
                        }
                        Thread.sleep(simSpeed)

                        while (isActive && game.timeRemaining > 0) {
                            game.simPlay()
                            game.makeUserSubsIfPossible()
                            withContext(Dispatchers.Main) {
                                updateGame(game, getNewPlayEvents(game))
                            }
                            Thread.sleep(simSpeed)
                            while (pauseGame) {
                                Thread.sleep(100)
                            }
                            game.makeUserSubsIfPossible()
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
                    GameDatabaseHelper.saveGames(listOf(game), database)
                    GameDatabaseHelper.saveGameEvents(gameEvents, database)
                }
                gameSim?.join()
            }
        }
    }

    fun pauseSim() {
        saveGame = GlobalScope.launch(Dispatchers.IO) {
            if (gameSim?.isActive == true) {
                val pause = pauseGame
                pauseGame = false
                gameSim?.cancelAndJoin()
                pauseGame = pause
            }
        }
    }

    fun addUserSub(sub: Pair<Int, Int>) {
        nullGame?.let { game ->
            if (game.homeTeam.isUser) {
                game.homeTeam.addPendingSub(sub)
            } else {
                game.awayTeam.addPendingSub(sub)
            }
        }
    }

    fun callTimeout() {
        nullGame?.let { game ->
            if (game.homeTeam.isUser) {
                game.homeTeam.userWantsTimeout = !game.homeTeam.userWantsTimeout
            } else {
                game.awayTeam.userWantsTimeout = !game.awayTeam.userWantsTimeout
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
                        game.homeTeam.abbreviation,
                        game.awayTeam.abbreviation,
                        game.homeScore,
                        game.awayScore,
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
                            game.homeTeam.abbreviation,
                            game.awayTeam.abbreviation,
                            game.homeScore,
                            game.awayScore,
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

    override fun onPaceChanged(pace: Int) {
        coach.paceGame = pace + Coach.minimumPace
    }

    override fun onOffenseFavorsThreesChanged(favorsThrees: Int) {
        coach.offenseFavorsThreesGame = favorsThrees
    }

    override fun onAggressionChanged(aggression: Int) {
        coach.aggressionGame = aggression
    }

    override fun onDefenseFavorsThreesChanged(favorsThrees: Int) {
        coach.defenseFavorsThreesGame = favorsThrees
    }

    override fun onPressFrequencyChanged(frequency: Int) {
        coach.pressFrequencyGame = frequency
    }

    override fun onPressAggressionChanged(aggression: Int) {
        coach.pressAggressionGame = aggression
    }
}