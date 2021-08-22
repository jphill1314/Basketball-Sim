package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import com.appdev.jphil.basketball.tournament.Tournament
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.relations.ConferenceTournamentRelations
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.util.RecordUtil
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TournamentSimRepository @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val relationalDao: RelationalDao,
    private val gameDao: GameDao,
    private val database: BasketballDatabase
) {
    private val repositoryScope = CoroutineScope(SupervisorJob() + dispatcherProvider.io)
    private var simJob: Job? = null

    private val _simState = MutableStateFlow<SimulationState?>(null)
    val simState = _simState.asStateFlow()

    fun simulateGame(userConferenceId: Int, gameId: Int) {
        _simState.update { it?.copy(isSimulatingToGame = false) }
        simulateGames(userConferenceId, gameId)
    }

    fun simulateToGame(userConferenceId: Int, gameId: Int) {
        _simState.update { it?.copy(isSimulatingToGame = true) }
        simulateGames(userConferenceId, gameId - 1)
    }

    private fun simulateGames(userConferenceId: Int, lastGameId: Int) {
        simJob?.cancel()
        simJob = repositoryScope.launch {
            _simState.update { it?.copy(isSimActive = true) }
            val allRecruits = RecruitDatabaseHelper.loadAllRecruits(database)
            // load all tournaments
            val tournaments = loadTournaments(userConferenceId, allRecruits)
            val firstGameId = GameDatabaseHelper.getFirstGameWithIsFinal(false, database)

            _simState.update {
                it?.copy(numberOfGamesToSim = lastGameId - firstGameId + 1)
            }
            // sim games -> user's conference last
            for (gameId in firstGameId..lastGameId) {
                val game = GameDatabaseHelper.getGameById(gameId, allRecruits, relationalDao)

                game.simulateFullGame()
                allRecruits.forEach { it.updateInterestAfterGame(game) }

                game.homeTeam.doScouting(allRecruits)
                game.awayTeam.doScouting(allRecruits)

                if (!game.homeTeam.isUser) {
                    TeamRecruitInteractor.interactWithRecruits(game.homeTeam, allRecruits)
                }
                if (!game.awayTeam.isUser) {
                    TeamRecruitInteractor.interactWithRecruits(game.awayTeam, allRecruits)
                }

                GameDatabaseHelper.saveGameAndStats(game, database)
                TeamDatabaseHelper.saveTeam(game.homeTeam, database)
                TeamDatabaseHelper.saveTeam(game.awayTeam, database)

                // Update tournament with completed game
                updateTournamentWithCompletedGame(
                    tournaments.first { it.getId() == game.tournamentId },
                    game
                )

                _simState.update {
                    it?.copy(numberOfGamesSimmed = it.numberOfGamesSimmed + 1)
                }

                if (!isActive) {
                    break
                }
            }

            // save data
            RecruitDatabaseHelper.saveRecruits(allRecruits, database)
            _simState.update { it?.copy(isSimActive = false) }
        }
    }

    private suspend fun loadTournaments(
        userConferenceId: Int,
        allRecruits: List<Recruit>
    ): List<Tournament> {
        val games = gameDao.getNonTournamentGames()
        val conferenceRelations = relationalDao.loadAllConferenceTournamentData()

        val tournaments = conferenceRelations.filter {
            it.conferenceEntity.id != userConferenceId
        }.map {
            loadConferenceAndTournament(it, games, allRecruits)
        }
        val userTournament = conferenceRelations.first {
            it.conferenceEntity.id == userConferenceId
        }.let { loadConferenceAndTournament(it, games, allRecruits) }
        return tournaments + listOf(userTournament)
    }

    private suspend fun loadConferenceAndTournament(
        conferenceRelation: ConferenceTournamentRelations,
        games: List<GameEntity>,
        allRecruits: List<Recruit>
    ): Tournament {
        val conference = Conference(
            conferenceRelation.conferenceEntity.id,
            conferenceRelation.conferenceEntity.name,
            conferenceRelation.teamEntities.map {
                GameDatabaseHelper.createTeam(it, allRecruits)
            }
        )

        conference.generateTournament(conference.teams.map { RecordUtil.getRecord(games, it) })
        return conference.tournament?.let { tournament ->
            val tournamentGames = conferenceRelation.tournamentGameEntities.map { entity ->
                entity.createGame(
                    homeTeam = conference.teams.first { it.teamId == entity.homeTeamId },
                    awayTeam = conference.teams.first { it.teamId == entity.awayTeamId }
                )
            }
            tournament.replaceGames(tournamentGames)
            val newGames = tournament.generateNextRound(2018).map {
                it.apply {
                    id = gameDao.insertGame(GameEntity.from(it)).toInt()
                }
            }
            tournament.replaceGames(tournamentGames + newGames)
            tournament
        } ?: throw IllegalStateException("Unable to load tournament for conference")
    }

    private suspend fun updateTournamentWithCompletedGame(
        tournament: Tournament,
        newGame: Game
    ) {
        val games = tournament.getGames().filter { it.id != newGame.id } + listOf(newGame)
        val tournamentGames = games.sortedBy { it.id }
        tournament.replaceGames(tournamentGames)

        val newGames = tournament.generateNextRound(2018).map {
            it.apply {
                id = gameDao.insertGame(GameEntity.from(it)).toInt()
            }
        }
        tournament.replaceGames(tournamentGames + newGames)
    }

    fun cancelSimulation() {
        simJob?.cancel("Simulation cancelled by user")
        simJob = null
    }
}
