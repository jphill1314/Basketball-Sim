package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.plays.utils.PassingUtils
import com.appdev.jphil.basketball.playtext.PressPlayText
import com.appdev.jphil.basketball.textcontracts.PressTextContract


class Press(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    private val deadBall: Boolean,
    private val passingUtils: PassingUtils,
    private val consecutivePresses: Int, // must be at least 1
    private val pressText: PressTextContract = PressPlayText()
) : BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    private var playerStartsWithBall = playerWithBall
    private lateinit var passer: Player
    private lateinit var passDefender: Player
    private lateinit var target: Player
    private lateinit var targetDefender: Player

    private var targetPos = 0
    private var timeChange = 0

    init {
        type = Plays.PRESS
        foul = Foul(
            homeTeamHasBall,
            timeRemaining,
            shotClock,
            homeTeam,
            awayTeam,
            playerWithBall,
            location,
            FoulType.CLEAN
        )
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        println("PRESS")
        getPasserAndTarget()

        val passSuccess = ((passer.passing + target.offBallMovement) /
                (r.nextInt(randomBound) + 1.0)).toInt() + r.nextInt(randomBound)
        val defSuccess = ((passDefender.onBallDefense + targetDefender.onBallDefense) /
                (r.nextInt(defense.pressAggression) + 1.0)).toInt() + r.nextInt(randomBound)

        when {
            passSuccess > defSuccess -> successfulPass(passSuccess, defSuccess)
            defSuccess > (passSuccess + 40) -> {
                stolenPass()
                println("stolen pass")
            }
            defSuccess > (passSuccess + 25) -> {
                badPass()
                println("bad pass")
            }
            !deadBall -> justDribbling()
            else -> successfulPass(passSuccess, defSuccess)
            // TODO: 5 second violation
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
                timeUtil.smartTimeChange(9 - ((offense.pace / 90.0) * r.nextInt(6)).toInt(), shotClock)
            }
            (target.ballHandling + r.nextInt(randomBound) > defense.pressAggression + r.nextInt(randomBound / consecutivePresses)) -> {
                // press broken -> walk ball up court
                playAsString = if (deadBall) {
                    pressText.inboundToWalkToFrontCourt(passer, target, consecutivePresses == 1, defense)
                } else {
                    pressText.passToWalkToFrontCourt(passer, target)
                }
                location = 1
                timeUtil.smartTimeChange(9 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
            }
            (r.nextInt(10) > 6 - consecutivePresses) -> {
                // ball passed into frontcourt
                playAsString = if (deadBall) {
                    pressText.inboundToFrontCourt(passer, target, consecutivePresses == 1, defense)
                } else {
                    pressText.passToFrontCourt(passer, target)
                }
                location = 1
                timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
            }
            else -> {
                // ball still in backcourt
                playAsString = if (deadBall) {
                    pressText.inboundToBackCourt(passer, target, consecutivePresses == 1, defense)
                } else {
                    pressText.passToBackCourt(passer, target)
                }
                timeUtil.smartTimeChange(3 - ((offense.pace / 90.0) * r.nextInt(2)).toInt(), shotClock)
            }
        }
    }

    private fun stolenPass() {
        timeChange = timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
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
        timeChange = timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
        foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.ON_BALL)
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
        timeChange = timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
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
        timeChange = timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
        foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.ON_BALL)
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
        timeChange = timeUtil.smartTimeChange(2 - ((offense.pace / 90.0) * r.nextInt(1)).toInt(), shotClock)
        foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.ON_BALL)
        if (foul.foulType != FoulType.CLEAN) {
            playAsString += " And, ${foul.playAsString}"
        }
    }
}