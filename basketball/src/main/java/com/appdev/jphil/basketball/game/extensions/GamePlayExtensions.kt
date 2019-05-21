package com.appdev.jphil.basketball.game.extensions

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.*
import com.appdev.jphil.basketball.plays.enums.FoulType
import kotlin.random.Random

fun Game.newShot(): Shot {
    val passer = if (homeTeamHasBall) homeTeam.getPlayerAtPosition(lastPlayerWithBall) else awayTeam.getPlayerAtPosition(lastPlayerWithBall)
    return Shot(
        homeTeamHasBall,
        timeRemaining,
        shotClock,
        homeTeam,
        awayTeam,
        playerWithBall,
        location,
        foulText,
        lastPassWasGreat,
        passer,
        shotClock <= 5,
        shotText
    )
}

fun Game.newRebound(shot: BasketballPlay): Rebound {
    return Rebound(
        homeTeamHasBall,
        shot.timeRemaining,
        shot.shotClock,
        homeTeam,
        awayTeam,
        playerWithBall,
        location,
        foulText,
        reboundText
    )
}

fun Game.newPass(): Pass {
    return Pass(
        homeTeamHasBall,
        timeRemaining,
        shotClock,
        homeTeam,
        awayTeam,
        playerWithBall,
        location,
        foulText,
        deadball,
        passingUtils,
        passText
    )
}

fun Game.newPress(): Press {
    return Press(
        homeTeamHasBall,
        timeRemaining,
        shotClock,
        homeTeam,
        awayTeam,
        playerWithBall,
        location,
        foulText,
        deadball,
        passingUtils,
        consecutivePresses++,
        pressText
    )
}

fun Game.newFastBreak(): FastBreak {
    return FastBreak(
        homeTeamHasBall,
        timeRemaining,
        shotClock,
        homeTeam,
        awayTeam,
        playerWithBall,
        location,
        foulText,
        fastBreakText
    )
}

fun Game.newFreeThrow(): FreeThrows {
    return FreeThrows(
        homeTeamHasBall,
        timeRemaining,
        shotClock,
        homeTeam,
        awayTeam,
        playerWithBall,
        location,
        foulText,
        numberOfFreeThrows,
        freeThrowText
    )
}

fun Game.newTipOff(): TipOff {
    return TipOff(
        homeTeamHasBall,
        timeRemaining,
        shotClock,
        homeTeam,
        awayTeam,
        playerWithBall,
        location,
        foulText,
        tipOffText
    )
}

fun Game.newPostMove(): PostMove {
    val passer = if (homeTeamHasBall) homeTeam.getPlayerAtPosition(lastPlayerWithBall) else awayTeam.getPlayerAtPosition(lastPlayerWithBall)
    return PostMove(
            homeTeamHasBall,
            timeRemaining,
            shotClock,
            homeTeam,
            awayTeam,
            playerWithBall,
            location,
            foulText,
            lastPassWasGreat,
            passer,
            postMoveText
        )
}

fun Game.newPostMoveOrShot(): BasketballPlay {
    val shooter = if (homeTeamHasBall) homeTeam.getPlayerAtPosition(playerWithBall) else awayTeam.getPlayerAtPosition(playerWithBall)
    val positionChanceMod = when (shooter.position) {
        4, 5 -> 0
        3 -> 30
        2 -> 40
        else -> 50
    }
    return if (Random.nextInt(100) + positionChanceMod < shooter.postMove) {
        newPostMove()
    } else {
        newShot()
    }
}

fun Game.newIntentionalFoul(): BasketballPlay {
    return IntentionalFoul(
        homeTeamHasBall,
        timeRemaining,
        shotClock,
        homeTeam,
        awayTeam,
        playerWithBall,
        location,
        foulText
    )
}