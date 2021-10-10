package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays

class FastBreak(game: Game) : BasketballPlay(game) {

    private val fastBreakText = game.fastBreakText

    init {
        type = Plays.SHOT
        foul = Foul(game, FoulType.CLEAN)
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        val timeChange = getTimeChangePaceIndependent(5, 1)
        timeRemaining -= timeChange
        shotClock -= timeChange

        return when {
            shooter.longRangeShot > 60 && r.nextInt(shooter.longRangeShot) < 10 -> getThreePointShot(shooter)
            r.nextInt(100) > 66 -> getFoul(shooter)
            r.nextInt(100) > 95 -> getBlock(shooter)
            else  -> getLayup(shooter)
        }
    }

    private fun getThreePointShot(shooter: Player): Int {
        offense.threePointAttempts++
        shooter.threePointAttempts++
        return if (r.nextDouble() * shooter.longRangeShot + r.nextInt(50) > 50) {
            playAsString = fastBreakText.madeThree(shooter)
            homeTeamHasBall = !homeTeamHasBall
            offense.threePointMakes++
            shooter.threePointMakes++
            3
        } else {
            playAsString = fastBreakText.missedThree(shooter)
            0
        }
    }

    private fun getFoul(shooter: Player): Int {
        foul = Foul(game, FoulType.FAST_BREAK)
        return if (r.nextInt(100) > 90) {
            playAsString = fastBreakText.madeLayupWithFoul(shooter, foul.fouler!!)
            homeTeamHasBall = !homeTeamHasBall
            offense.twoPointAttempts++
            shooter.twoPointAttempts++
            offense.twoPointMakes++
            shooter.twoPointMakes++
            2
        } else {
            playAsString = fastBreakText.missedLayupWithFoul(shooter, foul.fouler!!)
            0
        }
    }

    private fun getBlock(shooter: Player): Int {
        val blocker = defense.getPlayerAtPosition(r.nextInt(5) + 1)
        playAsString = fastBreakText.blockedLayup(shooter, blocker)
        offense.twoPointAttempts++
        shooter.twoPointAttempts++
        return 0
    }

    private fun getLayup(shooter: Player): Int {
        offense.twoPointAttempts++
        shooter.twoPointAttempts++
        return if (r.nextInt(shooter.closeRangeShot) > 3) {
            playAsString = fastBreakText.madeLayup(shooter)
            homeTeamHasBall = !homeTeamHasBall
            offense.twoPointMakes++
            shooter.twoPointMakes++
            2
        } else {
            playAsString = fastBreakText.missedLayup(shooter)
            0
        }
    }
}
