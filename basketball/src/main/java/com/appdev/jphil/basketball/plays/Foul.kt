package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.teams.Team

class Foul(game: Game, var foulType: FoulType) : BasketballPlay(game) {

    // TODO: simulate different games having different refs -> different chances for different foul calls

    private val foulText = game.foulText
    var isOnDefense = false
    var positionOfPlayerFouled = 0
    var fouler: Player? = null

    init {
        type = Plays.FOUL
        if (generatePlay() == 0) {
            foulType = FoulType.CLEAN
        }

        if (foulType != FoulType.CLEAN) {
            if (isOnDefense) {
                defense.defensiveFouls++
            } else {
                offense.offensiveFouls++
            }
        }
    }

    override fun generatePlay(): Int {
        return when (foulType) {
            FoulType.ON_BALL -> getFloorFoul()
            FoulType.OFF_BALL -> getFloorFoul()
            FoulType.REBOUNDING -> getReboundingFoul()
            FoulType.INTENTIONAL -> getIntentionalFoul()
            FoulType.FAST_BREAK -> getFastBreakFoul()
            FoulType.CLEAN -> 0
            else -> getShootingFoul()
        }
    }

    private fun getFloorFoul(): Int {
        var foulChance = 0.45

        if (foulType == FoulType.OFF_BALL) {
            // foul could be on anyone not with the ball
            if (r.nextDouble() > .75) {
                isOnDefense = false
                positionOfPlayerFouled = getFouler(offense)
                fouler = offense.getPlayerAtPosition(positionOfPlayerFouled)
                val aggression =
                    (offense.getPlayerAtPosition(playerWithBall).aggressiveness + ((offense.aggression + 10) * 5) / 2.0)
                foulChance -= (fouler!!.offBallMovement - aggression) / 20000
                playAsString = foulText.offenseOffBallFoul(
                    fouler!!,
                    defense.getPlayerAtPosition(positionOfPlayerFouled)
                )
            } else {
                isOnDefense = true
                positionOfPlayerFouled = getFouler(defense)
                fouler = defense.getPlayerAtPosition(positionOfPlayerFouled)
                val aggression =
                    (defense.getPlayerAtPosition(playerWithBall).aggressiveness + ((defense.aggression + 10) * 5) / 2.0)
                foulChance -= (fouler!!.offBallDefense - aggression) / 20000
                playAsString = foulText.defenseOffBallFoul(
                    fouler!!,
                    offense.getPlayerAtPosition(positionOfPlayerFouled)
                )
            }
        } else {
            // foul could be on person with ball or their defender
            positionOfPlayerFouled = playerWithBall
            if (r.nextDouble() > .55) {
                isOnDefense = false
                fouler = offense.getPlayerAtPosition(playerWithBall)
                val aggression =
                    (offense.getPlayerAtPosition(playerWithBall).aggressiveness + ((offense.aggression + 10) * 5) / 2.0)
                foulChance -= (fouler!!.ballHandling - aggression) / 20000
                playAsString = foulText.offenseOnBallFoul(
                    fouler!!,
                    defense.getPlayerAtPosition(positionOfPlayerFouled)
                )
            } else {
                isOnDefense = true
                fouler = defense.getPlayerAtPosition(playerWithBall)
                val aggression =
                    (defense.getPlayerAtPosition(playerWithBall).aggressiveness + ((defense.aggression + 10) * 5) / 2.0)
                foulChance -= (fouler!!.onBallDefense - aggression) / 20000
                playAsString = foulText.defenseOnBallFoul(
                    fouler!!,
                    offense.getPlayerAtPosition(positionOfPlayerFouled)
                )
            }
        }

        return if (r.nextDouble() < foulChance) {
            fouler!!.fouls++
            if (!isOnDefense) {
                fouler!!.turnovers++
                offense.turnovers++
            }
            1
        } else {
            0
        }
    }

