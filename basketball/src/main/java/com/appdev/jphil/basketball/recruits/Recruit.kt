package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.factories.PlayerFactory
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerType
import com.appdev.jphil.basketball.teams.Team
import kotlin.random.Random

class Recruit(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val position: Int,
    val playerType: PlayerType,
    val desires: List<RecruitDesire>,
    val rating: Int,
    val potential: Int,
    var isCommitted: Boolean,
    var teamCommittedTo: Int,
    val interestInTeams: MutableList<RecruitInterest>
) {

    val fullName = "$firstName $lastName"

    fun generateInitialInterest(team: Team, coachRecruitingRating: Int) {
        var interest = 100.0

        if (desires.isNotEmpty()) {
            val maxChange = 100.0 / desires.size
            desires.forEach { interest -= (1 - DesireHelper.teamMeetsDesire(it, team, this)) * maxChange }
        }

        if (!team.hasNeedAtPosition(position)) {
            interest = 0.0
        }

        val range = Random.nextInt(101 - coachRecruitingRating)
        val offset = if (range > 0 ) Random.nextInt(range) - range / 2 else 0

        interestInTeams.add(RecruitInterest(
            null,
            id,
            team.teamId,
            team.name,
            interest.toInt(),
            range,
            offset,
            false,
            -1000,
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
            if (isCommitted) {
                if (teamCommittedTo == homeInterest.teamId) {
                    homeInterest.interest = 100
                } else {
                    homeInterest.interest = 0
                }
            } else if (game.homeTeam.hasNeedAtPosition(position)) {
                homeInterest.onTeamGameCompleted(game, getTeamMultiplier(game.homeTeam))
                considerScholarship(game.homeTeam)
            } else {
                homeInterest.interest = 0
            }
        }

        interestInTeams.firstOrNull { it.teamId == game.awayTeam.teamId }?.let { awayInterest ->
            if (isCommitted) {
                if (teamCommittedTo == awayInterest.teamId) {
                    awayInterest.interest = 100
                } else {
                    awayInterest.interest = 0
                }
            } else if (game.awayTeam.hasNeedAtPosition(position)) {
                awayInterest.onTeamGameCompleted(game, getTeamMultiplier(game.awayTeam))
                considerScholarship(game.awayTeam)
            } else {
                awayInterest.interest = 0
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

    fun getRatingMinForTeam(teamId: Int) =
        interestInTeams.firstOrNull { it.teamId == teamId }?.getMinRating(rating) ?: 0

    fun getRatingMaxForTeam(teamId: Int) =
        interestInTeams.firstOrNull { it.teamId == teamId }?.getMaxRating(rating) ?: 100

    fun getRatingRangeForTeam(teamId: Int) =
        interestInTeams.firstOrNull { it.teamId == teamId }?.ratingRange ?: 100

    override fun equals(other: Any?): Boolean {
        return if (other is Recruit) other.id == id else false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}