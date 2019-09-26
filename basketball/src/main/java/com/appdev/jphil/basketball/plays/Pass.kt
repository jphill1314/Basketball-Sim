package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays

class Pass(game: Game) : BasketballPlay(game) {

    private val deadBall = game.deadball
    private val passingUtils = game.passingUtils
    private val playText = game.passText
    private val miscText = game.miscText

    var playerStartsWithBall = playerWithBall
    private lateinit var passer: Player
    private lateinit var passDefender: Player
    private lateinit var target: Player
    private lateinit var targetDefender: Player
    private var targetPos = -1

    private var timeChange = 0
    private val coach = offense.getHeadCoach()

    var isGreatPass = false

    init {
        super.type = Plays.PASS
        foul = Foul(game, FoulType.CLEAN)
        points = generatePlay()
    }

    override fun generatePlay(): Int {
        setPasserAndTarget()

        val passSuccess = (passer.passing + target.offBallMovement) / (r.nextInt(randomBound / 2) + 1)

        val (maxChange, minChange) = when {
            isSuccessfulPass(passSuccess) -> successfulPass(passSuccess)
            isBadPass(passSuccess) -> badPass()
            isStolenPass() -> stolenPass()
            else -> justDribbling() // TODO: increase chance of this when wasting time
        }

        if (foul.foulType == FoulType.CLEAN && defense.intentionallyFoul && timeChange < timeRemaining) {
            foul = Foul(game, FoulType.INTENTIONAL)
            type = Plays.FOUL
            playAsString += "\n${foul.playAsString}"
        }

        timeChange = getTimeChangePaceDependent(maxChange, minChange)
        timeRemaining -= timeChange
        shotClock -= timeChange
        return 0
    }

    private fun isSuccessfulPass(passSuccess: Int): Boolean {
        return passSuccess >= ((defense.aggression + passDefender.aggressiveness + targetDefender.aggressiveness) / 15) || location == -1
    }

    private fun isBadPass(passSuccess: Int): Boolean {
        return passSuccess < (((defense.aggression + passDefender.aggressiveness + targetDefender.aggressiveness) / 15)) - 6
    }

    private fun isStolenPass(): Boolean {
        val stealSuccess = ((passDefender.onBallDefense + passDefender.stealing + targetDefender.offBallDefense + targetDefender.stealing) / 2) / (r.nextInt(randomBound) + 1)
        return stealSuccess > (100 - defense.aggression - ((passDefender.aggressiveness + targetDefender.aggressiveness) / 2))
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

    private fun successfulPass(passSuccess: Int): Pair<Int, Int> {
        //TODO: add chance to have the ball knocked out of bounds
        playerWithBall = targetPos
        if (location < 1) {
            // the ball was inbounded in the backcourt so more time needs to come off the clock
            playAsString = if (deadBall) {
                playText.successfulInboundBackcourt(passer, target)
            } else {
                playText.successfulPassBackcourt(passer, target)
            }
            location = 1
            return when {
                coach.shouldHurry -> Pair(5, 1)
                coach.shouldWasteTime -> Pair(10, 5)
                else -> Pair(9, 3)
            }
        } else {
            //TODO: add pass leading to a shot / post move / etc
            isGreatPass = passSuccess > 25
            playAsString = if (deadBall) {
                playText.successfulInbound(passer, target)
            } else {
                playText.successfulPass(passer, target)
            }
            return when {
                coach.shouldHurry -> Pair(3, 1)
                coach.shouldWasteTime -> Pair(20, 5)
                else -> Pair(8, 4)
            }
        }
    }

    private fun badPass(): Pair<Int, Int> {
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
        homeTeamHasBall = !homeTeamHasBall
        return when {
            coach.shouldHurry -> Pair(3, 1)
            coach.shouldWasteTime -> Pair(20, 5)
            else -> Pair(8, 4)
        }
    }

    private fun stolenPass(): Pair<Int, Int> {
        if (r.nextBoolean()) {
            // ball is stolen by defender of target
            playAsString = if (deadBall) {
                playText.stolenInbound(passer, target, targetDefender)
            } else {
                playText.stolenPass(passer, target, targetDefender)
            }
            playerWithBall = targetPos
            foul = Foul(game, FoulType.ON_BALL)
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
            foul = Foul(game, FoulType.ON_BALL)
            if (foul.foulType == FoulType.CLEAN) {
                passer.turnovers++
                passDefender.steals++
            }
        }

        if (foul.foulType == FoulType.CLEAN) {
            offense.turnovers++
            homeTeamHasBall = !homeTeamHasBall
        } else {
            playAsString += " ${miscText.conjunction(true)}${foul.playAsString}"
        }
        return when {
            coach.shouldHurry -> Pair(3, 1)
            coach.shouldWasteTime -> Pair(20, 5)
            else -> Pair(6, 2)
        }
    }

    private fun justDribbling(): Pair<Int, Int> {
        foul = if (r.nextBoolean()) {
            Foul(game, FoulType.OFF_BALL)
        } else {
            Foul(game, FoulType.ON_BALL)
        }

        if (foul.foulType != FoulType.CLEAN) {
            playAsString = foul.playAsString
            playerStartsWithBall = foul.positionOfPlayerFouled
            type = Plays.FOUL
        } else {
            playAsString = playText.justDribbling(passer)
            type = Plays.DRIBBLE
        }
        location = 1
        return when {
            coach.shouldWasteTime -> Pair(20, 5)
            else -> Pair(4, 1)
        }
    }
}