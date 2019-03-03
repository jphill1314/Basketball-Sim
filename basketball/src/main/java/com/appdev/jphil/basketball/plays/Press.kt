package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team


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
    private val consecutivePresses: Int // must be at least 1
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

        playAsString = when (consecutivePresses) {
            1 -> "${defense.name} will pick up the press here as "
            else -> ""
        }

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
        playAsString += if (deadBall) {
            "${passer.fullName} inbounds the ball to ${target.fullName}"
        } else {
            "${passer.fullName} passes the ball to ${target.fullName}"
        }
        playerWithBall = targetPos

        timeChange = when {
            (passSuccess > defSuccess + 100) -> {
                // TODO: great pass -> fast break -> easy bucket
                playAsString += " and it's a great pass! ${target.firstName} has a clear path to the basket!"
                location = 1
                timeUtil.smartTimeChange(9 - ((offense.pace / 90.0) * r.nextInt(6)).toInt(), shotClock)
            }
            (target.ballHandling + r.nextInt(randomBound) > defense.pressAggression + r.nextInt(randomBound / consecutivePresses)) -> {
                // press broken -> walk ball up court
                playAsString += " and ${defense.name}'s press relents, so ${target.firstName} walks the ball into the frontcourt"
                location = 1
                timeUtil.smartTimeChange(9 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
            }
            (r.nextInt(10) > 6 - consecutivePresses) -> {
                // ball passed into frontcourt
                playAsString += " who is in the frontcourt, breaking the press."
                location = 1
                timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
            }
            else -> {
                // ball still in backcourt
                playAsString += "."
                timeUtil.smartTimeChange(3 - ((offense.pace / 90.0) * r.nextInt(2)).toInt(), shotClock)
            }
        }
    }

    private fun stolenPass() {
        playAsString += if (deadBall) {
            "${passer.fullName} inbounds the ball to ${target.fullName}"
        } else {
            "${passer.fullName} passes the ball to ${target.fullName}"
        }

        timeChange = timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
        if (r.nextBoolean()) {
            playerWithBall = targetPos
            playAsString += ", but the pass is stolen by ${targetDefender.fullName}!"
        } else {
            playAsString += ", but the pass is stolen by ${passDefender.fullName}!"
        }
        timeChange = timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
        homeTeamHasBall = !homeTeamHasBall
        // todo turnovers and stats and fouls etc
    }

    private fun badPass() {
        // todo: bespoke sim for bad pass
        stolenPass()
    }

    private fun justDribbling() {
        playAsString += "${passer.fullName} is dribbling with the ball."
        timeChange = timeUtil.smartTimeChange(2 - ((offense.pace / 90.0) * r.nextInt(1)).toInt(), shotClock)
    }
}