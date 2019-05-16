package com.appdev.jphil.basketball.game

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.game.extensions.*
import com.appdev.jphil.basketball.plays.*
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.plays.utils.PassingUtils
import com.appdev.jphil.basketball.playtext.*
import com.appdev.jphil.basketball.textcontracts.*
import kotlin.random.Random

class Game(
    val homeTeam: Team,
    val awayTeam: Team,
    val isNeutralCourt: Boolean,
    val season: Int,
    val id: Int? = null,
    val tournamentId: Int? = null,
    var isFinal: Boolean = false
) {
    var miscText: MiscTextContract = MiscPlayText()
    var fastBreakText: FastBreakTextContract = FastBreakPlayText()
    var foulText: FoulTextContract = FoulPlayText()
    var freeThrowText: FreeThrowTextContract = FTPlayText()
    var passText: PassTextContract = PassPlayText()
    var pressText: PressTextContract = PressPlayText()
    var reboundText: ReboundTextContract = ReboundPlayText()
    var shotText: ShotTextContract = ShotPlayText()
    var tipOffText: TipOffTextContract = TipOffPlayText()
    var postMoveText: PostMoveTextContract = PostMoveText()

    var shotClock = 0
    var timeRemaining = 0
    var lastTimeRemaining = 0
    var half = 1
    var homeScore = 0
    var awayScore = 0
    var homeFouls = 0
    var awayFouls = 0
    var homeTimeouts = maxTimeouts
    var awayTimeouts = maxTimeouts

    var mediaTimeOuts = MutableList(10) {false}
    var homeTeamHasBall = true
    var deadball = false
    var madeShot = false
    var shootFreeThrows = false
    var numberOfFreeThrows = 0
    var playerWithBall = 1
    var location = 0
    var possessions = 0
    var inProgress = false
    var consecutivePresses = 1
    var timeInBackcourt = 0
    var homeTeamHasPossessionArrow = false
    var userIsCoaching = false
    var lastPassWasGreat = false
    var lastPlayerWithBall = 0

    val gamePlays = mutableListOf<BasketballPlay>()
    val passingUtils = PassingUtils(homeTeam, awayTeam, 100)

    fun getAsString(): String{
        return "Half:$half \t ${homeTeam.name}:$homeScore - ${awayTeam.name}:$awayScore"
    }

    fun simulateFullGame(){
        setupGame()

        while(half < 3 || homeScore == awayScore){
            startHalf()

            while(timeRemaining > 0) {
                simPlay()
            }
            half++
        }
        half--
        finishGame()
    }

    fun setupGame() {
        homeTeam.startGame()
        awayTeam.startGame()
        inProgress = true
    }

    fun resumeGame() {
        homeTeam.resumeGame()
        awayTeam.resumeGame()
    }

    fun startHalf() {
        updateTimePlayed(false, false)
        timeRemaining = if (half < 3) lengthOfHalf else lengthOfOvertime
        lastTimeRemaining = timeRemaining
        shotClock = lengthOfShotClock
        location = 0

        if (half != 2) {
            // Tip off starts game and all overtime periods
            val tipOff = newTipOff()
            homeTeamHasBall = tipOff.homeTeamHasBall
            homeTeamHasPossessionArrow = !tipOff.homeTeamHasBall
            playerWithBall = tipOff.playerWithBall
            gamePlays.add(tipOff)
            deadball = false
            madeShot = false
            if (half != 1) {
                if (gamePlays.isNotEmpty()) {
                    gamePlays.last().playAsString += miscText.endOfHalf(half - 1, false)
                }
                homeTimeouts++
                awayTimeouts++
            }
        } else {
            homeTeamHasBall = homeTeamHasPossessionArrow
            deadball = true
            madeShot = false
            if (gamePlays.isNotEmpty()) {
                gamePlays.last().playAsString += miscText.endOfHalf(1, false)
            }

            if (homeTimeouts == maxTimeouts) {
                homeTimeouts--
            }
            if (awayTimeouts == maxTimeouts) {
                awayTimeouts--
            }
        }

        if(half < 3){
            homeFouls = 0
            awayFouls = 0
        }

        if(half == 2){
            // half time
            updateTimePlayed(false, true)
        }
        else{
            // coach talk before game or between overtime periods
            runTimeout()
            deadball = false
            madeShot = false
        }
    }

    fun simPlay() {
        gamePlays.addAll(getNextPlay())
        val lastPlay = gamePlays.last()
        lastPlayerWithBall = if (lastPlay is Pass) lastPlay.playerStartsWithBall else playerWithBall
        var mediaTimeoutCalled = false
        if(deadball && !madeShot){
            if(getMediaTimeout()) {
                gamePlays.last().playAsString += miscText.mediaTimeOut()
                runTimeout()
                mediaTimeoutCalled = true
            }

            makeSubs()
        }
        if (!mediaTimeoutCalled) {
            if ((homeTeamHasBall || deadball) && homeTeam.coachWantsTimeout(homeScore - awayScore) && homeTimeouts > 0) {
                gamePlays.last().playAsString += miscText.timeOut(homeTeam, getCoachExtendsToMedia())
                homeTimeouts--
                runTimeout()
            } else if ((!homeTeamHasBall || deadball) && awayTeam.coachWantsTimeout(awayScore - homeScore) && awayTimeouts > 0) {
                gamePlays.last().playAsString += miscText.timeOut(awayTeam, getCoachExtendsToMedia())
                awayTimeouts--
                runTimeout()
            }
        }
    }

    fun finishGame() {
        homeTeam.endGame()
        awayTeam.endGame()

        isFinal = true
        inProgress = false

        gamePlays.last().playAsString += miscText.endOfHalf(half, true)
    }

    private fun getNextPlay(): MutableList<BasketballPlay>{
        val plays: MutableList<BasketballPlay> = if(shootFreeThrows){
            getFreeThrows()
        }
        else{
            getGamePlay()
        }

        val play = plays.last()
        plays.forEach { p -> if (p is Press) addFatigueFromPress() }

        lastPassWasGreat = if (play is Pass) play.isGreatPass else false

        if (play is Rebound) {
            timeRemaining = plays[plays.size - 2].timeRemaining
            shotClock = if(timeRemaining >= lengthOfShotClock) lengthOfShotClock else timeRemaining
        }
        else {
            timeRemaining = play.timeRemaining
            shotClock = play.shotClock
        }

        location = play.location
        playerWithBall = play.playerWithBall

        if(play.homeTeamHasBall != this.homeTeamHasBall){
            changePossession()
        }

        manageFouls(play.foul)

        if(shotClock == 0 && timeRemaining > 0){
            play.playAsString += if (homeTeamHasBall) {
                miscText.shotClockViolation(homeTeam)
            } else {
                miscText.shotClockViolation(awayTeam)
            }
            handleTurnover()
        } else if (timeInBackcourt >= 10 && location == -1) {
            val overshoot = timeInBackcourt - 10
            timeRemaining += overshoot
            shotClock += overshoot
            play.playAsString += if (homeTeamHasBall) {
                miscText.tenSecondViolation(homeTeam)
            } else {
                miscText.tenSecondViolation(awayTeam)
            }
            handleTurnover()
        }

        return plays
    }

    private fun handleTurnover() {
        if(homeTeamHasBall){
            homeTeam.turnovers++
        }
        else{
            awayTeam.turnovers++
        }
        changePossession()
    }

    private fun getFreeThrows(): MutableList<BasketballPlay>{
        shootFreeThrows = false
        val plays = mutableListOf<BasketballPlay>()
        val freeThrows = newFreeThrow()
        addPoints(freeThrows.points)
        plays.add(freeThrows)

        if(!freeThrows.madeLastShot){
            plays.add(newRebound(freeThrows))
            deadball = false
        }

        return plays
    }

    private fun getGamePlay(): MutableList<BasketballPlay>{
        return when {
            gamePlays.size != 0 && gamePlays[gamePlays.size - 1].leadToFastBreak -> getFastBreak()
            location == -1 -> getBackcourtPlay()
            else -> getFrontcourtPlay()
        }
    }

    private fun getFastBreak(): MutableList<BasketballPlay> {
        val play = newFastBreak()
        return if (play.points == 0) {
            mutableListOf(play, newRebound(play))
        } else {
            madeShot = true
            deadball = true
            if(homeTeamHasBall){
                homeScore += play.points
            }
            else{
                awayScore += play.points
            }
            mutableListOf(play)
        }
    }

    private fun getBackcourtPlay(): MutableList<BasketballPlay> {
        val liveBallModifier = when {
            deadball -> 0
            consecutivePresses != 1 -> 0
            else -> 50
        }
        val press: Boolean = if (homeTeamHasBall) {
            Random.nextInt(100) < awayTeam.pressFrequency - liveBallModifier
        } else {
            Random.nextInt(100) < homeTeam.pressFrequency - liveBallModifier
        }

        val backcourtPlays: MutableList<BasketballPlay> = if (press) {
            mutableListOf(newPress())
        } else {
            mutableListOf(newPass())
        }

        if (backcourtPlays.last().type != Plays.FOUL) {
            deadball = false
        }

        timeInBackcourt += timeRemaining - backcourtPlays[0].timeRemaining

        return backcourtPlays
    }

    private fun getFrontcourtPlay(): MutableList<BasketballPlay> {
        consecutivePresses = 1
        val shotUrgency: Int = if(homeTeamHasBall) lengthOfHalf / homeTeam.pace else lengthOfHalf / awayTeam.pace
        val plays = mutableListOf<BasketballPlay>()

        madeShot = false
        if (((shotClock < (lengthOfShotClock - shotUrgency) || Random.nextDouble() > 0.7) && location == 1) || (shotClock <= 5 && Random.nextDouble() > 0.05) || lastPassWasGreat) {
            plays.add(newPostMoveOrShot())
            val shot = plays[0]
            if ( shot.points == 0 && shot.foul.foulType == FoulType.CLEAN) {
                // missed shot need to get a rebound
                plays.add(newRebound(shot))
                deadball = false
            } else if (shot.foul.foulType != FoulType.CLEAN) {
                shootFreeThrows = true
                if (shot.points != 0) {
                    addPoints(shot.points)
                    numberOfFreeThrows = 1
                }
                else if (shot.foul.foulType == FoulType.SHOOTING_LONG) {
                    numberOfFreeThrows = 3
                }
                else {
                    numberOfFreeThrows = 2
                }

                deadball = true
            } else {
                // made shot
                madeShot = true
                deadball = true
                if (homeTeamHasBall) {
                    homeScore += shot.points
                }
                else {
                    awayScore += shot.points
                }
            }
        }
        else {
            plays.add(newPass())
            deadball = false
        }

        return plays
    }

    private fun manageFouls(foul: Foul){
        if (foul.foulType != FoulType.CLEAN) {
            deadball = true
            madeShot = false // allow media timeouts to be called
            timeInBackcourt = 0 // reset time for 10 second call
            lastPassWasGreat = false

            if (foul.isOnDefense) {
                if (foul.homeTeamHasBall) {
                    awayFouls++
                }
                else {
                    homeFouls++
                }
            }
            else {
                if (foul.homeTeamHasBall) {
                    homeFouls++
                }
                else {
                    awayFouls++
                }
            }

            if (foul.isOnDefense) {
                if (foul.homeTeamHasBall && awayFouls > 6) {
                    shootFreeThrows = true
                    numberOfFreeThrows = if (awayFouls >= 10) {
                        2
                    }
                    else {
                        -1
                    }
                }
                else if (homeFouls > 6) {
                    shootFreeThrows = true
                    numberOfFreeThrows = if (homeFouls >= 10) {
                        2
                    }
                    else {
                        -1
                    }
                }

                if (shotClock < resetShotClockTime) {
                    shotClock = if(timeRemaining > resetShotClockTime) resetShotClockTime else timeRemaining
                }
                if (shootFreeThrows) {
                    shotClock = lengthOfShotClock
                }
            }
            else {
                if (homeTeamHasBall && foul.homeTeamHasBall || !homeTeamHasBall &&!foul.homeTeamHasBall) {
                    changePossession()
                }
            }
        }
    }

    private fun addPoints(points: Int){
        if (homeTeamHasBall) {
            homeScore += points
        }
        else {
            awayScore += points
        }
    }

    private fun runTimeout() {
        homeTeam.coachTalk(!isNeutralCourt, homeScore - awayScore, CoachTalk.NEUTRAL)
        awayTeam.coachTalk(false, awayScore - homeScore, CoachTalk.NEUTRAL)

        updateTimePlayed(true, false)

        homeTeam.userWantsTimeout = false
        awayTeam.userWantsTimeout = false

        homeTeam.lastScoreDiff = homeScore - awayScore
        awayTeam.lastScoreDiff = awayScore - homeScore

        deadball = true
        madeShot = false

        makeSubs()
    }

    private fun changePossession() {
        updateTimePlayed(false, false)
        lastPassWasGreat = false
        timeInBackcourt = 0
        shotClock = if (timeRemaining >= lengthOfShotClock) lengthOfShotClock else timeRemaining
        homeTeamHasBall = !homeTeamHasBall
        if (location != 0) {
            location = -location
        }
        possessions++
    }

    private fun updateTimePlayed(isTimeout: Boolean, isHalftime: Boolean) {
        val time = lastTimeRemaining - timeRemaining
        homeTeam.updateTimePlayed(time, isTimeout, isHalftime)
        awayTeam.updateTimePlayed(time, isTimeout, isHalftime)
        lastTimeRemaining = timeRemaining
    }

    private fun addFatigueFromPress() {
        homeTeam.addFatigueFromPress()
        awayTeam.addFatigueFromPress()
    }

    fun pauseGame() {
        homeTeam.pauseGame()
        awayTeam.pauseGame()
    }

    private companion object {
        const val lengthOfHalf = 20 * 60 // 20 minutes
        const val lengthOfOvertime = 5 * 60 // 5 minutes
        const val lengthOfShotClock = 30 // 30 seconds
        const val resetShotClockTime = 20 // shot clock resets to 20 on a defensive foul
        const val maxTimeouts = 4
    }
}