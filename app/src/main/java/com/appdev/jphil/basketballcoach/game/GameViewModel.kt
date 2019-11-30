package com.appdev.jphil.basketballcoach.game

import androidx.lifecycle.ViewModel
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.game.extensions.makeUserSubsIfPossible
import com.appdev.jphil.basketball.game.helpers.HalfTimeHelper
import com.appdev.jphil.basketball.plays.TipOff
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.strategy.StrategyContract
import kotlinx.coroutines.*

class GameViewModel(
    private val database: BasketballDatabase
): ViewModel() {
    var gameId = -1
    var simSpeed = 1000L
    var pauseGame = true
    var lastPlay = 0
    private var nullGame: Game? = null
    private var gameSim: Job? = null
    private var saveGame: Job? = null
    private val gameEvents = mutableListOf<GameEventEntity>()
    val gameStrategyOut = GameStrategyOut()

    fun simulateGame(updateGame: (game: Game, newEvents: List<GameEventEntity>) -> Unit, notifyNewHalf: (newEvents: List<GameEventEntity>) -> Unit) {
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
                gameStrategyOut.apply {
                    if (game.homeTeam.isUser) {
                        coach = game.homeTeam.getHeadCoach()
                        userTeam = game.homeTeam
                    } else {
                        coach = game.awayTeam.getHeadCoach()
                        userTeam = game.awayTeam
                    }
                }
                if (gameEvents.isEmpty()) {
                    gameEvents.addAll(GameDatabaseHelper.loadGameEvents(gameId, database))
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
                            HalfTimeHelper.startHalf(game)
                            withContext(Dispatchers.Main) {
                                notifyNewHalf(getNewPlayEvents(game))
                            }
                        }

                        while (pauseGame && isActive) {
                            // Wait for the user to press play
                            // Happens between each half and when the sim is first opened
                            Thread.sleep(100)
                        }
                        withContext(Dispatchers.Main) {
                            if (isActive) {
                                updateGame(game, getNewPlayEvents(game))
                            }
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
                        updateRecruitsAfterGame(game)
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
        val newEvents = GameEventsHelper.getNewPlayEvents(game, lastPlay, gameEvents.size)
        lastPlay = game.gamePlays.size
        gameEvents.addAll(newEvents)
        return newEvents
    }

    private fun updateRecruitsAfterGame(game: Game) {
        val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
        recruits.forEach { recruit -> recruit.updateInterestAfterGame(game) }
        game.homeTeam.doScouting(recruits)
        game.awayTeam.doScouting(recruits)

        if (!game.homeTeam.isUser) {
            TeamRecruitInteractor.interactWithRecruits(game.homeTeam, recruits)
        }

        if (!game.awayTeam.isUser) {
            TeamRecruitInteractor.interactWithRecruits(game.awayTeam, recruits)
        }
        RecruitDatabaseHelper.saveRecruits(recruits, database)
    }
}