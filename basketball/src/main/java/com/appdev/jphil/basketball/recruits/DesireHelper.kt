package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.teams.Team

object DesireHelper {

    fun teamMeetsDesire(desire: RecruitDesire, team: Team, recruit: Recruit): Boolean {
        return when (desire) {
            RecruitDesire.COMPETE -> teamMeetsCompeteDesire(team, recruit)
            RecruitDesire.DEVELOP -> teamMeetsDevelopDesire(team, recruit)
            RecruitDesire.STAR -> teamMeetsStarDesire(team, recruit)
        }
    }

    private fun teamMeetsCompeteDesire(team: Team, recruit: Recruit): Boolean {
        return team.teamRating - 10 > recruit.rating
    }

    private fun teamMeetsDevelopDesire(team: Team, recruit: Recruit): Boolean {
        var coachRating = 0
        team.coaches.forEach { coachRating += it.getRating() }
        coachRating /= team.coaches.size
        return coachRating >= recruit.potential
    }

    private fun teamMeetsStarDesire(team: Team, recruit: Recruit): Boolean {
        return team.teamRating + 10 < recruit.rating
    }
}