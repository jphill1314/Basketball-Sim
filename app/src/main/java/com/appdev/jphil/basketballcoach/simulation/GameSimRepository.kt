package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
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
            var gameId = GameDatabaseHelper.loadAllGameEntities(database)
                .filter { !it.isFinal }.sortedBy { it.id }.first().id ?: 1
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
                            simGame(game, recruits)
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
            if (gameLoaded) {
                withContext(Dispatchers.Main) {
                    presenter.startGameFragment(gameId - 1, homeName, awayName, userIsHomeTeam)
                }
            } else {
                val conferences = ConferenceDatabaseHelper.loadAllConferences(database)
                var conferenceTournamentsAreComplete = true
                conferences.forEach { conference ->
                    if (conference.tournament?.getWinnerOfTournament() == null) {
                        conferenceTournamentsAreComplete = false
                    }
                }
                withContext(Dispatchers.Main) {
                    presenter.onSeasonCompleted(conferenceTournamentsAreComplete)
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
                        simGame(game, recruits)
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
                        simGame(game, recruits)
                    }
                }
            }
            RecruitDatabaseHelper.saveRecruits(recruits, database)
            withContext(Dispatchers.Main) {
                presenter.onSimCompleted()
            }
        }
    }

    override fun finishSeason() {
        GlobalScope.launch(Dispatchers.IO) {
            var areTournamentsOver = false
            ConferenceDatabaseHelper.loadAllConferences(database).forEach { conference ->
                if (conference.tournament != null) {
                    areTournamentsOver = true
                    if (conference.tournament?.getWinnerOfTournament() == null) {
                        onSeasonFinished(false)
                        return@launch
                    }
                }
            }
            if (areTournamentsOver) {
                onSeasonFinished(true)
                return@launch
            }

            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
            GameDatabaseHelper.loadAllGameEntities(database).filter { !it.isFinal && it.tournamentId == null }
                .sortedBy { it.id }
                .forEach { gameEntity ->
                    val game = GameDatabaseHelper.loadGameById(gameEntity.id!!, database)!!
                    simGame(game, recruits)
            }
            RecruitDatabaseHelper.saveRecruits(recruits, database)
            onSeasonFinished(false)
        }
    }

    private fun simGame(game: Game, recruits: List<Recruit>) {
        game.simulateFullGame()
        GameDatabaseHelper.saveGames(listOf(game), database)
        recruits.forEach { recruit -> recruit.updateInterestAfterGame(game) }

        if (!game.homeTeam.isUser) {
            TeamRecruitInteractor.interactWithRecruits(game.homeTeam, recruits)
        }

        if (!game.awayTeam.isUser) {
            TeamRecruitInteractor.interactWithRecruits(game.awayTeam, recruits)
        }
    }

    private suspend fun onSeasonFinished(areTournamentsOver: Boolean) {
        withContext(Dispatchers.Main) {
            presenter.onSeasonCompleted(areTournamentsOver)
        }
    }
}