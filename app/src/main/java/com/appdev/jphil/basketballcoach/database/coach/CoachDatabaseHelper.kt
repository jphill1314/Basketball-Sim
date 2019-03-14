package com.appdev.jphil.basketballcoach.database.coach

import com.appdev.jphil.basketball.Coach
import com.appdev.jphil.basketballcoach.database.BasketballDatabase

object CoachDatabaseHelper {

    fun loadCoachByTeamId(teamId: Int, database: BasketballDatabase): Coach {
        return database.coachDao().getCoachByTeamId(teamId).createCoach()
    }

    fun saveCoach(coach: Coach, database: BasketballDatabase) {
        database.coachDao().saveCoach(CoachEntity.from(coach))
    }
}