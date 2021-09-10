package com.appdev.jphil.basketballcoach.database.team

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.location.Location
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PracticeType
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketballcoach.database.typeconverters.IntListTypeConverter

@Entity
@TypeConverters(IntListTypeConverter::class)
data class TeamEntity(
    @PrimaryKey
    val teamId: Int,
    val schoolName: String,
    val mascot: String,
    val abbreviation: String,
    val color: Int,
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
    val practiceType: PracticeType,
    val gamesPlayed: Int,
    val rating: Int,
    val postseasonTournamentId: Int,
    val postseasonTournamentSeed: Int,
    val prestige: Int,
    val location: Location,
    val commitments: List<Int>,
) {

    fun createTeam(
        players: MutableList<Player>,
        coaches: MutableList<Coach>,
        allRecruits: List<Recruit>
    ): Team {
        val team = Team(
            teamId,
            schoolName,
            mascot,
            abbreviation,
            TeamColor.fromInt(color),
            players,
            conferenceId,
            isUser,
            coaches,
            location,
            prestige,
            gamesPlayed,
            postseasonTournamentId,
            postseasonTournamentSeed
        )

        if (allRecruits.isNotEmpty()) {
            team.commitments.addAll(commitments.map { cId -> allRecruits.first { it.id == cId } })
        }
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
        team.practiceType = practiceType

        return team
    }

    companion object {
        fun from(team: Team): TeamEntity {
            return TeamEntity(
                team.teamId,
                team.schoolName,
                team.mascot,
                team.abbreviation,
                team.color.type,
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
                team.practiceType,
                team.gamesPlayed,
                team.teamRating,
                team.postSeasonTournamentId,
                team.postSeasonTournamentSeed,
                team.prestige,
                team.location,
                team.commitments.map { it.id },
            )
        }
    }
}
