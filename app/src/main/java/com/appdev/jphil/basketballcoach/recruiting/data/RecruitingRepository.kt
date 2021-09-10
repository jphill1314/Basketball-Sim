package com.appdev.jphil.basketballcoach.recruiting.data

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class RecruitingRepository @Inject constructor(
    private val relationalDao: RelationalDao
) {

    fun getRecruitingData(teamId: Int): Flow<RecruitingData> {
        val recruitsFlow = relationalDao.loadAllRecruitsFlow()
        val teamFlow = relationalDao.loadTeamByIdFlow(teamId)

        return combine(recruitsFlow, teamFlow) { r, t ->
            val recruits = r.map { it.create() }
            val team = t.create(recruits)
            RecruitingData(team, recruits)
        }
    }
}

data class RecruitingData(
    val team: Team,
    val recruits: List<Recruit>
)
