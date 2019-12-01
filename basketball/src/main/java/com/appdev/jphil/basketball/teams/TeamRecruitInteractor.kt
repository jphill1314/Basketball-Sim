package com.appdev.jphil.basketball.teams

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitingEvent
import kotlin.math.max
import kotlin.math.min

object TeamRecruitInteractor {

    const val GAMES_BETWEEN_INTERACTIONS = 1

    fun interactWithRecruits(team: Team, allRecruits: List<Recruit>) {
        val recruits = mutableListOf<Recruit>()
        team.knownRecruits.forEach {
            recruits.add(allRecruits[allRecruits.indexOf(it)]) // want to use up to date recruits / ones in team might have old data
        }

        for (position in 1..5) {
            if (team.hasNeedAtPosition(position)) {
                recruits.filter { it.position == position }
                    .filter { !it.isCommitted }
                    .filter { isInterestedInRecruit(team, it) }
                    .forEach { recruit ->
                        if (team.hasNeedAtPosition(position)) {
                            interactWithRecruit(recruit, team)
                        }
                    }
            } else {
                recruits.filter { it.position == position }.forEach { it.revokeScholarship(team.teamId) }
            }
        }
    }

    private fun interactWithRecruit(recruit: Recruit, team: Team) {
        val interest = recruit.interestInTeams.firstOrNull { it.teamId == team.teamId }

        if (interest == null) {
            return
        }

        if (interest.lastInteractionGame + GAMES_BETWEEN_INTERACTIONS <= team.gamesPlayed) {
            if (recruit.getRatingRangeForTeam(team.teamId) > 10) {
                recruit.updateInterest(team, RecruitingEvent.SCOUT, team.gamesPlayed)
            } else if (!interest.isOfferedScholarship){
                recruit.updateInterest(team, RecruitingEvent.OFFER_SCHOLARSHIP, team.gamesPlayed)
            } else {
                recruit.considerScholarship(team)
            }
        }
    }

    private fun isInterestedInRecruit(team: Team, recruit: Recruit): Boolean {
        val recruitRating = (recruit.getRatingMaxForTeam(team.teamId) + recruit.getRatingMinForTeam(team.teamId)) / 2
        val allowableRange = getRatingRange(team)
        return recruitRating in allowableRange.first..allowableRange.second
    }

    fun getRatingRange(team: Team): Pair<Int, Int> {
        val min = max(0, team.teamRating - (team.gamesPlayed * 2))
        val max = min(100, team.teamRating + 60 - (team.gamesPlayed * 2))
        return Pair(min, max)
    }
}