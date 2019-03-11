package com.appdev.jphil.basketballcoach.database.team

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity

@Entity
data class TeamEntity(
    @PrimaryKey
    val teamId: Int,
    val name: String,
    val abbreviation: String,
    val offenseFavorsThrees: Int,
    val defenseFavorsThrees: Int,
    val pressFrequency : Int,
    val pressAggression: Int,
    val aggression: Int,
    val pace: Int,
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
    val isUser: Boolean
) {

    fun createTeam(players: List<PlayerEntity>): Team {
        val teamPlayers = mutableListOf<Player>()
        players.forEach { player -> teamPlayers.add(player.createPlayer()) }
        val team = Team(
            teamId,
            name,
            abbreviation,
            offenseFavorsThrees,
            defenseFavorsThrees,
            pressFrequency,
            pressAggression,
            aggression,
            pace,
            teamPlayers,
            conferenceId,
            isUser
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

        return team
    }

    companion object {
        fun from(team: Team): TeamEntity {
            return TeamEntity(
                team.teamId,
                team.name,
                team.abbreviation,
                team.offenseFavorsThrees,
                team.defenseFavorsThrees,
                team.pressFrequency,
                team.pressAggression,
                team.aggression,
                team.pace,
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
                team.isUser
            )
        }
    }
}