package com.appdev.jphil.basketballcoach.database.game

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

@Entity
@TypeConverters(GameTypeConverter::class)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val tournamentId: Int?,
    val season: Int,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val isNeutralCourt: Boolean,
    val isFinal: Boolean,
    val inProgress: Boolean,
    val shotClock: Int,
    val timeRemaining: Int,
    val lastTimeRemaining: Int,
    val half: Int,
    val homeScore: Int,
    val awayScore: Int,
    val homeFouls: Int,
    val awayFouls: Int,
    val homeTeamHasBall: Boolean,
    val deadball: Boolean,
    val madeShot: Boolean,
    val shootFreeThrows: Boolean,
    val numberOfFreeThrows: Int,
    val playerWithBall: Int,
    val location: Int,
    val possessions: Int,
    val mediaTimeouts: MutableList<Boolean>,
    val homeTeamName: String,
    val awayTeamName: String,
    val homeTeamHasPossessionArrow: Boolean,
    val homeTimeouts: Int,
    val awayTimeouts: Int,
    val consecutivePresses: Int,
    val timeInBackcourt: Int,
    val lastPassWasGreat: Boolean,
    val lastPlayerWithBall: Int
) {
    fun createGame(homeTeam: Team, awayTeam: Team): Game {
        val game = Game(homeTeam, awayTeam, isNeutralCourt, season, id, tournamentId, isFinal)
        game.inProgress = inProgress
        game.shotClock = shotClock
        game.timeRemaining = timeRemaining
        game.lastTimeRemaining = lastTimeRemaining
        game.half = half
        game.homeScore = homeScore
        game.awayScore = awayScore
        game.homeFouls = homeFouls
        game.awayFouls = awayFouls
        game.homeTeamHasBall = homeTeamHasBall
        game.deadball = deadball
        game.madeShot = madeShot
        game.shootFreeThrows = shootFreeThrows
        game.numberOfFreeThrows = numberOfFreeThrows
        game.playerWithBall = playerWithBall
        game.location = location
        game.possessions = possessions
        game.mediaTimeOuts = mediaTimeouts
        game.homeTeamHasPossessionArrow = homeTeamHasPossessionArrow
        game.homeTimeouts = homeTimeouts
        game.awayTimeouts = awayTimeouts
        game.consecutivePresses = consecutivePresses
        game.timeInBackcourt = timeInBackcourt
        game.lastPassWasGreat = lastPassWasGreat
        game.lastPlayerWithBall = lastPlayerWithBall
        return game
    }

    companion object {
        fun from(game: Game): GameEntity {
            return GameEntity(
                game.id,
                game.tournamentId,
                game.season,
                game.homeTeam.teamId,
                game.awayTeam.teamId,
                game.isNeutralCourt,
                game.isFinal,
                game.inProgress,
                game.shotClock,
                game.timeRemaining,
                game.lastTimeRemaining,
                game.half,
                game.homeScore,
                game.awayScore,
                game.homeFouls,
                game.awayFouls,
                game.homeTeamHasBall,
                game.deadball,
                game.madeShot,
                game.shootFreeThrows,
                game.numberOfFreeThrows,
                game.playerWithBall,
                game.location,
                game.possessions,
                game.mediaTimeOuts,
                game.homeTeam.name,
                game.awayTeam.name,
                game.homeTeamHasPossessionArrow,
                game.homeTimeouts,
                game.awayTimeouts,
                game.consecutivePresses,
                game.timeInBackcourt,
                game.lastPassWasGreat,
                game.lastPlayerWithBall
            )
        }
    }
}