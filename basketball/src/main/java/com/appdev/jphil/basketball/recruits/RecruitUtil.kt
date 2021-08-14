package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.teams.Team

object RecruitUtil {

    fun getReturningPlayersAtPosition(team: Team, position: Int): Int {
        return team.roster.filter { it.year != 3 && it.position == position }.size
    }

    fun getCommitsAtPosition(team: Team, position: Int): Int {
        return team.knownRecruits.filter { it.isCommitted && it.teamCommittedTo == team.teamId && it.position == position }.size
    }

    fun teamHasOpenSpot(team: Team, position: Int): Boolean {
        return getReturningPlayersAtPosition(team, position) + getCommitsAtPosition(team, position) < 3
    }
}
