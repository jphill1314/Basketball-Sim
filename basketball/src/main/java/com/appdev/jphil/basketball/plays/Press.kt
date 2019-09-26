package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays

class Press(
    game: Game,
    private val consecutivePresses: Int // must be at least 1
) : BasketballPlay(game) {

    private val pressText = game.pressText
    private val passingUtils = game.passingUtils
    private val deadBall = game.deadball

    private var playerStartsWithBall = playerWithBall
    private lateinit var passer: Player
    private lateinit var passDefender: Player
    private lateinit var target: Player
    private lateinit var targetDefender: Player

    private var targetPos = 0
    private var timeChange = 0

    init {
        type = Plays.PRESS
        foul = Foul(game, FoulType.CLEAN)
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        getPasserAndTarget()

        val passSuccess = ((passer.passing + target.offBallMovement) /
                (r.nextInt(randomBound) + 1.0)).toInt() + r.nextInt(randomBound)
        val defSuccess = ((passDefender.onBallDefense + targetDefender.onBallDefense) /
                (r.nextInt(defense.pressAggression) + 1.0)).toInt() + r.nextInt(randomBound)

        // TODO: when a team is hurrying there should be a greater chance for turnovers and fast breaks
        when {
            passSuccess > defSuccess -> successfulPass(passSuccess, defSuccess)
            defSuccess > (passSuccess + 40) -> stolenPass()
            defSuccess > (passSuccess + 25) -> badPass()
            !deadBall -> justDribbling()
            else -> successfulPass(passSuccess, defSuccess)
            // TODO: 5 second violation
        }

        if (type != Plays.FOUL && !leadToFastBreak && defense.intentionallyFoul) {
            foul = Foul(game, FoulType.INTENTIONAL)
            type = Plays.FOUL
            playAsString += "\n${foul.playAsString}"
        }

        timeRemaining -= timeChange
        shotClock -= timeChange
        return 0
    }

    private fun getPasserAndTarget() {
        if (deadBall) {
            playerStartsWithBall = passingUtils.getInbounder(homeTeamHasBall)
        }
        passer = offense.getPlayerAtPosition(playerStartsWithBall)
        passDefender = defense.getPlayerAtPosition(playerStartsWithBall)

        targetPos = passingUtils.getTarget(homeTeamHasBall, playerWithBall)
        target = offense.getPlayerAtPosition(targetPos)
        targetDefender = defense.getPlayerAtPosition(targetPos)
    }

    private fun successfulPass(passSuccess: Int, defSuccess: Int) {
        playerWithBall = targetPos

        timeChange = when {
            (passSuccess > defSuccess + 50) -> {
                playAsString = if (deadBall) {
                    pressText.inboundToFastBreak(passer, target, consecutivePresses == 1, defense)
                } else {
                    pressText.passToFastBreak(passer, target)
                }
                location = 1
                leadToFastBreak = true
                getTimeChangePaceDependent(9, 3)
            }
            (target.ballHandling + r.nextInt(randomBound) > defense.pressAggression + r.nextInt(randomBound / consecutivePresses)) -> {
                // press broken -> walk ball up court
                playAsString = if (deadBall) {
                    pressText.inboundToWalkToFrontCourt(passer, target, consecutivePresses == 1, defense)
                } else {
                    pressText.passToWalkToFrontCourt(passer, target)
                }
                location = 1
                getTimeChangePaceDependent(9, 5)
            }
            (r.nextInt(10) > 6 - consecutivePresses) -> {
                // ball passed into frontcourt
                playAsString = if (deadBall) {
                    pressText.inboundToFrontCourt(passer, target, consecutivePresses == 1, defense)
                } else {
                    pressText.passToFrontCourt(passer, target)
                }
                location = 1
                getTimeChangePaceDependent(9, 5)
            }
            else -> {
                // ball still in backcourt
                playAsString = if (deadBall) {
                    pressText.inboundToBackCourt(passer, target, consecutivePresses == 1, defense)
                } else {
                    pressText.passToBackCourt(passer, target)
                }
                getTimeChangePaceDependent(3, 1)
            }
        }
    }

    private fun stolenPass() {
        playAsString = if (r.nextBoolean()) {
            playerWithBall = targetPos
            if (deadBall) {
                pressText.stolenInbound(passer, target, targetDefender)
            } else {
                pressText.stolenPass(passer, target, targetDefender)
            }
        } else {
            if (deadBall) {
                pressText.stolenInbound(passer, target, passDefender)
            } else {
                pressText.stolenPass(passer, target, passDefender)
            }
        }
        timeChange = getTimeChangePaceDependent(6, 2)
        foul = Foul(game, FoulType.ON_BALL)
        if (foul.foulType == FoulType.CLEAN) {
            homeTeamHasBall = !homeTeamHasBall
            passer.turnovers++
            if (playerWithBall == playerStartsWithBall) {
                passDefender.steals++
            } else {
                targetDefender.steals++
            }
        } else {
            playAsString += " But, ${foul.playAsString}"
        }
    }

    private fun badPass() {
        playAsString = if (r.nextBoolean()) {
            playerWithBall = targetPos
            if (deadBall) {
                pressText.badInbound(passer, target, targetDefender)
            } else {
                pressText.badPass(passer, target, targetDefender)
            }
        } else {
            if (deadBall) {
                pressText.badInbound(passer, target, targetDefender)
            } else {
                pressText.badPass(passer, target, targetDefender)
            }
        }
        timeChange = getTimeChangePaceDependent(6, 2)
        foul = Foul(game, FoulType.ON_BALL)
        if (foul.foulType == FoulType.CLEAN) {
            homeTeamHasBall = !homeTeamHasBall
            passer.turnovers++
            if (playerWithBall == playerStartsWithBall) {
                passDefender.steals++
            } else {
                targetDefender.steals++
            }
        } else {
            playAsString += " But, ${foul.playAsString}"
        }
    }

    private fun justDribbling() {
        playAsString = pressText.justDribbling(passer)
        timeChange = getTimeChangePaceDependent(2, 1)
        foul = Foul(game, FoulType.ON_BALL)
        if (foul.foulType != FoulType.CLEAN) {
            playAsString += " And, ${foul.playAsString}"
        }
    }
}