package com.appdev.jphil.basketball.game

import com.appdev.jphil.basketball.game.extensions.makeSubs
import com.appdev.jphil.basketball.game.helpers.HalfTimeHelper
import com.appdev.jphil.basketball.game.helpers.PlayGenerator
import com.appdev.jphil.basketball.game.helpers.TimeoutHelper
import com.appdev.jphil.basketball.plays.BasketballPlay
import com.appdev.jphil.basketball.plays.EndOfHalf
import com.appdev.jphil.basketball.plays.Pass
import com.appdev.jphil.basketball.plays.enums.FreeThrowTypes
import com.appdev.jphil.basketball.plays.utils.PassingUtils
import com.appdev.jphil.basketball.playtext.FTPlayText
import com.appdev.jphil.basketball.playtext.FastBreakPlayText
import com.appdev.jphil.basketball.playtext.FoulPlayText
import com.appdev.jphil.basketball.playtext.MiscPlayText
import com.appdev.jphil.basketball.playtext.PassPlayText
import com.appdev.jphil.basketball.playtext.PostMoveText
import com.appdev.jphil.basketball.playtext.PressPlayText
import com.appdev.jphil.basketball.playtext.ReboundPlayText
import com.appdev.jphil.basketball.playtext.ShotPlayText
import com.appdev.jphil.basketball.playtext.TipOffPlayText
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.textcontracts.FastBreakTextContract
import com.appdev.jphil.basketball.textcontracts.FoulTextContract
import com.appdev.jphil.basketball.textcontracts.FreeThrowTextContract
import com.appdev.jphil.basketball.textcontracts.MiscTextContract
import com.appdev.jphil.basketball.textcontracts.PassTextContract
import com.appdev.jphil.basketball.textcontracts.PostMoveTextContract
import com.appdev.jphil.basketball.textcontracts.PressTextContract
import com.appdev.jphil.basketball.textcontracts.ReboundTextContract
import com.appdev.jphil.basketball.textcontracts.ShotTextContract
import com.appdev.jphil.basketball.textcontracts.TipOffTextContract

class Game(
    val homeTeam: Team,
    val awayTeam: Team,
    val isNeutralCourt: Boolean,
    val season: Int,
    val isConferenceGame: Boolean,
    var id: Int? = null,
    val tournamentId: Int? = null,
    var isFinal: Boolean = false,
    val scoreline: Scoreline = Scoreline()
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

    var mediaTimeOuts = MutableList(10) { false }
    var homeTeamHasBall = true
    var deadball = false
    var madeShot = false
    var shootFreeThrows = false
    var freeThrowType = FreeThrowTypes.ONE_SHOT
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

    fun simulateFullGame() {
        setupGame()

        while (half < 3 || homeScore == awayScore) {
            HalfTimeHelper.startHalf(this)

            while (timeRemaining > 0) {
                simPlay()
                if (TimeoutHelper.isTimeoutCalled(this)) {
                    TimeoutHelper.runTimeout(this)
                }
            }
            scoreline.addScores(homeScore, awayScore)
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

    fun simPlay() {
        if (deadball) {
            updateStrategy()
            if (!madeShot) {
                makeSubs()
            }
        }

        gamePlays.addAll(PlayGenerator.getNextPlay(this))
        val lastPlay = gamePlays.last()
        lastPlayerWithBall = if (lastPlay is Pass) lastPlay.playerStartsWithBall else playerWithBall
    }

    fun finishGame() {
        homeTeam.endGame()
        awayTeam.endGame()

        isFinal = true
        inProgress = false

        gamePlays.add(EndOfHalf(this, true, homeTeamHasBall))
    }

    fun changePossession() {
        updateTimePlayed(false, false)
        lastPassWasGreat = false
        timeInBackcourt = 0
        resetShotClock()
        homeTeamHasBall = !homeTeamHasBall
        if (location != 0) {
            location = -location
        }
        if (shootFreeThrows) {
            location = 1
        }
        possessions++

        updateStrategy()
    }

    fun resetShotClock() {
        shotClock = if (timeRemaining >= lengthOfShotClock) lengthOfShotClock else timeRemaining
    }

    private fun updateStrategy() {
        if (userIsCoaching) {
            if (homeTeam.isUser) {
                homeTeam.updateStrategy(0, 0, -1, 0)
                awayTeam.updateStrategy(awayScore, homeScore, half, timeRemaining)
            } else {
                awayTeam.updateStrategy(0, 0, -1, 0)
                homeTeam.updateStrategy(homeScore, awayScore, half, timeRemaining)
            }
        } else {
            homeTeam.updateStrategy(homeScore, awayScore, half, timeRemaining)
            awayTeam.updateStrategy(awayScore, homeScore, half, timeRemaining)
        }
    }

    fun updateTimePlayed(isTimeout: Boolean, isHalftime: Boolean) {
        val time = lastTimeRemaining - timeRemaining
        homeTeam.updateTimePlayed(time, isTimeout, isHalftime)
        awayTeam.updateTimePlayed(time, isTimeout, isHalftime)
        lastTimeRemaining = timeRemaining
    }

    fun updateTimeRemaining(play: BasketballPlay) {
        timeRemaining = play.timeRemaining
        shotClock = play.shotClock
    }

    fun pauseGame() {
        homeTeam.pauseGame()
        awayTeam.pauseGame()
    }

    companion object {
        const val lengthOfHalf = 20 * 60 // 20 minutes
        const val lengthOfOvertime = 5 * 60 // 5 minutes
        const val lengthOfShotClock = 30 // 30 seconds
        const val resetShotClockTime = 20 // shot clock resets to 20 on a defensive foul TODO: also reset for offensive rebound
        const val maxTimeouts = 4
    }
}
