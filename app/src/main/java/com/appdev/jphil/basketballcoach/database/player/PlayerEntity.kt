package com.appdev.jphil.basketballcoach.database.player

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appdev.jphil.basketball.Pronouns
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerType

@Entity
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val teamId: Int,
    val firstName: String,
    val lastName: String,
    val position: Int,
    val rating: Int,
    val year: Int,
    val type: Int,
    val isOnScholarship: Boolean,
    val closeRangeShot: Int,
    val midRangeShot: Int,
    val longRangeShot: Int,
    val freeThrowShot: Int,
    val postMove: Int,
    val ballHandling: Int,
    val passing: Int,
    val offBallMovement: Int,
    val postDefense: Int,
    val perimeterDefense: Int,
    val onBallDefense: Int,
    val offBallDefense: Int,
    val stealing: Int,
    val rebounding: Int,
    val stamina: Int,
    val aggressiveness: Int,
    val offensiveStatMod: Int,
    val defensiveStatMod: Int,
    val fatigue: Double,
    val timePlayed: Int,
    val inGame: Boolean,
    val twoPointAttempts: Int,
    val twoPointMakes: Int,
    val threePointAttempts: Int,
    val threePointMakes: Int,
    val assists: Int,
    val offensiveRebounds: Int,
    val defensiveRebounds: Int,
    val turnovers: Int,
    val steals: Int,
    val fouls: Int,
    val freeThrowShots: Int,
    val freeThrowMakes: Int,
    val potential: Int,
    val rosterIndex: Int,
    val courtIndex: Int,
    val pronouns: Pronouns
) {

    fun createPlayer(): Player {
        val pType = when (type) {
            1 -> PlayerType.SHOOTER
            2 -> PlayerType.DISTRIBUTOR
            3 -> PlayerType.REBOUNDER
            4 -> PlayerType.DEFENDER
            else -> PlayerType.BALANCED
        }

        val player = Player(
            id!!,
            teamId,
            firstName,
            lastName,
            position,
            year,
            pType,
            isOnScholarship,
            closeRangeShot,
            midRangeShot,
            longRangeShot,
            freeThrowShot,
            postMove,
            ballHandling,
            passing,
            offBallMovement,
            postDefense,
            perimeterDefense,
            onBallDefense,
            offBallDefense,
            stealing,
            rebounding,
            stamina,
            aggressiveness,
            potential,
            rosterIndex,
            courtIndex,
            pronouns
        )

        player.offensiveStatMod = offensiveStatMod
        player.defensiveStatMod = defensiveStatMod
        player.fatigue = fatigue
        player.timePlayed = timePlayed
        player.inGame = inGame
        player.twoPointAttempts = twoPointAttempts
        player.twoPointMakes = twoPointMakes
        player.threePointAttempts = threePointAttempts
        player.threePointMakes = threePointMakes
        player.assists = assists
        player.offensiveRebounds = offensiveRebounds
        player.defensiveRebounds = defensiveRebounds
        player.turnovers = turnovers
        player.steals = steals
        player.fouls = fouls
        player.freeThrowShots = freeThrowShots
        player.freeThrowMakes = freeThrowMakes

        return player
    }

    companion object {
        fun from(player: Player): PlayerEntity {
            return PlayerEntity(
                player.id,
                player.teamId,
                player.firstName,
                player.lastName,
                player.position,
                player.getOverallRating(),
                player.year,
                player.type.type,
                player.isOnScholarship,
                player.closeRangeShot,
                player.midRangeShot,
                player.longRangeShot,
                player.freeThrowShot,
                player.postMove,
                player.ballHandling,
                player.passing,
                player.offBallMovement,
                player.postDefense,
                player.perimeterDefense,
                player.onBallDefense,
                player.offBallDefense,
                player.stealing,
                player.rebounding,
                player.stamina,
                player.aggressiveness,
                player.offensiveStatMod,
                player.defensiveStatMod,
                player.fatigue,
                player.timePlayed,
                player.inGame,
                player.twoPointAttempts,
                player.twoPointMakes,
                player.threePointAttempts,
                player.threePointMakes,
                player.assists,
                player.offensiveRebounds,
                player.defensiveRebounds,
                player.turnovers,
                player.steals,
                player.fouls,
                player.freeThrowShots,
                player.freeThrowMakes,
                player.potential,
                player.rosterIndex,
                player.courtIndex,
                player.pronouns
            )
        }
    }
}
