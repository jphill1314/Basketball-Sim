package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.factories.PlayerFactory
import com.appdev.jphil.basketball.game.Game
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
    val potential: Int,
    var isCommitted: Boolean,
    var teamCommittedTo: Int,
    val interestInTeams: MutableList<RecruitInterest>
) {

    val fullName = "$firstName $lastName"

    fun generateInitialInterest(team: Team) {
        val interest = min(100, (Random.nextInt(RecruitInterest.MAX_INTEREST / 2) * getTeamMultiplier(team)).toInt())
        interestInTeams.add(RecruitInterest(
            null,
            id,
            team.teamId,
            team.name,
            interest,
            false,
            false,
            false,
            false,
            -1000,
            0,
            0,
            false
        ))
    }

    fun revokeScholarship(teamId: Int) {
        interestInTeams.firstOrNull { it.teamId == teamId }?.let { interest ->
            if (interest.isOfferedScholarship && !isCommitted) {
                interest.revokeScholarshipOffer()
            }
        }
    }

    fun updateInterest(team: Team, event: RecruitingEvent, gameNumber: Int) {
        if (!isCommitted) {
            val interestInTeam = interestInTeams.first { it.teamId == team.teamId }
            isCommitted = interestInTeam.updateInterest(getTeamMultiplier(team), event, gameNumber)
            if (isCommitted) {
                teamCommittedTo = team.teamId
            }
        }
    }

    fun updateInterestAfterGame(game: Game) {
        interestInTeams.firstOrNull { it.teamId == game.homeTeam.teamId }?.let { homeInterest ->
            if (homeInterest.isScouted) {
                homeInterest.onTeamGameCompleted(game, getTeamMultiplier(game.homeTeam))
                this@Recruit.considerScholarship(game.homeTeam)
            }
        }

        interestInTeams.firstOrNull { it.teamId == game.awayTeam.teamId }?.let { awayInterest ->
            if (awayInterest.isScouted) {
                awayInterest.onTeamGameCompleted(game, getTeamMultiplier(game.awayTeam))
                this@Recruit.considerScholarship(game.awayTeam)
            }
        }
    }

    fun considerScholarship(team: Team) {
        if (!isCommitted) {
            val interestInTeam = interestInTeams.first { it.teamId == team.teamId }
            isCommitted = interestInTeam.considerScholarship()
            if (isCommitted) {
                teamCommittedTo = team.teamId
            }
        }
    }

    private fun getTeamMultiplier(team: Team): Double {
        // Players are more interested in a good team that they can immediately start at
        var multiplier = 1.0
        val ratingDifference = team.teamRating - rating
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
            playerType,
            potential
        )
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Recruit) other.id == id else false
    }
}