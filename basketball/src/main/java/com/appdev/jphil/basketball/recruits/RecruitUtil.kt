package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.teams.Team

fun Team.getReturningPlayersAtPosition(position: Int): Int {
    return roster.filter { it.year != 3 && it.position == position }.size
}

fun Team.getCommitsAtPosition(position: Int): Int {
    return commitments.filter { it.position == position }.size
}

fun Team.getActiveRecruitmentsAtPosition(position: Int): Int {
    return coaches.flatMap { it.recruitingAssignments }.filter { it.position == position }.size
}
