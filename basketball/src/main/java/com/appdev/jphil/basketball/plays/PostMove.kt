package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.textcontracts.FoulTextContract
import com.appdev.jphil.basketball.textcontracts.PostMoveTextContract

class PostMove(
    game: Game,
    private val assisted: Boolean,
    private val passer: Player
) : BasketballPlay(game) {

    private val postMoveText = game.postMoveText

    init {
        type = Plays.SHOT
        foul = Foul(game, FoulType.CLEAN)
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        val defender = defense.getPlayerAtPosition(playerWithBall)
        var shotSuccess = r.nextInt(shooter.postMove) - r.nextInt(defender.postDefense)

        if (assisted) {
            shotSuccess += 30
        }

        foul = Foul(game, FoulType.SHOOTING_CLOSE)

        if (foul.foulType != FoulType.CLEAN) {
            type = Plays.FOUL
            shotSuccess -= 30
        }

        val timeChange = timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
        timeRemaining -= timeChange
        shotClock -= timeChange

        shooter.twoPointAttempts++
        offense.twoPointAttempts++
        return if (shotSuccess > 10 && r.nextDouble() > 0.2) {
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