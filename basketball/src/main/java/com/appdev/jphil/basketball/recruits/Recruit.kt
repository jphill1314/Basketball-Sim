package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.factories.PlayerFactory
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerType
import kotlin.math.min
import kotlin.random.Random

class Recruit(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val position: Int,
    val playerType: PlayerType,
    val rating: Int,
    var isCommitted: Boolean,
    val interestInTeams: MutableList<RecruitInterest>
) {

    fun generateInitialInterest(team: Team) {
        val interest = min(100, (Random.nextInt(RecruitInterest.MAX_INTEREST / 2) * getTeamMultiplier(team)).toInt())
        interestInTeams.add(RecruitInterest(null, id, team.teamId, interest, false, false, false, false))
    }

    fun updateInterest(team: Team, event: RecruitingEvent) {
        val interestInTeam = interestInTeams.filter { it.teamId == team.teamId }[0]
        interestInTeam.updateInterest(getTeamMultiplier(team), event)
    }

    private fun getTeamMultiplier(team: Team): Double {
        // Players are more interested in a good team that they can immediately start at
        var multiplier = 1.0
        val ratingDifference = rating - team.teamRating
        val playersInPosition = team.players.filter { it.position == position && it.year != 3 }.size

        multiplier += .25 * (ratingDifference / 10)
        if (playersInPosition > 1) {
            multiplier -= .33 * (playersInPosition - 1)
        } else {
            multiplier += .33 * (2 - playersInPosition)
        }

        if (multiplier < .1) {
            multiplier = .1
        }

        return multiplier
    }

    fun generatePlayer(teamId: Int, index: Int): Player {
        return PlayerFactory.generatePlayerWithType(
            firstName,
            lastName,
            position,
            0,
            teamId,
            rating,
            index,
            playerType
        )
    }
}