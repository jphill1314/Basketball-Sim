package com.appdev.jphil.basketball.coaches

import kotlin.random.Random

object StrategyHelper {

    fun updateStrategyLateGame(scoreDiff: Int, timeRemaining: Int, coach: Coach) {
        val minLeft = timeRemaining / 60.0
        val pointsPerMinute = scoreDiff / minLeft
        if (scoreDiff < 0) {
            // Losing with less than 2 min to go
            coach.shouldWasteTime = false
            if (coach.intentionallyFoul) {
                // Continue to intentionally foul?
                if (pointsPerMinute !in 1.5..8.0) {
                    coach.apply {
                        intentionallyFoul = false
                        shouldHurry = false
                    }
                }
            } else {
                // Start to intentionally foul
                if (pointsPerMinute in 2.5..7.0) {
                    coach.apply {
                        intentionallyFoul = true
                        shouldHurry = true
                    }
                }
            }
        } else if (scoreDiff > 0) {
            coach.apply {
                shouldHurry = false
                intentionallyFoul = false
                shouldWasteTime = if (shouldWasteTime) {
                    if (pointsPerMinute > 8) {
                        true
                    } else {
                        Random.nextDouble() > 0.3
                    }
                } else {
                    Random.nextDouble() > 0.25
                }
            }
        } else {
            coach.apply {
                intentionallyFoul = false
                shouldWasteTime = false
                shouldHurry = false
            }
        }
    }

    fun updateStrategy(scoreDiff: Int, coach: Coach) {
        coach.apply {
            intentionallyFoul = false
            shouldHurry = false
            shouldWasteTime = false
        }
    }
}