package com.appdev.jphil.basketballcoach.database.team

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity

@Entity
data class TeamEntity(
    @PrimaryKey
    val teamId: Int,
    val name: String,
    val abbreviation: String,
    val conferenceId: Int,
    val twoPointAttempts: Int,
    val twoPointMakes: Int,
    val threePointAttempts: Int,
    val threePointMakes: Int,
    val offensiveRebounds: Int,
    val defensiveRebounds: Int,
    val turnovers: Int,
    val offensiveFouls: Int,
    val defensiveFouls: Int,
    val freeThrowShots: Int,
    val freeThrowMakes: Int,
    val isUser: Boolean,
    val lastScoreDif: Int
) {

    fun createTeam(players: List<PlayerEntity>, coach: CoachEntity): Team {
        val teamPlayers = mutableListOf<Player>()
        players.forEach { player -> teamPlayers.add(player.createPlayer()) }
        val team = Team(
            teamId,
            name,
            abbreviation,
            teamPlayers,
            conferenceId,
            isUser,
            coach.createCoach()
        )

        team.twoPointAttempts = twoPointAttempts
        team.twoPointMakes = twoPointMakes
        team.threePointAttempts = threePointAttempts
        team.threePointMakes = threePointMakes
        team.offensiveRebounds = offensiveRebounds
        team.defensiveRebounds = defensiveRebounds
        team.turnovers = turnovers
        team.offensiveFouls = offensiveFouls
        team.defensiveFouls = defensiveFouls
        team.freeThrowShots = freeThrowShots
        team.freeThrowMakes = freeThrowMakes
        team.lastScoreDiff = lastScoreDif

        return team
    }

    companion object {
        fun from(team: Team): TeamEntity {
            return TeamEntity(
                team.teamId,
                team.name,
                team.abbreviation,
                team.conferenceId,
                team.twoPointAttempts,
                team.twoPointMakes,
                team.threePointAttempts,
                team.threePointMakes,
                team.offensiveRebounds,
                team.defensiveRebounds,
                team.turnovers,
                team.offensiveFouls,
                team.defensiveFouls,
                team.freeThrowShots,
                team.freeThrowMakes,
                team.isUser,
                team.lastScoreDiff
            )
        }
    }
}