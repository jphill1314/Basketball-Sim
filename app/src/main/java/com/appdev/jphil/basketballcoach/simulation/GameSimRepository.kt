package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.BatchInsertHelper
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
    private var simIsCancelled = false

    override fun attachPresenter(presenter: SimulationContract.GameSimPresenter) {
        this.presenter = presenter
    }

    override fun playGame(gameId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            simIsCancelled = false
            var id = GameDatabaseHelper.getFirstGameWithIsFinal(false, database)!!
            onSimStarted(gameId - id - 1)
            val teams = mutableMapOf<Int, Team>()
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
            val games = mutableListOf<Game>()
            while (id < gameId && !simIsCancelled) {
                GameDatabaseHelper.loadGameByIdWithTeams(id++, teams, database)?.let { game ->
                    games.add(game)
                    teams[game.homeTeam.teamId] = game.homeTeam
                    teams[game.awayTeam.teamId] = game.awayTeam
                    if (!game.isFinal) {
                        simGame(game, recruits)
                        updatePresenter(game)
                    }
                }
            }
            BatchInsertHelper.saveGamesTeamsAndRecruits(
                games,
                teams.map { (_, team) -> team },
                recruits,
                database
            )
            if (simIsCancelled) {
                onSimComplete()
            } else {
                GameDatabaseHelper.loadGameByIdWithTeams(id, teams, database)?.let {
                    withContext(Dispatchers.Main) {
                        presenter.startGameFragment(
                            id,
                            it.homeTeam.name,
                            it.awayTeam.name,
                            it.homeTeam.isUser
                        )
                    }
                } ?: run { onSimComplete() }
            }
        }
    }

    override fun simGame(gameId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            simIsCancelled = false
            var id = GameDatabaseHelper.getFirstGameWithIsFinal(false, database)!!
            onSimStarted(gameId - id)
            val teams = mutableMapOf<Int, Team>()
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
            val games = mutableListOf<Game>()
            while (id <= gameId && !simIsCancelled) {
                GameDatabaseHelper.loadGameByIdWithTeams(id++, teams, database)?.let { game ->
                    games.add(game)
                    teams[game.homeTeam.teamId] = game.homeTeam
                    teams[game.awayTeam.teamId] = game.awayTeam
                    if (!game.isFinal) {
                        simGame(game, recruits)
                        updatePresenter(game)
                    }
                }
            }
            BatchInsertHelper.saveGamesTeamsAndRecruits(
                games,
                teams.map { (_, team) -> team },
                recruits,
                database
            )
            onSimComplete()
        }
    }

    override fun finishRegularSeason() {
        GlobalScope.launch(Dispatchers.IO) {
            if (!GameDatabaseHelper.hasTournamentGames(database)) {
                val teams = mutableMapOf<Int, Team>()
                val games = GameDatabaseHelper.loadAllGameEntities(database)
                    .filter { !it.isFinal && it.tournamentId == null }
                    .sortedBy { it.id }
                onSimStarted(games.size)
                val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
                val realGames = mutableListOf<Game>()
                games.forEach { gameEntity ->
                    val game = GameDatabaseHelper.loadGameByIdWithTeams(gameEntity.id!!, teams, database)!!
                    realGames.add(game)
                    teams[game.homeTeam.teamId] = game.homeTeam
                    teams[game.awayTeam.teamId] = game.awayTeam
                    simGame(game, recruits)
                    updatePresenter(game)
                }
                BatchInsertHelper.saveGamesTeamsAndRecruits(
                    realGames,
                    teams.map { (_, team) -> team },
                    recruits,
                    database
                )
                onSeasonFinished(false)
            } else {
                ConferenceDatabaseHelper.loadAllConferenceEntities(database).forEach { conference ->
                    if (!conference.tournamentIsFinished) {
                        onSeasonFinished(false)
                        return@launch
                    }
                }
                onSeasonFinished(true)
            }
        }
    }

    override fun finishTournaments() {
        GlobalScope.launch(Dispatchers.IO) {
            val teams = mutableMapOf<Int, Team>()
            var isFinished = false
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
            while (!isFinished) {
                isFinished = true
                ConferenceDatabaseHelper.loadAllConferences(database).forEach {
                    if (it.tournament?.getWinnerOfTournament() == null) {
                        isFinished = false
                    }
                }
                if (isFinished) {
                    BatchInsertHelper.saveGamesTeamsAndRecruits(
                        emptyList(),
                        teams.map { (_, team) -> team },
                        recruits,
                        database
                    )
                    onSeasonFinished(true)
                    return@launch
                }

                var id = GameDatabaseHelper.getFirstGameWithIsFinal(false, database)!!
                var game = GameDatabaseHelper.loadGameByIdWithTeams(id++, teams, database)
                while (game != null) {
                    teams[game.homeTeam.teamId] = game.homeTeam
                    teams[game.awayTeam.teamId] = game.awayTeam
                    simGame(game, recruits)
                    GameDatabaseHelper.saveGameAndStats(game, database)
                    updatePresenter(game)
                    game = GameDatabaseHelper.loadGameByIdWithTeams(id++, teams, database)
                }
            }
        }
    }

    override fun cancelSim() {
        simIsCancelled = true
    }

    private fun simGame(game: Game, recruits: List<Recruit>) {
        game.simulateFullGame()
        recruits.forEach { it.updateInterestAfterGame(game) }

        game.homeTeam.doScouting(recruits)
        game.awayTeam.doScouting(recruits)
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

    private suspend fun updatePresenter(finishedGame: Game) {
        withContext(Dispatchers.Main) { presenter.updateSchedule(finishedGame) }
    }

    private suspend fun onSimStarted(totalGames: Int) {
        withContext(Dispatchers.Main) { presenter.onSimulationStarted(totalGames) }
    }

    private suspend fun onSimComplete() {
        withContext(Dispatchers.Main) { presenter.onSimCompleted() }
    }
}
