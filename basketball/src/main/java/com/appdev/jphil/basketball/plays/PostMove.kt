package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.textcontracts.FoulTextContract
import com.appdev.jphil.basketball.textcontracts.PostMoveTextContract

class PostMove(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    foulText: FoulTextContract,
    val assisted: Boolean,
    val passer: Player,
    private val postMoveText: PostMoveTextContract
) : BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, foulText) {

    init {
        type = Plays.SHOT
        foul = Foul(
            homeTeamHasBall,
            timeRemaining,
            shotClock,
            homeTeam,
            awayTeam,
            playerWithBall,
            location,
            foulText,
            FoulType.CLEAN
        )
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        val defender = defense.getPlayerAtPosition(playerWithBall)
        var shotSuccess = shooter.postMove - defender.postDefense + r.nextInt(2 * randomBound) - randomBound

        if (assisted) {
            shotSuccess += 30
        }

        foul = Foul(
            homeTeamHasBall,
            timeRemaining,
            shotClock,
            homeTeam,
            awayTeam,
            playerWithBall,
            location,
            foulText,
            FoulType.SHOOTING_CLOSE
        )

        if (foul.foulType != FoulType.CLEAN) {
            type = Plays.FOUL
            shotSuccess -= 30
        }

        val timeChange = timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
        timeRemaining -= timeChange
        shotClock -= timeChange

        shooter.twoPointAttempts++
        offense.twoPointAttempts++
        return if (shotSuccess > 30) {
            // made shot
            playAsString = postMoveText.madeShot(shooter, type == Plays.FOUL)
            shooter.twoPointMakes++
            offense.twoPointMakes++
            if (assisted) {
                passer.assists++
            }
            if (type != Plays.FOUL) {
                homeTeamHasBall = !homeTeamHasBall
            }

            2
        } else {
            // missed shot
            playAsString = postMoveText.missedShot(shooter, type == Plays.FOUL)
            if (type == Plays.FOUL) {
                shooter.twoPointAttempts--
                offense.twoPointAttempts--
            }
            0
        }
    }
}