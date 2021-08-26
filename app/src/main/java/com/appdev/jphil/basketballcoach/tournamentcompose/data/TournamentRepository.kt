package com.appdev.jphil.basketballcoach.tournamentcompose.data

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.tournament.Tournament
import com.appdev.jphil.basketball.tournament.TournamentType
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.relations.ConferenceTournamentRelations
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.database.team.TeamDao
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleDataModel
import com.appdev.jphil.basketballcoach.util.RecordUtil
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class TournamentRepository @Inject constructor(
    private val gameDao: GameDao,
    private val teamDao: TeamDao,
    private val relationalDao: RelationalDao,
    private val database: BasketballDatabase
) {

    suspend fun generateTournaments(conferenceId: Int) {
        val allRecruits = RecruitDatabaseHelper.loadAllRecruits(database)
        loadTournaments(
            conferenceId, // TODO: make sure this is always the user's conference
            allRecruits
        )
    }

    suspend fun getTournamentType(conferenceId: Int): TournamentType {
        val conferenceRelation = relationalDao.loadConferenceTournamentData(conferenceId)
        val allRecruits = RecruitDatabaseHelper.loadAllRecruits(database)
        val conference = Conference(
            conferenceRelation.conferenceEntity.id,
            conferenceRelation.conferenceEntity.name,
            conferenceRelation.teamEntities.map {
                GameDatabaseHelper.createTeam(it, allRecruits)
            }
        )
        return conference.tournamentType
    }

    suspend fun getGamesForTournament(tournamentId: Int): Flow<List<ScheduleDataModel>> {
        val userTeamId = teamDao.getUserTeamId(true)
        return gameDao.getGamesWithTournamentIdFlow(tournamentId)
            .map { flow -> flow.map { it.toDataModel(userTeamId) } }
    }

    suspend fun getGamesForDialog(): Flow<List<ScheduleDataModel>> {
        val userTeamId = TeamDatabaseHelper.loadUserTeam(database)!!.teamId
        val firstGameId = gameDao.getFirstGameWithIsFinal(false) ?: return emptyFlow()
        val firstId = firstGameId - 1
        return gameDao.getAllGamesWithIsFinalFlow(true, firstId).map { entities ->
            entities.map { it.toDataModel(userTeamId) }
        }
    }

    // TODO: use TournamentDataModel when this screen gets its own views
    private fun GameEntity.toDataModel(userTeamId: Int) = ScheduleDataModel(
        gameId = id ?: -1,
        topTeamId = awayTeamId,
        bottomTeamId = homeTeamId,
        topTeamName = awayTeamName,
        bottomTeamName = homeTeamName,
        topTeamScore = awayScore,
        bottomTeamScore = homeScore,
        isInProgress = inProgress,
        isFinal = isFinal,
        isHomeTeamUser = homeTeamId == userTeamId,
        tournamentId = tournamentId
    )

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
}
