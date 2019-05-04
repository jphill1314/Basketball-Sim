package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GameSimRepository @Inject constructor(private val database: BasketballDatabase) : SimulationContract.GameSimRepository {

    private lateinit var presenter: SimulationContract.GameSimPresenter

    override fun attachPresenter(presenter: SimulationContract.GameSimPresenter) {
        this.presenter = presenter
    }

    override fun startNextGame() {
        GlobalScope.launch(Dispatchers.IO) {
            var gameId = GameDatabaseHelper.loadAllGameEntities(database).sortedBy { it.id }.first().id ?: 1
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
            var homeName = ""
            var awayName = ""
            var userIsHomeTeam = false
            var continueSim = true
            var gameLoaded = false
            while(continueSim) {
                val game = GameDatabaseHelper.loadGameById(gameId++, database)
                if (game == null) {
                    continueSim = false
                } else {
                    if (!game.isFinal) {
                        continueSim = !(game.homeTeam.isUser || game.awayTeam.isUser)
                        if (continueSim) {
                            game.simulateFullGame()
                            recruits.forEach { recruit -> recruit.updateInterestAfterGame(game) }
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
            RecruitDatabaseHelper.saveRecruits(recruits, database)
            withContext(Dispatchers.Main) {
                if (gameLoaded) {
                    presenter.startGameFragment(gameId - 1, homeName, awayName, userIsHomeTeam)
                } else {
                    presenter.onSeasonCompleted()
                }
            }
        }
    }

    override fun simToGame(gameId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
            var id = GameDatabaseHelper.loadAllGameEntities(database).sortedBy { it.id }.first().id ?: 1
            while (id < gameId) {
                GameDatabaseHelper.loadGameById(id++, database)?.let { game ->
                    if (!game.isFinal) {
                        game.simulateFullGame()
                        recruits.forEach { recruit -> recruit.updateInterestAfterGame(game) }
                        GameDatabaseHelper.saveGames(listOf(game), database)
                    }
                }
            }
            RecruitDatabaseHelper.saveRecruits(recruits, database)
            withContext(Dispatchers.Main) {
                presenter.onSimCompleted()
            }
        }
    }

    override fun simGame(gameId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
            var id = GameDatabaseHelper.loadAllGameEntities(database).sortedBy { it.id }.first().id ?: 1
            while (id <= gameId) {
                GameDatabaseHelper.loadGameById(id++, database)?.let { game ->
                    if (!game.isFinal) {
                        game.simulateFullGame()
                        recruits.forEach { recruit -> recruit.updateInterestAfterGame(game) }
                        GameDatabaseHelper.saveGames(listOf(game), database)
                    }
                }
            }
            RecruitDatabaseHelper.saveRecruits(recruits, database)
            withContext(Dispatchers.Main) {
                presenter.onSimCompleted()
            }
        }
    }
}