package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.plays.utils.PassingUtils
import com.appdev.jphil.basketball.textcontracts.FoulTextContract
import com.appdev.jphil.basketball.textcontracts.PassTextContract


class Pass(
    homeTeamHasBall: Boolean,
    timeRemaining: Int,
    shotClock: Int,
    homeTeam: Team,
    awayTeam: Team,
    playerWithBall: Int,
    location: Int,
    foulText: FoulTextContract,
    private val deadBall: Boolean,
    private val passingUtils: PassingUtils,
    private val playText: PassTextContract
) : BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, foulText) {

    var playerStartsWithBall = playerWithBall
    private lateinit var passer: Player
    private lateinit var passDefender: Player
    private lateinit var target: Player
    private lateinit var targetDefender: Player
    private var targetPos = -1

    private var timeChange = 0

    var isGreatPass = false

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
            foulText,
            FoulType.CLEAN
        )
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        setPasserAndTarget()

        val passSuccess = (passer.passing + target.offBallMovement) / (r.nextInt(randomBound / 2) + 1)
        val stealSuccess =
            ((passDefender.onBallDefense + passDefender.stealing + targetDefender.offBallDefense + targetDefender.stealing) / 2) /
                    (r.nextInt(randomBound) + 1)

        if (passSuccess >= ((defense.aggression + passDefender.aggressiveness + targetDefender.aggressiveness) / 15) || location == -1) {
            successfulPass(passSuccess)
        } else if (passSuccess < (((defense.aggression + passDefender.aggressiveness + targetDefender.aggressiveness) / 15)) - 6) {
            badPass()
        } else if (stealSuccess > (100 - defense.aggression - ((passDefender.aggressiveness + targetDefender.aggressiveness) / 2))) {
            stolenPass()
        } else {
            justDribbling()
        }

        if (foul.foulType == FoulType.CLEAN && defense.intentionallyFoul) {
            foul = Foul(
                homeTeamHasBall,
                timeRemaining,
                shotClock,
                homeTeam,
                awayTeam,
                playerWithBall,
                location,
                foulText,
                FoulType.INTENTIONAL
            )
            type = Plays.FOUL
            playAsString += "\n${foul.playAsString}"
        }

        timeRemaining -= timeChange
        shotClock -= timeChange
        return 0
    }

    private fun setPasserAndTarget() {
        if (deadBall) {
            playerWithBall = passingUtils.getInbounder(homeTeamHasBall)
            playerStartsWithBall = playerWithBall
        }

        passer = offense.getPlayerAtPosition(playerWithBall)
        passDefender = defense.getPlayerAtPosition(playerWithBall)

        targetPos = passingUtils.getTarget(homeTeamHasBall, playerWithBall)
        target = offense.getPlayerAtPosition(targetPos)
        targetDefender = defense.getPlayerAtPosition(targetPos)
    }

    private fun successfulPass(passSuccess: Int) {
        //TODO: add chance to have the ball knocked out of bounds
        playerWithBall = targetPos
        timeChange = if (location < 1) {
            // the ball was inbounded in the backcourt so more time needs to come off the clock
            playAsString = if (deadBall) {
                playText.successfulInboundBackcourt(passer, target)
            } else {
                playText.successfulPassBackcourt(passer, target)
            }
            location = 1
            timeUtil.smartTimeChange(9 - ((offense.pace / 90.0) * r.nextInt(6)).toInt(), shotClock)
        } else {
            //TODO: add pass leading to a shot / post move / etc
            isGreatPass = passSuccess > 25
            playAsString = if (deadBall) {
                playText.successfulInbound(passer, target)
            } else {
                playText.successfulPass(passer, target)
            }
            timeUtil.smartTimeChange(8 - ((offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
        }
    }

    private fun badPass() {
        if (r.nextInt(100) > 60) {
            // target of pass is at fault
            playerWithBall = targetPos
            target.turnovers++
            targetDefender.steals++
            playAsString = if (deadBall) {
                playText.mishandledInbound(passer, target)
            } else {
                playText.mishandledPass(passer, target)
            }
        } else {
            // passer is at fault
            passer.turnovers++
            passDefender.steals++
            playAsString = if (deadBall) {
                playText.badInbound(passer, target)
            } else {
                playText.badPass(passer, target)
            }
        }
        offense.turnovers++
        timeChange = timeUtil.smartTimeChange(4 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
        homeTeamHasBall = !homeTeamHasBall
    }

    private fun stolenPass() {
        if (r.nextBoolean()) {
            // ball is stolen by defender of target
            playAsString = if (deadBall) {
                playText.stolenInbound(passer, target, targetDefender)
            } else {
                playText.stolenPass(passer, target, targetDefender)
            }
            playerWithBall = targetPos
            foul = Foul(
                homeTeamHasBall,
                timeRemaining,
                shotClock,
                homeTeam,
                awayTeam,
                playerWithBall,
                location,
                foulText,
                FoulType.ON_BALL
            )
            if (foul.foulType == FoulType.CLEAN) {
                target.turnovers++
                targetDefender.steals++
            }
        } else {
            // ball is stolen by defender of passer
            playAsString = if (deadBall) {
                playText.stolenInbound(passer, target, passDefender)
            } else {
                playText.stolenPass(passer, target, passDefender)
            }
            foul = Foul(
                homeTeamHasBall,
                timeRemaining,
                shotClock,
                homeTeam,
                awayTeam,
                playerWithBall,
                location,
                foulText,
                FoulType.ON_BALL
            )
            if (foul.foulType == FoulType.CLEAN) {
                passer.turnovers++
                passDefender.steals++
            }
        }
        timeChange = timeUtil.smartTimeChange(4 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)

        if (foul.foulType == FoulType.CLEAN) {
            offense.turnovers++
            homeTeamHasBall = !homeTeamHasBall
        } else {
            playAsString += " But ${foul.playAsString}"
        }
    }

    private fun justDribbling() {
        foul = if (r.nextBoolean()) {
            Foul(
                homeTeamHasBall,
                timeRemaining,
                shotClock,
                homeTeam,
                awayTeam,
                playerWithBall,
                location,
                foulText,
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
                foulText,
                FoulType.ON_BALL
            )
        }

        if (foul.foulType != FoulType.CLEAN) {
            playAsString = foul.playAsString
            playerStartsWithBall = foul.positionOfPlayerFouled
            type = Plays.FOUL
        } else {
            playAsString = playText.justDribbling(passer)
            type = Plays.DRIBBLE
        }

        timeChange = timeUtil.smartTimeChange(4 - ((offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
        location = 1
    }
}