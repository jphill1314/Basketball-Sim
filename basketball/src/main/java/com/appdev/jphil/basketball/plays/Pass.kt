package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team


class Pass(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    private val deadBall: Boolean,
    private val passingUtils: PassingUtils
) :
    BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    private var playerStartsWithBall = playerWithBall
    private lateinit var passer: Player
    private lateinit var passDefender: Player
    private lateinit var target: Player
    private lateinit var targetDefender: Player

    private var timeChange = 0

    init {
        super.type = Plays.PASS
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
        if (deadBall) {
            playerWithBall = passingUtils.getInbounder(homeTeamHasBall)
            playerStartsWithBall = playerWithBall
        }

        passer = offense.getPlayerAtPosition(playerWithBall)
        passDefender = defense.getPlayerAtPosition(playerWithBall)

        val targetPos: Int = passingUtils.getTarget(homeTeamHasBall, playerWithBall)
        target = offense.getPlayerAtPosition(targetPos)
        targetDefender = defense.getPlayerAtPosition(targetPos)

        val passSuccess = (passer.passing + target.offBallMovement) / (r.nextInt(randomBound) + 1)
        val stealSuccess =
            ((passDefender.onBallDefense + passDefender.stealing + targetDefender.offBallDefense + targetDefender.stealing) / 2) / (r.nextInt(
                randomBound
            ) + 1)

        //println("pass success: $passSuccess vs. pass fail:${(defense.aggression + (passDefender.aggressiveness + targetDefender.aggressiveness) / 15)}")
        if (passSuccess >= (defense.aggression + (passDefender.aggressiveness + targetDefender.aggressiveness) / 15) || location == -1) {
            // successful pass
            //TODO: add chance to have the ball knocked out of bounds
            playAsString = if (deadBall) {
                "${passer.fullName} inbounds the ball to ${target.fullName}"
            } else {
                "${passer.fullName} passes the ball to ${target.fullName}"
            }
            playerWithBall = targetPos
            timeChange = if (location < 1) {
                // the ball was inbounded in the backcourt so more time needs to come off the clock
                playAsString += " who brings the ball into the front court."
                location = 1
                timeUtil.smartTimeChange(9 - ((offense.pace / 90.0) * r.nextInt(6)).toInt(), shotClock)
            } else {
                //TODO: add pass leading to a shot / post move / etc
                playAsString += "."
                timeUtil.smartTimeChange(8 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
            }
        } else if (passSuccess < ((defense.aggression + (passDefender.aggressiveness + targetDefender.aggressiveness) / 15)) - 6) {
            // Bad pass -> turnover
            if (r.nextInt(100) > 60) {
                // target of pass is at fault
                playerWithBall = targetPos
                target.turnovers++
                playAsString = if (deadBall) {
                    "${passer.fullName} inbounds the ball to ${target.fullName} who cannot handle the pass and turns it over!"
                } else {
                    "${passer.fullName} passes the ball to ${target.fullName} who cannot handle the pass and turns it over!"
                }
            } else {
                // passer is at fault
                passer.turnovers++
                playAsString = if (deadBall) {
                    "${passer.fullName} turns the ball over with a bad inbounds pass!"
                } else {
                    "${passer.fullName} turns the ball over with a horrid pass!"
                }
            }
            offense.turnovers++
            timeChange = timeUtil.smartTimeChange(4 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
            homeTeamHasBall = !homeTeamHasBall
        } else if (stealSuccess > (100 - defense.aggression - ((passDefender.aggressiveness + targetDefender.aggressiveness) / 2))) {
            // Pass is stolen by defense
            playAsString = if (deadBall) {
                "${passer.fullName} inbounds the ball to ${target.fullName}, but it is stolen by "
            } else {
                "${passer.fullName} passes the ball to ${target.fullName}, but it is stolen by "
            }
            if (r.nextBoolean()) {
                // ball is stolen by defender of target
                playAsString += "${targetDefender.fullName}!"
                playerWithBall = targetPos
                foul = Foul(
                    homeTeamHasBall,
                    timeRemaining,
                    shotClock,
                    homeTeam,
                    awayTeam,
                    playerWithBall,
                    location,
                    FoulType.ON_BALL
                )
                if (foul.foulType == FoulType.CLEAN) {
                    target.turnovers++
                }
            } else {
                // ball is stolen by defender of passer
                playAsString += "${passDefender.fullName}!"
                foul = Foul(
                    homeTeamHasBall,
                    timeRemaining,
                    shotClock,
                    homeTeam,
                    awayTeam,
                    playerWithBall,
                    location,
                    FoulType.ON_BALL
                )
                if (foul.foulType == FoulType.CLEAN) {
                    passer.turnovers++
                }
            }
            timeChange = timeUtil.smartTimeChange(4 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)

            if (foul.foulType == FoulType.CLEAN) {
                offense.turnovers++
                homeTeamHasBall = !homeTeamHasBall
            } else {
                playAsString += " But ${foul.playAsString}"
            }
        } else {
            // no pass takes place, just run off some time
            foul = if (r.nextBoolean()) {
                Foul(
                    homeTeamHasBall,
                    timeRemaining,
                    shotClock,
                    homeTeam,
                    awayTeam,
                    playerWithBall,
                    location,
                    FoulType.OFF_BALL
                )
            } else {
                Foul(
                    homeTeamHasBall,
                    timeRemaining,
                    shotClock,
                    homeTeam,
                    awayTeam,
                    playerWithBall,
                    location,
                    FoulType.ON_BALL
                )
            }

            if (foul.foulType != FoulType.CLEAN) {
                playAsString = foul.playAsString
                playerStartsWithBall = foul.positionOfPlayerFouled
                type = Plays.FOUL
            } else {
                playAsString = "${passer.fullName} is dribbling the ball."
                type = Plays.DRIBBLE
            }

            timeChange = timeUtil.smartTimeChange(4 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
            location = 1
        }

        timeRemaining -= timeChange
        shotClock -= timeChange
        return 0
    }
}