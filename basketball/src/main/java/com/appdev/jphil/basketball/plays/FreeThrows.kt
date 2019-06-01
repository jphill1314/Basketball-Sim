package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.textcontracts.FoulTextContract
import com.appdev.jphil.basketball.textcontracts.FreeThrowTextContract
import kotlin.math.max

class FreeThrows(game: Game, private val numberOfShots: Int) : BasketballPlay(game) {

    private val ftText = game.freeThrowText
    var madeLastShot = true

    init {
        type = Plays.FREE_THROW
        this.location = 1
        foul = Foul(game, FoulType.CLEAN)
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        var made = 0
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        if (numberOfShots > 0) {
            for (i in 1..numberOfShots) {
                offense.freeThrowShots++
                shooter.freeThrowShots++
                if (getFreeThrowMake(shooter)) {
                    made++
                    offense.freeThrowMakes++
                    shooter.freeThrowMakes++
                    if (i == numberOfShots) {
                        homeTeamHasBall = !homeTeamHasBall
                    }
                } else if (i == numberOfShots) {
                    madeLastShot = false
                }
            }
            playAsString = ftText.freeThrowText(shooter, made, numberOfShots)
        } else {
            // 1 and 1 situation
            if (getFreeThrowMake(shooter)) {
                made++
                offense.freeThrowShots++
                offense.freeThrowMakes++
                shooter.freeThrowShots++
                shooter.freeThrowMakes++
                if (getFreeThrowMake(shooter)) {
                    made++
                    offense.freeThrowShots++
                    offense.freeThrowMakes++
                    shooter.freeThrowShots++
                    shooter.freeThrowMakes++
                    homeTeamHasBall = !homeTeamHasBall
                } else {
                    offense.freeThrowShots++
                    shooter.freeThrowShots++
                    madeLastShot = false
                }
            } else {
                offense.freeThrowShots++
                shooter.freeThrowShots++
                madeLastShot = false
            }
            playAsString = ftText.oneAndOneText(shooter, made)
        }
        return made
    }

    private fun getFreeThrowMake(shooter: Player): Boolean {
        return r.nextInt(125) < shooter.freeThrowShot || r.nextBoolean()
    }
}