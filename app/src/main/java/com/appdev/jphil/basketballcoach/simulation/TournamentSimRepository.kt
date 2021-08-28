package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import com.appdev.jphil.basketball.tournament.NationalChampionship
import com.appdev.jphil.basketball.tournament.Tournament
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import com.appdev.jphil.basketballcoach.basketball.NationalChampionshipHelper
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDao
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.relations.ConferenceTournamentRelations
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.util.RecordUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class TournamentSimRepository @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val relationalDao: RelationalDao,
    private val gameDao: GameDao,
    private val conferenceDao: ConferenceDao,
    private val database: BasketballDatabase
) {
    private val repositoryScope = CoroutineScope(SupervisorJob() + dispatcherProvider.io)
    private var simJob: Job? = null

    private val _simState = MutableStateFlow<SimulationState?>(null)
    val simState = _simState.asStateFlow()

    fun simulateGame(userTournamentId: Int, gameId: Int) {
        _simState.update { SimulationState() }
        simulateGames(userTournamentId, gameId)
    }

    fun simulateToGame(userTournamentId: Int, gameId: Int) {
        _simState.update { SimulationState(isSimulatingToGame = true) }
        simulateGames(userTournamentId, gameId - 1)
    }

    private fun simulateGames(userTournamentId: Int, lastGameId: Int) {
        simJob?.cancel()
        simJob = repositoryScope.launch {
            _simState.update { it?.copy(isSimActive = true) }
            val allRecruits = RecruitDatabaseHelper.loadAllRecruits(database)
            // load all tournaments
            val tournaments = if (userTournamentId != NationalChampionshipHelper.NATIONAL_CHAMPIONSHIP_ID) {
                loadTournaments(userTournamentId, allRecruits)
            } else {
                listOf(loadNationalChampionship(allRecruits))
            }
            val firstGameId = GameDatabaseHelper.getFirstGameWithIsFinal(false, database) ?: run {
                _simState.update { it?.copy(isSimActive = false) }
                return@launch
            }

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
                    tournaments.first { it.id == game.tournamentId },
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
            tournaments.forEach { tournament ->
                if (tournament.getWinnerOfTournament() != null) {
                    val conference = conferenceDao.getConferenceWithId(tournament.id)!!
                    conferenceDao.insertConference(conference.copy(
                        tournamentIsFinished = true,
                        championId = tournament.getWinnerOfTournament()?.teamId ?: -1
                    ))
                }
            }
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
        val games = tournament.games.filter { it.id != newGame.id } + listOf(newGame)
        val tournamentGames = games.sortedBy { it.id }
        tournament.replaceGames(tournamentGames)

        val newGames = tournament.generateNextRound(2018).map {
            it.apply {
                id = gameDao.insertGame(GameEntity.from(it)).toInt()
            }
        }
        tournament.replaceGames(tournamentGames + newGames)
    }

    private suspend fun loadNationalChampionship(
        allRecruits: List<Recruit>
    ): NationalChampionship {
        val tournamentId = NationalChampionshipHelper.NATIONAL_CHAMPIONSHIP_ID
        val teams = relationalDao.loadTeamsByTournamentId(tournamentId).map {
            GameDatabaseHelper.createTeam(it, allRecruits)
        }
        val games = gameDao.getGamesWithTournamentId(tournamentId).map { game ->
            game.createGame(
                homeTeam = teams.first { it.teamId == game.homeTeamId },
                awayTeam = teams.first { it.teamId == game.awayTeamId }
            )
        }
        return NationalChampionship(
            tournamentId,
            teams
        ).apply {
            replaceGames(games)
            val newGames = generateNextRound(2018).map {
                it.apply {
                    id = gameDao.insertGame(GameEntity.from(it)).toInt()
                }
            }
            replaceGames(games + newGames)
        }
    }

    fun cancelSimulation() {
        simJob?.cancel("Simulation cancelled by user")
        simJob = null
    }
}
