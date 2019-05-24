package com.appdev.jphil.basketball.game.extensions

import com.appdev.jphil.basketball.game.Game


fun Game.makeSubs() {
    if(shootFreeThrows){
        if(homeTeamHasBall){
            if (userIsCoaching) {
                // Only make subs for non-user team
                if (homeTeam.isUser) {
                    awayTeam.aiMakeSubs(-1, half, timeRemaining)
                } else {
                    homeTeam.aiMakeSubs(playerWithBall - 1, half, timeRemaining)
                }
            } else {
                // make subs for both teams
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

fun Game.makeUserSubsIfPossible() {
    if (deadball && !madeShot) {
        if (shootFreeThrows) {
            if (homeTeamHasBall) {
                if (homeTeam.isUser) {
                    homeTeam.makeUserSubs(playerWithBall - 1)
                } else {
                    awayTeam.makeUserSubs(-1)
                }
            } else {
                if (homeTeam.isUser) {
                    homeTeam.makeUserSubs(-1)
                } else {
                    awayTeam.makeUserSubs(playerWithBall - 1)
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