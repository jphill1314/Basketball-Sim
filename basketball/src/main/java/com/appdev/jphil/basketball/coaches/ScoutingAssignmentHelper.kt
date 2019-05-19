package com.appdev.jphil.basketball.coaches

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor

object ScoutingAssignmentHelper {

    fun updateScoutingAssignment(team: Team, scoutingAssignment: ScoutingAssignment) {
        for (i in 1..5) {
            if (team.hasNeedAtPosition(i)) {
                if (!scoutingAssignment.positions.contains(i)) {
                    scoutingAssignment.positions.add(i)
                }
            } else {
                if (scoutingAssignment.positions.contains(i)) {
                    scoutingAssignment.positions.remove(i)
                }
            }
        }

        val allowableRange = TeamRecruitInteractor.getRatingRange(team)
        scoutingAssignment.minRating = allowableRange.first
        scoutingAssignment.maxRating = allowableRange.second
    }
}