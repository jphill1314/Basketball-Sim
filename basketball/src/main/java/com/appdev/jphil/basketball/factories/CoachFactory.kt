package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.Pronouns
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import kotlin.math.min
import kotlin.random.Random

object CoachFactory {

    private const val attributeVariance = 20

    fun generateCoach(
        teamId: Int,
        type: CoachType,
        firstName: String,
        lastName: String,
        rating: Int,
        pronouns: Pronouns = Pronouns.HE
    ): Coach {
        val offenseFavorsThrees = Random.nextInt(100)
        val pace = Random.nextInt(30) + Coach.minimumPace
        val aggression = Random.nextInt(100)
        val defenseFavorsThrees = Random.nextInt(100)

        val presses = Random.nextInt(100) < 20
        val pressFrequency = if (presses) Random.nextInt(66) + 34 else 0
        val pressAggression = if (presses) Random.nextInt(99) + 1 else 0

        val teachShooting = getRating(rating)
        val teachPostMoves = getRating(rating)
        val teachBallControl = getRating(rating)
        val teachPostDefense = getRating(rating)
        val teachPerimeterDefense = getRating(rating)
        val teachPositioning = getRating(rating)
        val teachRebounding = getRating(rating)
        val teachConditioning = getRating(rating)

        val recruiting = getRating(rating)

        return Coach(
            null,
            teamId,
            type,
            firstName,
            lastName,
            pronouns,
            recruiting,
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
            false,
            false,
            false,
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

    private fun getRating(rating: Int): Int {
        return min(100, rating + Random.nextInt(2 * attributeVariance) - attributeVariance)
    }
}
