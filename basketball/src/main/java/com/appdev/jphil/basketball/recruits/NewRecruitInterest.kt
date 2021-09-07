package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.location.getRegion
import com.appdev.jphil.basketball.teams.Team
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class NewRecruitInterest(
    val team: Team,
    private val preferredPrestige: Int,
    private val wantsClose: Boolean,
    private val wantsFar: Boolean,
    private val wantsImmediateStart: Boolean,
    private val wantsToDevelop: Boolean,
    private val wantsThrees: Boolean,
    private val wantsPress: Boolean,
    private val wantsAggressive: Boolean,
    private val wantsToBeStar: Boolean,
    var prestigeInterest: Int? = null,
    var locationInterest: Int? = null,
    var playingTimeInterest: Int? = null,
    var playStyleInterest: Int? = null,
    var teamAbilityInterest: Int? = null,
    var recruitmentInterest: Int = 0,
) {

    private companion object {
        const val MAX_DESIRE = 20
        const val MAX_CHANGE_FROM_RECRUITMENT = 10
    }

    fun createInitialInterest(recruit: Recruit) {
        if (team.prestige > 75) {
            prestigeInterest = getInterestFromPrestige()
        }
        if (team.location == recruit.location) {
            locationInterest = getInterestFromLocation(recruit)
        }
    }

    fun doActiveRecruitment(recruit: Recruit, game: Game, recruitingCoach: Coach?) {
        if (recruitingCoach != null) {
            when {
                prestigeInterest == null -> {
                    prestigeInterest = getInterestFromPrestige()
                }
                locationInterest == null -> {
                    locationInterest = getInterestFromLocation(recruit)
                }
                playingTimeInterest == null -> {
                    playingTimeInterest = getInterestFromPlayingTime(recruit)
                }
                playStyleInterest == null -> {
                    playStyleInterest = getInterestFromPlayStyle(team.getHeadCoach())
                }
                teamAbilityInterest == null -> {
                    teamAbilityInterest = getInterestFromTeamAbility(recruit)
                }
            }
        }

        recruitmentInterest += getInterestFromActiveRecruitment(game, recruit, recruitingCoach)
    }

    private fun allowPassiveGrowth(recruit: Recruit): Boolean {
        if (team.prestige > 75) {
            return true
        }
        if (team.location.getRegion() == recruit.location.getRegion() && team.prestige > 50) {
            return true
        }
        if (team.location == recruit.location && team.prestige > 25) {
            return true
        }
        return false
    }

    private fun getInterestFromActiveRecruitment(
        game: Game,
        recruit: Recruit,
        recruitingCoach: Coach?
    ): Int {
        val maxInterestChange = MAX_CHANGE_FROM_RECRUITMENT * game.getInterestModifier()
        if (recruitingCoach == null) {
            return if (allowPassiveGrowth(recruit)) (maxInterestChange * 0.25).toInt() else 0
        }

        val interestChange = if (maxInterestChange > 0) {
            maxInterestChange * Random.nextDouble(recruitingCoach.recruiting / 100.0)
        } else {
            maxInterestChange - maxInterestChange * Random.nextDouble(recruitingCoach.recruiting / 100.0)
        }
        return interestChange.toInt()
    }

    private fun Game.getInterestModifier(): Int {
        val teamRating: Int
        val teamScore: Int
        val otherRating: Int
        val otherScore: Int
        if (homeTeam.teamId == team.teamId) {
            teamRating = homeTeam.teamRating
            teamScore = homeScore
            otherRating = awayTeam.teamRating
            otherScore = awayScore
        } else {
            teamRating = awayTeam.teamRating
            teamScore = awayScore
            otherRating = homeTeam.teamRating
            otherScore = homeScore
        }
        val ratingModifier = if (isNeutralCourt) 10 else 15

        return if (tournamentId != null) {
            if (teamScore > otherScore) 2 else -2 // Tournament games always count extra
        } else if (teamRating + ratingModifier < otherRating && teamScore > otherScore) {
            2 // Upset win -> extra increase from game
        } else if (teamRating - ratingModifier > otherRating && teamScore < otherScore) {
            -2 // Upset lose -> extra decrease from game
        } else {
            1 // Win vs lose doesn't matter extra
        }
    }

    private fun getInterestFromPrestige(): Int {
        return max(0, min(MAX_DESIRE, team.prestige - preferredPrestige + MAX_DESIRE))
    }

    private fun getInterestFromLocation(recruit: Recruit): Int {
        return when {
            wantsClose -> when {
                team.location == recruit.location -> MAX_DESIRE
                team.location.getRegion() == recruit.location.getRegion() -> MAX_DESIRE / 2
                else -> 0
            }
            wantsFar -> when {
                team.location.getRegion() != recruit.location.getRegion() -> MAX_DESIRE
                team.location != recruit.location -> MAX_DESIRE / 2
                else -> 0
            }
            else -> MAX_DESIRE / 2
        }
    }

    private fun getInterestFromPlayingTime(recruit: Recruit): Int {
        val competition = team.players.filter {
            it.year < 3 && it.position == recruit.position
        }.sortedByDescending {
            it.getOverallRating()
        }

        val recruitRating = recruit.rating
        val firstStringRating = competition.firstOrNull()?.getOverallRating() ?: 0
        val secondStringRating = competition.getOrNull(1)?.getOverallRating() ?: 0
        return when {
            wantsImmediateStart -> when {
                recruitRating > firstStringRating + 10 -> MAX_DESIRE
                recruitRating > secondStringRating + 10 -> MAX_DESIRE / 2
                else -> 0
            }
            wantsToDevelop -> {
                when {
                    recruitRating > firstStringRating + 10 -> 0
                    recruitRating > secondStringRating + 10 -> MAX_DESIRE / 2
                    else -> MAX_DESIRE
                }
            }
            else -> MAX_DESIRE / 2
        }
    }

    private fun getInterestFromPlayStyle(coach: Coach): Int {
        val threesMatch = when {
            wantsThrees -> when {
                coach.offenseFavorsThrees > 70 -> MAX_DESIRE
                coach.offenseFavorsThrees > 50 -> MAX_DESIRE / 2
                else -> 0
            }
            else -> when {
                coach.offenseFavorsThrees < 30 -> MAX_DESIRE
                coach.offenseFavorsThrees < 50 -> MAX_DESIRE / 2
                else -> 0
            }
        }

        val pressMatch = when {
            wantsPress -> when {
                coach.pressFrequency > 50 -> MAX_DESIRE
                coach.pressFrequency > 25 -> MAX_DESIRE / 2
                else -> 0
            }
            else -> when {
                coach.pressFrequency > 25 -> MAX_DESIRE / 2
                coach.pressFrequency > 50 -> MAX_DESIRE
                else -> 0
            }
        }

        val aggroMatch = when {
            wantsAggressive -> when {
                coach.aggression > 70 -> MAX_DESIRE
                coach.aggression > 50 -> MAX_DESIRE / 2
                else -> 0
            }
            else -> when {
                coach.aggression < 20 -> MAX_DESIRE
                coach.aggression < 50 -> MAX_DESIRE / 2
                else -> 0
            }
        }

        return ((threesMatch + pressMatch + aggroMatch) / 3.0).toInt()
    }

    private fun getInterestFromTeamAbility(recruit: Recruit): Int {
        return when {
            wantsToBeStar -> when {
                recruit.rating > team.teamRating + 10 -> MAX_DESIRE
                recruit.rating >= team.teamRating -> MAX_DESIRE / 2
                else -> 0
            }
            wantsToDevelop -> when {
                recruit.rating > team.teamRating -> 0
                recruit.rating >= team.teamRating - 10 -> MAX_DESIRE / 2
                else -> MAX_DESIRE
            }
            else -> MAX_DESIRE / 2
        }
    }
}
