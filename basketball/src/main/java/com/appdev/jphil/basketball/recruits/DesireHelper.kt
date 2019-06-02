package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.teams.Team

object DesireHelper {

    fun teamMeetsDesire(desire: RecruitDesire, team: Team, recruit: Recruit): Double {
        return when (desire) {
            RecruitDesire.COMPETE -> teamMeetsCompeteDesire(team, recruit)
            RecruitDesire.DEVELOP -> teamMeetsDevelopDesire(team, recruit)
            RecruitDesire.STAR -> teamMeetsStarDesire(team, recruit)
            RecruitDesire.GOOD_FIT -> teamMeetsGoodFit(team, recruit)
        }
    }

    private fun teamMeetsCompeteDesire(team: Team, recruit: Recruit): Double {
        val rating = team.teamRating
        val rRating = recruit.rating
        return when {
            rRating <= rating -> .4
            rRating < rating - 5 -> .8
            rRating < rating - 10 -> 1.0
            rRating < rating - 15 -> .7
            rRating < rating - 20 -> .3
            else -> 0.0
        }
    }

    private fun teamMeetsDevelopDesire(team: Team, recruit: Recruit): Double {
        var coachRating = 0
        team.coaches.forEach { coachRating += it.getRating() }
        coachRating /= team.coaches.size
        val rRating = recruit.potential
        return when {
            coachRating >= rRating -> 1.0
            coachRating > rRating - 5 -> .8
            coachRating > rRating - 10 -> .6
            coachRating > rRating - 15 -> .4
            coachRating > rRating - 20 -> .2
            else -> 0.0
        }
    }

    private fun teamMeetsStarDesire(team: Team, recruit: Recruit): Double {
        val rating = team.teamRating
        val rRating = recruit.rating
        return when {
            rRating > rating + 25 -> .2
            rRating > rating + 20 -> .6
            rRating > rating + 15 -> .8
            rRating > rating + 10 -> 1.0
            rRating > rating + 5 -> .7
            rRating > rating -> .5
            rRating > rating - 5 -> .2
            else -> 0.0
        }
    }

    private fun teamMeetsGoodFit(team: Team, recruit: Recruit): Double {
        val rating = team.teamRating
        return when (recruit.rating) {
            rating -> 1.0
            in (rating - 2)..(rating + 2) -> .8
            in (rating - 4)..(rating + 4) -> .6
            in (rating - 6)..(rating + 6) -> .4
            in (rating - 8)..(rating + 8) -> .2
            else -> 0.0
        }
    }
}