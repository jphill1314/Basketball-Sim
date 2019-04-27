package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import java.util.*

object CoachFactory {

    private const val attributeVariance = 20

    fun generateCoach(
        teamId: Int,
        type: CoachType,
        firstName: String,
        lastName: String,
        rating: Int
    ): Coach {
        val r = Random()
        val offenseFavorsThrees = r.nextInt(100)
        val pace = r.nextInt(30) + Coach.minimumPace
        val aggression = r.nextInt(100)
        val defenseFavorsThrees = r.nextInt(100)

        val presses = r.nextInt(100) < 20
        val pressFrequency = if (presses) r.nextInt(66) + 34 else 0
        val pressAggression = if (presses) r.nextInt(99) + 1 else 0

        val teachShooting = rating + (2 * r.nextInt(attributeVariance)) - attributeVariance
        val teachPostMoves = rating + (2 * r.nextInt(attributeVariance)) - attributeVariance
        val teachBallControl = rating + (2 * r.nextInt(attributeVariance)) - attributeVariance
        val teachPostDefense = rating + (2 * r.nextInt(attributeVariance)) - attributeVariance
        val teachPerimeterDefense = rating + (2 * r.nextInt(attributeVariance)) - attributeVariance
        val teachPositioning = rating + (2 * r.nextInt(attributeVariance)) - attributeVariance
        val teachRebounding = rating + (2 * r.nextInt(attributeVariance)) - attributeVariance
        val teachConditioning = rating + (2 * r.nextInt(attributeVariance)) - attributeVariance

        return Coach(
            null,
            teamId,
            type,
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
            pressAggression,
            teachShooting,
            teachPostMoves,
            teachBallControl,
            teachPostDefense,
            teachPerimeterDefense,
            teachPositioning,
            teachRebounding,
            teachConditioning
        )
    }
}