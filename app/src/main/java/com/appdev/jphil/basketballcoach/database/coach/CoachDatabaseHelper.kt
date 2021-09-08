package com.appdev.jphil.basketballcoach.database.coach

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketballcoach.database.BasketballDatabase

object CoachDatabaseHelper {

    suspend fun loadHeadCoachByTeamId(teamId: Int, database: BasketballDatabase): Coach {
        val coaches = database.coachDao().getCoachesByTeamId(teamId)
        return coaches.first { it.type == CoachType.HEAD_COACH }.createCoach(emptyList())
    }

    suspend fun loadAllCoachesByTeamId(teamId: Int, database: BasketballDatabase): MutableList<Coach> {
        val coaches = database.coachDao().getCoachesByTeamId(teamId)
        val recruits = database.relationalDao().loadAllRecruits()
        val list = mutableListOf<Coach>()
        coaches.forEach { list.add(it.createCoach(recruits)) }
        return list
    }

    suspend fun loadCoachById(coachId: Int, database: BasketballDatabase): Coach {
        val coach = database.coachDao().getCoachById(coachId)
        val recruits = database.relationalDao().loadAllRecruits()
        return coach.createCoach(recruits)
    }

    suspend fun saveCoach(coach: Coach, database: BasketballDatabase) {
        database.coachDao().saveCoach(CoachEntity.from(coach))
    }
}
