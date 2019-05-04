package com.appdev.jphil.basketballcoach.database.team

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.players.PlayerProgression
import com.appdev.jphil.basketball.players.PracticeType
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerProgressionEntity

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
    val lastScoreDif: Int,
    val practiceType: Int
) {

    fun createTeam(players: List<PlayerEntity>, coaches: List<CoachEntity>, progression: List<PlayerProgressionEntity?>): Team {
        val teamPlayers = mutableListOf<Player>()
        players.forEach { player ->
            val p = player.createPlayer()
            val progress = progression.filter { it?.playerId == p.id!! }
            p.progression = if (progress.isNotEmpty()) {
                progress[0]?.createProgression(p) ?: PlayerProgression(null, p)
            } else {
                PlayerProgression(null, p)
            }
            teamPlayers.add(p)
        }
        val teamCoaches = mutableListOf<Coach>()
        coaches.forEach { coach -> teamCoaches.add(coach.createCoach()) }
        val team = Team(
            teamId,
            name,
            abbreviation,
            teamPlayers,
            conferenceId,
            isUser,
            teamCoaches
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

        team.practiceType = when (practiceType) {
            PracticeType.OFFENSE.type -> PracticeType.OFFENSE
            PracticeType.DEFENSE.type -> PracticeType.DEFENSE
            PracticeType.CONDITIONING.type -> PracticeType.CONDITIONING
            else -> PracticeType.NO_FOCUS
        }

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
                team.lastScoreDiff,
                team.practiceType.type
            )
        }
    }
}