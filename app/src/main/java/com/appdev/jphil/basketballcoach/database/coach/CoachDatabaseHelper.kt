package com.appdev.jphil.basketballcoach.database.coach

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketballcoach.database.BasketballDatabase

object CoachDatabaseHelper {

    fun loadHeadCoachByTeamId(teamId: Int, database: BasketballDatabase): Coach {
        val coaches = database.coachDao().getCoachesByTeamId(teamId)
        return coaches.first { it.type == CoachType.HEAD_COACH.type }.let {
            it.createCoach(database.coachDao().getScoutingAssignmentByCoachId(it.id!!))
        }
    }

    fun loadAllCoachesByTeamId(teamId: Int, database: BasketballDatabase): MutableList<Coach> {
        val coaches = database.coachDao().getCoachesByTeamId(teamId)
        val list = mutableListOf<Coach>()
        coaches.forEach { list.add(it.createCoach(database.coachDao().getScoutingAssignmentByCoachId(it.id!!))) }
        return list
    }

    fun saveCoach(coach: Coach, database: BasketballDatabase) {
        database.coachDao().saveCoach(CoachEntity.from(coach))
        coach.id?.let {
            database.coachDao().saveScoutingAssignment(ScoutingAssignmentEntity.from(it, coach.scoutingAssignment))
        }
    }
}