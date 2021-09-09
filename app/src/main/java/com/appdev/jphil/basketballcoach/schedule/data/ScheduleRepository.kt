package com.appdev.jphil.basketballcoach.schedule.data

import com.appdev.jphil.basketballcoach.basketball.NationalChampionshipHelper
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDao
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val gameDao: GameDao,
    private val conferenceDao: ConferenceDao,
    private val database: BasketballDatabase
) {

    fun doesTournamentExistForConference(conferenceId: Int): Flow<Boolean> {
        return gameDao.getGamesWithTournamentIdFlow(conferenceId).map { it.isNotEmpty() }
    }

    fun doesNationalChampionshipExist(): Flow<Boolean> {
        return gameDao.getGamesWithTournamentIdFlow(NationalChampionshipHelper.NATIONAL_CHAMPIONSHIP_ID).map {
            it.isNotEmpty()
        }
    }

    fun areAllGamesComplete(): Flow<Boolean> {
        return gameDao.getFirstGameWithIsFinalFlow(false).map { it == null }
    }

    fun isSeasonFinished(): Flow<Boolean> { // TODO: remember to consider national championship
        return conferenceDao.getAllConferenceEntitiesFlow().map { list ->
            list.map { it.tournamentIsFinished }.reduce { acc, b -> acc && b }
        }
    }

    suspend fun getGames(): Flow<List<ScheduleDataModel>> {
        val userTeam = TeamDatabaseHelper.loadUserTeam(database)!!.teamId
        return gameDao.getAllGamesFlow()
            .map { entities ->
                entities.map { entity ->
                    entity.toDataModel(userTeam)
                }
            }
    }

    suspend fun getGamesForDialog(): Flow<List<ScheduleDataModel>> {
        val userTeam = TeamDatabaseHelper.loadUserTeam(database)!!.teamId
        val firstGameId = gameDao.getFirstGameWithIsFinal(false) ?: return emptyFlow()
        val firstId = firstGameId - 1
        return gameDao.getAllGamesWithIsFinalFlow(true, firstId).map { entities ->
            entities.map { it.toDataModel(userTeam) }
        }
    }

    private fun GameEntity.toDataModel(userTeamId: Int) = ScheduleDataModel(
        gameId = id ?: -1,
        topTeamId = awayTeamId,
        bottomTeamId = homeTeamId,
        topTeamName = awayTeamName,
        bottomTeamName = homeTeamName,
        topTeamScore = awayScore,
        bottomTeamScore = homeScore,
        isConferenceGame = isConferenceGame,
        isInProgress = inProgress,
        isFinal = isFinal,
        isHomeTeamUser = homeTeamId == userTeamId,
        tournamentId = tournamentId
    )
}
