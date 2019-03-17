package com.appdev.jphil.basketball.game.extensions

import com.appdev.jphil.basketball.game.Game

fun Game.getMediaTimeout(): Boolean {
    return if (half == 1) {
        if (timeRemaining < 16 * 60 && timeRemaining > 12 * 60 && !mediaTimeOuts[0]) {
            mediaTimeOuts[0] = true
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

fun Game.getCoachExtendsToMedia(): Boolean {
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