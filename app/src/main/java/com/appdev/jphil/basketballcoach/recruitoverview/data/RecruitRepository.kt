package com.appdev.jphil.basketballcoach.recruitoverview.data

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.coach.CoachDao
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDao
import com.appdev.jphil.basketballcoach.database.recruit.RecruitEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitInterestEntity
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.database.team.TeamDao
import com.appdev.jphil.basketballcoach.database.team.TeamEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RecruitRepository @Inject constructor(
    private val teamDao: TeamDao,
    private val coachDao: CoachDao,
    private val recruitDao: RecruitDao,
    private val relationalDao: RelationalDao
) {

    fun getRecruitAndTeam(teamId: Int, recruitId: Int): Flow<RecruitData> {
        val teamFlow = relationalDao.loadTeamByIdFlow(teamId)
        val recruitFlow = relationalDao.loadAllRecruitsFlow()

        return combine(teamFlow, recruitFlow) { teamData, recruitData ->
            val allRecruits = recruitData.map { it.create() }
            val team = teamData.create(allRecruits)

            RecruitData(
                team = team,
                recruit = allRecruits.first { it.id == recruitId }
            )
        }
    }

    suspend fun updateCoach(coach: Coach) {
        coachDao.saveCoach(CoachEntity.from(coach))
    }

    suspend fun updateTeam(team: Team) {
        teamDao.insertTeam(TeamEntity.from(team))
    }

    suspend fun updateRecruit(recruit: Recruit) {
        val recruitInterests = recruit.recruitInterests.map { RecruitInterestEntity.from(recruit.id, it) }
        val recruitEntity = RecruitEntity.from(recruit)

        recruitDao.insertInterests(recruitInterests)
        recruitDao.insertRecruits(listOf(recruitEntity))
    }
}

data class RecruitData(
    val recruit: Recruit,
    val team: Team
)