    private fun getReboundingFoul(): Int {
        var foulChance = .15

        // The assumption is that this is called after it has been decided who is going to get the rebound
        positionOfPlayerFouled = playerWithBall
        val aggression: Double
        fouler = if (r.nextDouble() > 0.4) {
            // more likely that the person who wasn't going to get the rebound is called for the foul
            isOnDefense = true
            aggression = (defense.getPlayerAtPosition(playerWithBall).aggressiveness + ((defense.aggression + 10) * 5) / 2.0)
            defense.getPlayerAtPosition(playerWithBall)
        } else {
            isOnDefense = false
            aggression = (offense.getPlayerAtPosition(playerWithBall).aggressiveness + ((offense.aggression + 10) * 5) / 2.0)
            offense.getPlayerAtPosition(playerWithBall)
        }

        foulChance -= ((fouler!!.rebounding - aggression) / 20000)
        val fouled = if (isOnDefense) offense.getPlayerAtPosition(positionOfPlayerFouled) else defense.getPlayerAtPosition(positionOfPlayerFouled)
        return if (r.nextDouble() < foulChance) {
            playAsString = if (isOnDefense) {
                foulText.defensiveReboundingFoul(fouler!!, fouled)
            } else {
                foulText.offensiveReboundingFoul(fouler!!, fouled)
            }
            fouler!!.fouls++
            1
        } else {
            0
        }
    }

    private fun getShootingFoul(): Int {
        positionOfPlayerFouled = playerWithBall
        var foulChance: Double = when (foulType) {
            FoulType.SHOOTING_CLOSE -> 0.25
            FoulType.SHOOTING_MID -> 0.02
            FoulType.SHOOTING_LONG -> 0.0075
            else -> 0.0
        }

        val defender = defense.getPlayerAtPosition(playerWithBall)
        val defenderAbility: Double = when (foulType) {
            FoulType.SHOOTING_CLOSE -> {
                (defender.onBallDefense + defender.postDefense) / 2.0
            }
            FoulType.SHOOTING_MID -> {
                (defender.onBallDefense + defender.postDefense + defender.perimeterDefense) / 3.0
            }
            FoulType.SHOOTING_LONG -> {
                (defender.onBallDefense + defender.perimeterDefense) / 2.0
            }
            else -> 0.0
        }

        val aggression = (defender.aggressiveness + ((defense.aggression + 10) * 5) / 2.0)

        foulChance -= ((defenderAbility - aggression) / 20000)

        return if (r.nextDouble() < foulChance) {
            isOnDefense = true
            fouler = defender
            defender.fouls++
            1
        } else {
            0
        }
    }

    private fun getIntentionalFoul(): Int {
        positionOfPlayerFouled = playerWithBall
        isOnDefense = true
        fouler = defense.getPlayerAtPosition(playerWithBall)
        playAsString = foulText.intentionalFoul(fouler!!, offense.getPlayerAtPosition(playerWithBall))
        fouler!!.fouls++
        return 1
    }

    private fun getFastBreakFoul(): Int {
        positionOfPlayerFouled = playerWithBall
        isOnDefense = true
        fouler = defense.getPlayerAtPosition(r.nextInt(5) + 1)
        fouler!!.fouls++
        return 1
    }

    private fun getFouler(team: Team): Int {
        val values = Array(5) { 0 }

        for (i in 0..4) {
            values[i] = r.nextInt(100) / team.players[i].offBallDefense
        }

        values[playerWithBall - 1] = -100

        return if (values[0] > values[1] && values[0] > values[2] && values[0] > values[3] && values[0] > values[4]) {
            1
        } else if (values[1] > values[0] && values[1] > values[2] && values[1] > values[3] && values[1] > values[4]) {
            2
        } else if (values[2] > values[1] && values[2] > values[0] && values[2] > values[3] && values[2] > values[4]) {
            3
        } else if ((values[3] > values[1] && values[3] > values[2] && values[4] > values[0] && values[4] > values[4]) || playerWithBall == 5) {
            4
        } else {
            5
        }
    }
}
