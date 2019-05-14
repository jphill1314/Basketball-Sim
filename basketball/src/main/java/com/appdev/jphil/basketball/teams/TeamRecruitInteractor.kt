package com.appdev.jphil.basketball.teams

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitInterest
import com.appdev.jphil.basketball.recruits.RecruitingEvent

object TeamRecruitInteractor {

    const val GAMES_BETWEEN_INTERACTIONS = 1

    fun interactWithRecruits(team: Team, recruits: List<Recruit>) {
        val teamRating = team.teamRating
        for (position in 1..5) {
            if (hasNeedAtPosition(position, team, recruits)) {
                recruits.filter { it.position == position }
                    .filter { !it.isCommitted }
                    .filter { teamRating - 10 <= it.rating && teamRating + 10 >= it.rating }
                    .forEach { recruit ->
                        if (hasNeedAtPosition(position, team, recruits)) {
                            interactWithRecruit(recruit, team)
                        }
                    }
            } else {
                recruits.filter { it.position == position }.forEach { it.revokeScholarship(team.teamId) }
            }
        }
    }

    private fun hasNeedAtPosition(position: Int, team: Team, recruits: List<Recruit>): Boolean {
        var players = team.roster.filter { it.position == position && it.year != 3 }.size
        players += recruits.filter { it.isCommitted && it.teamCommittedTo == team.teamId && it.position == position }.size
        return 3 - players > 0
    }

    private fun interactWithRecruit(recruit: Recruit, team: Team) {
        val interest = getInterestInTeam(recruit, team)

        if (interest.lastInteractionGame + GAMES_BETWEEN_INTERACTIONS <= team.gamesPlayed) {
            when {
                !interest.isScouted -> InteractWithRecruitHelper.notScouted(recruit, interest, team)
                !interest.isContacted -> InteractWithRecruitHelper.notContacted(recruit, interest, team)
                !interest.isOfferedScholarship -> InteractWithRecruitHelper.noScholarshipOffer(recruit, interest, team)
                !interest.isOfficialVisitDone -> InteractWithRecruitHelper.noOfficialVisit(recruit, interest, team)
                else -> InteractWithRecruitHelper.notCommitted(recruit, interest, team)
            }
        }
    }

    private fun getInterestInTeam(recruit: Recruit, team: Team): RecruitInterest {
        val interestInTeam = recruit.interestInTeams.filter { it.teamId == team.teamId }
        return if (interestInTeam.isEmpty()) {
            recruit.generateInitialInterest(team)
            recruit.interestInTeams.filter { it.teamId == team.teamId }.first()
        } else {
            interestInTeam.first()
        }
    }
}