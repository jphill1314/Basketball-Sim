package com.appdev.jphil.basketball.game

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.plays.*
import com.appdev.jphil.basketball.plays.enums.FoulType
import com.appdev.jphil.basketball.plays.enums.Plays
import com.appdev.jphil.basketball.plays.utils.PassingUtils
import com.appdev.jphil.basketball.playtext.MiscPlayText
import com.appdev.jphil.basketball.textcontracts.MiscTextContract
import java.util.*

class Game(
    val homeTeam: Team,
    val awayTeam: Team,
    val isNeutralCourt: Boolean,
    val season: Int,
    val id: Int? = null,
    var isFinal: Boolean = false
) {
    var miscText: MiscTextContract = MiscPlayText()

    var shotClock = 0
    var timeRemaining = 0
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

    val gamePlays = mutableListOf<BasketballPlay>()

    private val r = Random()
    private val passingUtils =
        PassingUtils(homeTeam, awayTeam, BasketballPlay.randomBound)

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
        timeRemaining = if (half < 3) lengthOfHalf else lengthOfOvertime
        shotClock = lengthOfShotClock
        location = 0

        if (half != 2) {
            // Tip off starts game and all overtime periods
            val tipOff = TipOff(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location)
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
            updateTimePlayed(0, false, true)
        }
        else{
            // coach talk before game or between overtime periods
            runTimeout()
        }
    }

    fun simPlay() {
        gamePlays.addAll(getNextPlay())
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

    private fun makeSubs() {
        if(shootFreeThrows){
            if(homeTeamHasBall){
                if (userIsCoaching) {
                    if (homeTeam.isUser) {
                        awayTeam.aiMakeSubs(-1, half, timeRemaining)
                    } else {
                        homeTeam.aiMakeSubs(playerWithBall - 1, half, timeRemaining)
                    }
                } else {
                    homeTeam.aiMakeSubs(playerWithBall - 1, half, timeRemaining)
                    awayTeam.aiMakeSubs(-1, half, timeRemaining)
                }
            }
            else{
                if (userIsCoaching) {
                    if (homeTeam.isUser) {
                        awayTeam.aiMakeSubs(playerWithBall - 1, half, timeRemaining)
                    } else {
                        homeTeam.aiMakeSubs(-1, half, timeRemaining)
                    }
                } else {
                    homeTeam.aiMakeSubs(-1, half, timeRemaining)
                    awayTeam.aiMakeSubs(playerWithBall - 1, half, timeRemaining)
                }
            }
        }
        else {
            if (userIsCoaching) {
                if (homeTeam.isUser) {
                    awayTeam.aiMakeSubs(-1, half, timeRemaining)
                } else {
                    homeTeam.aiMakeSubs(-1, half, timeRemaining)
                }
            } else {
                homeTeam.aiMakeSubs(-1, half, timeRemaining)
                awayTeam.aiMakeSubs(-1, half, timeRemaining)
            }
        }
    }

    fun makeUserSubsIfPossible() {
        if (deadball && !madeShot) {
            if (shootFreeThrows) {
                if (homeTeamHasBall) {
                    if (homeTeam.isUser) {
                        homeTeam.makeUserSubs(playerWithBall)
                    } else {
                        awayTeam.makeUserSubs(-1)
                    }
                } else {
                    if (homeTeam.isUser) {
                        homeTeam.makeUserSubs(-1)
                    } else {
                        awayTeam.makeUserSubs(playerWithBall)
                    }
                }
            } else {
                if (homeTeam.isUser) {
                    homeTeam.makeUserSubs(-1)
                } else {
                    awayTeam.makeUserSubs(-1)
                }
            }
        }
    }

    fun finishGame() {
        //println("Final! Half: $half Home: $homeScore  Away: $awayScore")

        homeTeam.endGame()
        awayTeam.endGame()

        if(half > 4){
            println("wtf: $homeScore - $awayScore half:$half")
        }
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
        updateTimePlayed(timeRemaining - play.timeRemaining, false, false)
        plays.forEach { p -> if (p is Press) addFatigueFromPress() }

        if(play is Rebound){
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
        val freeThrows = FreeThrows(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, numberOfFreeThrows)
        addPoints(freeThrows.points)
        plays.add(freeThrows)

        if(!freeThrows.madeLastShot){
            plays.add(Rebound(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location))
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
        val play = FastBreak(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location)
        return if (play.points == 0) {
            mutableListOf(play, Rebound(homeTeamHasBall, play.timeRemaining, play.shotClock, homeTeam, awayTeam, playerWithBall, location))
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
            r.nextInt(100) < awayTeam.pressFrequency - liveBallModifier
        } else {
            r.nextInt(100) < homeTeam.pressFrequency - liveBallModifier
        }

        val backcourtPlays: MutableList<BasketballPlay> = if (press) {
            mutableListOf(Press(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, deadball, passingUtils, consecutivePresses++))
        } else {
            mutableListOf(Pass(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, deadball, passingUtils))
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
        if (((shotClock < (lengthOfShotClock - shotUrgency) || r.nextDouble() > 0.7) && location == 1) || (shotClock <= 5 && r.nextDouble() > 0.05)) {
            plays.add(Shot(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, false, deadball))
            val shot = plays[0]
            if ( shot.points == 0 && shot.foul.foulType == FoulType.CLEAN) {
                // missed shot need to get a rebound
                plays.add(Rebound(homeTeamHasBall, shot.timeRemaining, shot.shotClock, homeTeam, awayTeam, playerWithBall, location))
                deadball = false
            }
            else if (shot.foul.foulType != FoulType.CLEAN) {
                // shoot free throws now?
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
            }
            else {
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
            plays.add(Pass(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, deadball, passingUtils))
            deadball = false
        }

        return plays
    }

    private fun manageFouls(foul: Foul){
        if (foul.foulType != FoulType.CLEAN) {
            deadball = true
            madeShot = false // allow media timeouts to be called
            timeInBackcourt = 0 // reset time for 10 second call

            if (foul.isOnDefense) {
                if (homeTeamHasBall) {
                    awayFouls++
                }
                else {
                    homeFouls++
                }
            }
            else {
                if (homeTeamHasBall) {
                    homeFouls++
                }
                else {
                    awayFouls++
                }
            }

            if (foul.isOnDefense) {
                if (homeTeamHasBall && awayFouls > 6) {
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
                changePossession()
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

    private fun getMediaTimeout(): Boolean{
        return if (half == 1) {
            if (timeRemaining < 16 * 60 && timeRemaining > 12 * 60 && !mediaTimeOuts[0]) {
                mediaTimeOuts[0]= true
                true
            }
            else if (timeRemaining < 12 * 60 && timeRemaining > 8 * 60 && !mediaTimeOuts[1]) {
                mediaTimeOuts[1] = true
                true
            }
            else if (timeRemaining < 8 * 60 && timeRemaining > 4 * 60 && !mediaTimeOuts[2]) {
                mediaTimeOuts[2] = true
                true
            }
            else if (timeRemaining < 4 * 60 && !mediaTimeOuts[3]) {
                mediaTimeOuts[3] = true
                true
            }
            else {
                false
            }
        }
        else if (half == 2) {
            if (timeRemaining < 16 * 60 && timeRemaining > 12 * 60 && !mediaTimeOuts[4]) {
                mediaTimeOuts[4] = true
                true
            }
            else if (timeRemaining < 12 * 60 && timeRemaining > 8 * 60 && !mediaTimeOuts[5]) {
                mediaTimeOuts[5] = true
                true
            }
            else if (timeRemaining < 8 * 60 && timeRemaining > 4 * 60 && !mediaTimeOuts[6]) {
                mediaTimeOuts[6] = true
                true
            }
            else if (timeRemaining < 4 * 60 && !mediaTimeOuts[7]) {
                mediaTimeOuts[7] = true
                true
            }
            else {
                false
            }
        }
        else {
            false
        }
    }

    private fun getCoachExtendsToMedia(): Boolean {
        return if (half == 1) {
            if (timeRemaining < 16.5 * 60 && timeRemaining > 12 * 60 && !mediaTimeOuts[0]) {
                mediaTimeOuts[0]= true
                true
            }
            else if (timeRemaining < 12.5 * 60 && timeRemaining > 8 * 60 && !mediaTimeOuts[1]) {
                mediaTimeOuts[1] = true
                true
            }
            else if (timeRemaining < 8.5 * 60 && timeRemaining > 4 * 60 && !mediaTimeOuts[2]) {
                mediaTimeOuts[2] = true
                true
            }
            else if (timeRemaining < 4.5 * 60 && !mediaTimeOuts[3]) {
                mediaTimeOuts[3] = true
                true
            }
            else {
                false
            }
        }
        else if (half == 2) {
            if (timeRemaining < 16.5 * 60 && timeRemaining > 12 * 60 && !mediaTimeOuts[4]) {
                mediaTimeOuts[4] = true
                true
            }
            else if (timeRemaining < 12.5 * 60 && timeRemaining > 8 * 60 && !mediaTimeOuts[5]) {
                mediaTimeOuts[5] = true
                true
            }
            else if (timeRemaining < 8.5 * 60 && timeRemaining > 4 * 60 && !mediaTimeOuts[6]) {
                mediaTimeOuts[6] = true
                true
            }
            else if (timeRemaining < 4.5 * 60 && !mediaTimeOuts[7]) {
                mediaTimeOuts[7] = true
                true
            }
            else {
                false
            }
        }
        else {
            false
        }
    }

    private fun runTimeout() {
        homeTeam.coachTalk(!isNeutralCourt, homeScore - awayScore, CoachTalk.NEUTRAL)
        awayTeam.coachTalk(false, awayScore - homeScore, CoachTalk.NEUTRAL)

        updateTimePlayed(0, true, false)

        homeTeam.userWantsTimeout = false
        awayTeam.userWantsTimeout = false

        homeTeam.lastScoreDiff = homeScore - awayScore
        awayTeam.lastScoreDiff = awayScore - homeScore

        deadball = true
        madeShot = false

        makeSubs()
    }

    private fun changePossession() {
        timeInBackcourt = 0
        shotClock = if (timeRemaining >= lengthOfShotClock) lengthOfShotClock else timeRemaining
        homeTeamHasBall = !homeTeamHasBall
        if (location != 0) {
            location = -location
        }
        possessions++
    }

    private fun updateTimePlayed(time: Int, isTimeout: Boolean, isHalftime: Boolean) {
        homeTeam.updateTimePlayed(time, isTimeout, isHalftime)
        awayTeam.updateTimePlayed(time, isTimeout, isHalftime)
    }

    private fun addFatigueFromPress() {
        homeTeam.addFatigueFromPress()
        awayTeam.addFatigueFromPress()
    }

    fun getTimeAsString(): String {
        val min = timeRemaining / 60
        val sec = timeRemaining - min * 60
        return if (sec > 9) "$min:$sec ($shotClock)" else "$min:0$sec ($shotClock)"
    }

    fun pauseGame() {
        homeTeam.pauseGame()
        awayTeam.pauseGame()
    }

    fun getTeamStats(): List<TeamStatLine> {
        val stats = mutableListOf<TeamStatLine>()
        stats.add(TeamStatLine(homeTeam.name, awayTeam.name, ""))
        stats.add(
            TeamStatLine(
                "${homeTeam.twoPointMakes}/${homeTeam.twoPointAttempts}",
                "${awayTeam.twoPointMakes}/${awayTeam.twoPointAttempts}",
                "2FGs"
            )
        )
        stats.add(
            TeamStatLine(
                "${homeTeam.threePointMakes}/${homeTeam.threePointAttempts}",
                "${awayTeam.threePointMakes}/${awayTeam.threePointAttempts}",
                "3FGs"
            )
        )
        stats.add(
            TeamStatLine(
                "${homeTeam.freeThrowMakes}/${homeTeam.freeThrowShots}",
                "${awayTeam.freeThrowMakes}/${awayTeam.freeThrowShots}",
                "FTs"
            )
        )
        stats.add(
            TeamStatLine(
                "${homeTeam.offensiveRebounds} - ${homeTeam.defensiveFouls}",
                "${awayTeam.offensiveRebounds} - ${awayTeam.defensiveRebounds}",
                "Rebounds"
            )
        )
        stats.add(
            TeamStatLine(
                "${homeTeam.turnovers}",
                "${awayTeam.turnovers}",
                "Turnovers"
            )
        )
        stats.add(
            TeamStatLine(
                "${homeTeam.offensiveFouls} - ${homeTeam.defensiveFouls}",
                "${awayTeam.offensiveFouls} - ${awayTeam.defensiveFouls}",
                "Fouls"
            )
        )
        return stats
    }

    private companion object {
        const val lengthOfHalf = 20 * 60 // 20 minutes
        const val lengthOfOvertime = 5 * 60 // 5 minutes
        const val lengthOfShotClock = 30 // 30 seconds
        const val resetShotClockTime = 20 // shot clock resets to 20 on a defensive foul
        const val maxTimeouts = 4
    }
}