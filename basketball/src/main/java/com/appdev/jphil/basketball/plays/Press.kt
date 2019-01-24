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
    private val passingUtils: PassingUtils
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

        val passSuccess = passer.passing + target.offBallMovement + r.nextInt(randomBound)
        val defSuccess = ((passDefender.onBallDefense + targetDefender.onBallDefense) /
                (101.0 - defense.pressAggression)).toInt() + r.nextInt(randomBound)

        // if successful -> chance to walk up court, chance for fast break
        if (passSuccess > defSuccess) {
            successfulPass(passSuccess, defSuccess)
        } else if (defSuccess > passSuccess + 25 /* number is arbitrary should be tuned a bit, maybe higher ceiling but random?*/) {
          stolenPass()
        } else if (defSuccess > passSuccess + 10) {
            badPass()
        } else if (!deadBall) {
            justDribbling()
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
        playAsString = if (deadBall) {
            "${passer.fullName} inbounds the ball to ${target.fullName}"
        } else {
            "${passer.fullName} passes the ball to ${target.fullName}"
        }
        playerWithBall = targetPos

        timeChange = if (passSuccess > defSuccess + 25) {
            // TODO: great pass -> fast break -> easy bucket
            playAsString += " and it's a great pass! ${target.firstName} has a clear path to the basket!"
            location = 1
            timeUtil.smartTimeChange(9 - ((offense.pace / 90.0) * r.nextInt(6)).toInt(), shotClock)
        } else if (target.ballHandling > defense.pressAggression + r.nextInt(50)) {
            // press broken -> walk ball up court
            playAsString += " and ${defense.Name}'s press relents, so ${target.firstName} walks the ball into the frontcourt"
            location = 1
            timeUtil.smartTimeChange(9 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
        } else if (r.nextInt(10) > 6) {
            // ball passed into frontcourt
            playAsString += " who is in the frontcourt, breaking the press."
            location = 1
            timeUtil.smartTimeChange(6 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
        } else {
            // ball still in backcourt
            playAsString += "."
            timeUtil.smartTimeChange(4 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
        }
    }

    private fun stolenPass() {
        playAsString = if (deadBall) {
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
        playAsString = "${passer.fullName} is dribbling with the ball."
        timeChange = timeUtil.smartTimeChange(4 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
    }
}