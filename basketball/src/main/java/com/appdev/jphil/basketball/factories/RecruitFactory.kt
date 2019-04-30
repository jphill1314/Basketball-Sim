package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.recruits.Recruit
import kotlin.random.Random

object RecruitFactory {

    fun generateRecruits(
        firstNames: List<String>,
        lastNames: List<String>,
        numberOfRecruits: Int
    ): List<Recruit> {
        val recruits = mutableListOf<Recruit>()

        for (i in 1..numberOfRecruits) {
            recruits.add(generateRecruit(
                i,
                firstNames[Random.nextInt(firstNames.size)],
                lastNames[Random.nextInt(lastNames.size)],
                (i % 5) + 1,
                Random.nextInt(25, 75)
            ))
        }

        return recruits
    }

    private fun generateRecruit(
        id: Int,
        firstName: String,
        lastName: String,
        position: Int,
        rating: Int
    ): Recruit {
        return Recruit(
            id,
            firstName,
            lastName,
            position,
            PlayerFactory.getPlayerType(position - 1),
            rating,
            false,
            mutableListOf()
        )
    }
}