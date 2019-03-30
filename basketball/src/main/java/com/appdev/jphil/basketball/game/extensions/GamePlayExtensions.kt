package com.appdev.jphil.basketball.game.extensions

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.*

fun Game.newShot(isRushed: Boolean): Shot {
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
        isRushed,
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