package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.Coach
import java.util.*

object CoachFactory {

    fun generateCoach(
        teamId: Int,
        firstName: String,
        lastName: String
    ): Coach {
        val r = Random()
        val offenseFavorsThrees = r.nextInt(100)
        val pace = r.nextInt(30) + Coach.minimumPace
        val aggression = r.nextInt(100)
        val defenseFavorsThrees = r.nextInt(100)

        val presses = r.nextInt(100) < 20
        val pressFrequency = if (presses) r.nextInt(66) + 34 else 0
        val pressAggression = if (presses) r.nextInt(99) + 1 else 0

        return Coach(
            null,
            teamId,
            firstName,
            lastName,
            offenseFavorsThrees,
            pace,
            aggression,
            defenseFavorsThrees,
            pressFrequency,
            pressAggression,
            offenseFavorsThrees,
            pace,
            aggression,
            defenseFavorsThrees,
            pressFrequency,
            pressAggression
        )
    }
}