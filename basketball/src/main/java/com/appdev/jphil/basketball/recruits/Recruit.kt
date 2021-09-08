package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.factories.PlayerFactory
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.location.Location
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerType
import com.appdev.jphil.basketball.teams.Team

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
    val location: Location,
    val recruitInterests: MutableList<NewRecruitInterest>,
) {
    val fullName = "$firstName $lastName"

    fun getInterest(teamId: Int): Int {
        return recruitInterests.first { it.teamId == teamId }.getInterest()
    }

    fun getRecruitmentLevel(teamId: Int): Int {
        return recruitInterests.first { it.teamId == teamId }.getRecruitmentLevel()
    }

    fun updateRecruitment(team: Team, game: Game, coach: Coach?) {
        if (!isCommitted) {
            recruitInterests.first { it.teamId == team.teamId }.doActiveRecruitment(
                team,
                this,
                game,
                coach
            )
        }
    }

    fun generateInitialInterests(teams: List<Team>, desires: RecruitDesireData) {
        recruitInterests.clear()

        teams.forEach { team ->
            val interest = desires.createInterest(team)
            interest.createInitialInterest(team, this)
            recruitInterests.add(interest)
        }
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
            true, // TODO: handle walk ons
            potential
        )
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Recruit) other.id == id else false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
