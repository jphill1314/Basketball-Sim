package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.teams.Team

object RecruitUtil {

    fun getReturningPlayersAtPosition(team: Team, position: Int): Int {
        return team.roster.filter { it.year != 3 && it.position == position }.size
    }

    fun getCommitsAtPosition(team: Team, position: Int): Int {
        return team.commitments.filter { it.position == position }.size
    }

    fun teamHasOpenSpot(team: Team, position: Int): Boolean {
        return getReturningPlayersAtPosition(team, position) + getCommitsAtPosition(team, position) < 3
    }
}

fun Team.getReturningPlayersAtPosition(position: Int): Int {
    return roster.filter { it.year != 3 && it.position == position }.size
}

fun Team.getCommitsAtPosition(position: Int): Int {
    return commitments.filter { it.position == position }.size
}
