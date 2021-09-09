package com.appdev.jphil.basketballcoach.tournament.data

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.tournament.TournamentType
import com.appdev.jphil.basketballcoach.basketball.NationalChampionshipHelper
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.relations.ConferenceTournamentRelations
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.database.team.TeamDao
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.util.RecordUtil
import timber.log.Timber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TournamentRepository @Inject constructor(
    @ConferenceId private val userConferenceId: Int,
    @TeamId private val userTeamId: Int,
    private val gameDao: GameDao,
    private val teamDao: TeamDao,
    private val relationalDao: RelationalDao,
    private val database: BasketballDatabase,
    private val championshipHelper: NationalChampionshipHelper
) {

    suspend fun generateTournaments(tournamentId: Int) {
        if (tournamentId != NationalChampionshipHelper.NATIONAL_CHAMPIONSHIP_ID) {
            val allRecruits = RecruitDatabaseHelper.loadAllRecruits(database)
            loadTournaments(
                userConferenceId, // TODO: make sure this is always the user's conference
                allRecruits
            )
        } else {
            Timber.d("Creating national championship")
            championshipHelper.createNationalChampionship()
        }
    }

    suspend fun getTournamentType(tournamentId: Int): TournamentType {
        return if (tournamentId != NationalChampionshipHelper.NATIONAL_CHAMPIONSHIP_ID) {
            val conferenceRelation = relationalDao.loadConferenceTournamentData(tournamentId)
            val allRecruits = RecruitDatabaseHelper.loadAllRecruits(database)
            val conference = Conference(
                conferenceRelation.conferenceEntity.id,
                conferenceRelation.conferenceEntity.name,
                conferenceRelation.teamEntities.map { it.create(allRecruits) }
            )
            conference.tournamentType
        } else {
            TournamentType.NATIONAL_CHAMPIONSHIP
        }
    }

    fun getGamesForTournament(tournamentId: Int): Flow<List<TournamentDataModel>> {
        return gameDao.getGamesWithTournamentIdFlow(tournamentId)
            .map { flow -> flow.map { it.toDataModel() } }
    }

    suspend fun getGamesForDialog(): Flow<List<TournamentDataModel>> {
        val firstGameId = gameDao.getFirstGameWithIsFinal(false) ?: return emptyFlow()
        val firstId = firstGameId - 1
        return gameDao.getAllGamesWithIsFinalFlow(true, firstId).map { entities ->
            entities.map { it.toDataModel() }
        }
    }

    private fun GameEntity.toDataModel() = TournamentDataModel(
        gameId = id ?: -1,
        topTeamId = homeTeamId,
        bottomTeamId = awayTeamId,
        topTeamName = homeTeamName,
        bottomTeamName = awayTeamName,
        topTeamScore = homeScore,
        bottomTeamScore = awayScore,
        topTeamSeed = homeTeamSeed,
        bottomTeamSeed = awayTeamSeed,
        isInProgress = inProgress,
        isFinal = isFinal,
        isHomeTeamUser = homeTeamId == userTeamId,
        isUserGame = homeTeamId == userTeamId || awayTeamId == userTeamId
    )

    private suspend fun loadTournaments(
        userConferenceId: Int,
        allRecruits: List<Recruit>
    ) {
        val games = gameDao.getNonTournamentGames()
        val conferenceRelations = relationalDao.loadAllConferenceTournamentData()

        conferenceRelations.filter {
            it.conferenceEntity.id != userConferenceId
        }.map {
            loadConferenceAndTournament(it, games, allRecruits)
        }
        conferenceRelations.first {
            it.conferenceEntity.id == userConferenceId
        }.let { loadConferenceAndTournament(it, games, allRecruits) }
    }

    private suspend fun loadConferenceAndTournament(
        conferenceRelation: ConferenceTournamentRelations,
        games: List<GameEntity>,
        allRecruits: List<Recruit>
    ) {
        val conference = Conference(
            conferenceRelation.conferenceEntity.id,
            conferenceRelation.conferenceEntity.name,
            conferenceRelation.teamEntities.map { it.create(allRecruits) }
        )

        conference.generateTournament(conference.teams.map { RecordUtil.getRecord(games, it) })
        conference.tournament?.let { tournament ->
            val tournamentGames = conferenceRelation.tournamentGameEntities.map { entity ->
                entity.createGame(
                    homeTeam = conference.teams.first { it.teamId == entity.homeTeamId },
                    awayTeam = conference.teams.first { it.teamId == entity.awayTeamId }
                )
            }
            tournament.replaceGames(tournamentGames)
            val sortedIds = tournament.teams.map { it.teamId }
            val newGames = tournament.generateNextRound(2018).map {
                it.apply {
                    id = gameDao.insertGame(
                        GameEntity.from(
                            it,
                            homeTeamSeed = sortedIds.indexOf(it.homeTeam.teamId) + 1,
                            awayTeamSeed = sortedIds.indexOf(it.awayTeam.teamId) + 1
                        )
                    ).toInt()
                }
            }
            tournament.replaceGames(tournamentGames + newGames)
            tournament
        } ?: throw IllegalStateException("Unable to load tournament for conference")
    }
}
