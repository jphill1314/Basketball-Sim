package com.appdev.jphil.basketballcoach.database.coach

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.database.BasketballDatabase

object CoachDatabaseHelper {

    suspend fun loadAllCoachesByTeamId(teamId: Int, database: BasketballDatabase): MutableList<Coach> {
        val coaches = database.coachDao().getCoachesByTeamId(teamId)
        val recruits = database.relationalDao().loadAllRecruits()
        val list = mutableListOf<Coach>()
        coaches.forEach { list.add(it.createCoach(recruits)) }
        return list
    }

    suspend fun saveCoach(coach: Coach, database: BasketballDatabase) {
        database.coachDao().saveCoach(CoachEntity.from(coach))
    }
}
