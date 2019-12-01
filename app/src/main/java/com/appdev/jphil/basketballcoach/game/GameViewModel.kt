package com.appdev.jphil.basketballcoach.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.game.extensions.makeSubs
import com.appdev.jphil.basketball.game.extensions.makeUserSubsIfPossible
import com.appdev.jphil.basketball.game.helpers.HalfTimeHelper
import com.appdev.jphil.basketball.game.helpers.TimeoutHelper
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.game.sim.GameEventsHelper
import com.appdev.jphil.basketballcoach.game.sim.GameState
import com.appdev.jphil.basketballcoach.game.sim.GameStrategyOut
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

    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState

    suspend fun getGame(): Game {
        if (nullGame != null) {
            return nullGame!!
        }
        GlobalScope.launch(Dispatchers.IO) {
            nullGame = GameDatabaseHelper.loadGameById(gameId, database)
        }.join()
        return nullGame!!
    }

    fun simulateGame() {
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

                    _gameState.postValue(GameState(game, gameEvents))

                    while (isActive && (game.half < 3 || game.homeScore == game.awayScore)) {
                        if (game.timeRemaining == 0) {
                            HalfTimeHelper.startHalf(game)
                            withContext(Dispatchers.Main) {
                                _gameState.value = GameState(game, getNewPlayEvents(game), isNewHalf = true, isTimeout = true)
                                // Post value doesn't happen fast enough so we have to switch to main thread manually
                            }
                        }

                        while (pauseGame && isActive) {
                            // Wait for the user to press play
                            // Happens between each half and when the sim is first opened
                            Thread.sleep(100)
                        }
                        if (isActive) {
                            _gameState.postValue(GameState(game, getNewPlayEvents(game)))
                        }
                        Thread.sleep(simSpeed)

                        while (isActive && game.timeRemaining > 0) {
                            game.simPlay()
                            if (TimeoutHelper.isTimeoutCalled(game)) {
                                game.makeUserSubsIfPossible()
                                _gameState.postValue(GameState(game, getNewPlayEvents(game), isTimeout = true))
                                pauseGame = true
                                while(pauseGame && isActive) {
                                    Thread.sleep(100)
                                }
                                TimeoutHelper.runTimeout(game)
                            }
                            game.makeUserSubsIfPossible()
                            _gameState.postValue(GameState(game, getNewPlayEvents(game)))
                            Thread.sleep(simSpeed)
                            while (pauseGame && isActive) {
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
                        _gameState.postValue(GameState(game, getNewPlayEvents(game)))
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