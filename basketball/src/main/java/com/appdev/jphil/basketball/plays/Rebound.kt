package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.teams.Team

class Rebound(game: Game) : BasketballPlay(game) {

    private val reboundText = game.reboundText

    init {
        type = Plays.REBOUND
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        val offChance = getReboundChance(offense)
        val defChance = getReboundChance(defense)

        offChance[playerWithBall - 1] -= 15 // less likely to get own rebound
        var offHigh = 0
        var defHigh = 0

        for (i in 1..4) {
            if (offChance[i] > offChance[offHigh]) {
                offHigh = i
            }

            if (defChance[i] > defChance[defHigh]) {
                defHigh = i
            }
        }

        if (offChance[offHigh] > defChance[defHigh] && r.nextInt() > 60) {
            offensiveRebound(offHigh)
        } else {
            defensiveRebound(defHigh)
        }

        return 0
    }

    private fun getReboundChance(team: Team): ArrayList<Int> {
        val values = ArrayList<Int>()
        for (i in 0..4) {
            val player = team.players[i]
            values.add(player.rebounding + player.aggressiveness / 5 + r.nextInt(4 * randomBound))
        }
        return values
    }

    private fun offensiveRebound(offHigh: Int) {
        playerWithBall = offHigh + 1 // convert index to basketball position
        foul = Foul(game, FoulType.REBOUNDING)
        playAsString = if (foul.foulType == FoulType.CLEAN) {
            offense.offensiveRebounds++
            offense.getPlayerAtPosition(playerWithBall).offensiveRebounds++
            reboundText.offensiveRebound(offense.getPlayerAtPosition(playerWithBall))
        } else {
            foul.playAsString
        }
    }

    private fun defensiveRebound(defHigh: Int) {
        playerWithBall = defHigh + 1 // convert index to basketball position
        homeTeamHasBall = !homeTeamHasBall
        foul = Foul(game, FoulType.REBOUNDING)
        playAsString = if (foul.foulType == FoulType.CLEAN) {
            defense.defensiveRebounds++
            defense.getPlayerAtPosition(playerWithBall).defensiveRebounds++
            reboundText.defensiveRebound(defense.getPlayerAtPosition(playerWithBall))
        } else {
            foul.playAsString
        }
    }
}
