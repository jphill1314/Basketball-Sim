package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
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
            var gameId = GameDatabaseHelper.getFirstGameWithIsFinal(false, database)
            var homeName = ""
            var awayName = ""
            var userIsHomeTeam = false
            var continueSim = true
            var gameLoaded = false
            val teams = mutableMapOf<Int, Team>()
            while(continueSim) {
                val game = GameDatabaseHelper.loadGameByIdWithTeams(gameId++, teams, database)
                if (game == null) {
                    continueSim = false
                } else {
                    teams[game.homeTeam.teamId] = game.homeTeam
                    teams[game.awayTeam.teamId] = game.awayTeam
                    if (!game.isFinal) {
                        continueSim = !(game.homeTeam.isUser || game.awayTeam.isUser)
                        if (continueSim) {
                            simGame(game)
                            updatePresenter(game)
                        } else {
                            gameLoaded = true
                            homeName = game.homeTeam.name
                            awayName = game.awayTeam.name
                            userIsHomeTeam = game.homeTeam.isUser
                        }
                    }
                }
            }
            teams.forEach { (_, team) -> TeamDatabaseHelper.saveTeam(team, database) }
            if (gameLoaded) {
                withContext(Dispatchers.Main) {
                    presenter.startGameFragment(gameId - 1, homeName, awayName, userIsHomeTeam)
                }
            } else {
                val conferences = ConferenceDatabaseHelper.loadAllConferences(database)
                var conferenceTournamentsAreComplete = true
                conferences.forEach { conference ->
                    conference.tournament = ConferenceDatabaseHelper.createTournament(conference, database)
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
            var id = GameDatabaseHelper.getFirstGameWithIsFinal(false, database)
            val teams = mutableMapOf<Int, Team>()
            while (id < gameId) {
                GameDatabaseHelper.loadGameByIdWithTeams(id++, teams, database)?.let { game ->
                    teams[game.homeTeam.teamId] = game.homeTeam
                    teams[game.awayTeam.teamId] = game.awayTeam
                    if (!game.isFinal) {
                        simGame(game)
                        if (id <= gameId) {
                            updatePresenter(game)
                        }
                    }
                }
            }
            teams.forEach { (_, team) -> TeamDatabaseHelper.saveTeam(team, database) }
            withContext(Dispatchers.Main) {
                presenter.onSimCompleted()
            }
        }
    }

    override fun simGame(gameId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            var id = GameDatabaseHelper.getFirstGameWithIsFinal(false, database)
            val teams = mutableMapOf<Int, Team>()
            while (id <= gameId) {
                GameDatabaseHelper.loadGameByIdWithTeams(id++, teams, database)?.let { game ->
                    teams[game.homeTeam.teamId] = game.homeTeam
                    teams[game.awayTeam.teamId] = game.awayTeam
                    if (!game.isFinal) {
                        simGame(game)
                        if (id <= gameId) {
                            updatePresenter(game)
                        }
                    }
                }
            }
            teams.forEach { (_, team) -> TeamDatabaseHelper.saveTeam(team, database) }
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

            GameDatabaseHelper.loadAllGameEntities(database).filter { !it.isFinal && it.tournamentId == null }
                .sortedBy { it.id }
                .forEach { gameEntity ->
                    val game = GameDatabaseHelper.loadGameById(gameEntity.id!!, database)!!
                    simGame(game)
            }
            onSeasonFinished(false)
        }
    }

    private fun simGame(game: Game) {
        game.simulateFullGame()
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
        GameDatabaseHelper.saveGameAndStats(game, database)
        RecruitDatabaseHelper.saveRecruits(recruits, database)
    }

    private suspend fun onSeasonFinished(areTournamentsOver: Boolean) {
        withContext(Dispatchers.Main) {
            presenter.onSeasonCompleted(areTournamentsOver)
        }
    }

    private suspend fun updatePresenter(finishedGame: Game) {
        withContext(Dispatchers.Main) { presenter.updateSchedule(finishedGame) }
    }
}