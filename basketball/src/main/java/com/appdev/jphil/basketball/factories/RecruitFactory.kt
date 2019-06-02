package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitDesire
import kotlin.math.min
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
                generateRating(),
                generateRating()
            ))
        }

        return recruits
    }

    private fun generateRating(): Int {
        var rating = 25
        do {
            rating += Random.nextInt(26)
        } while (Random.nextDouble() > min(0.5,  (rating / 200.0)) && rating < 75)

        return rating
    }

    private fun generateDesires(): List<RecruitDesire> {
        val desires = mutableListOf<RecruitDesire>()
        if (Random.nextBoolean()) {
            desires.add(RecruitDesire.DEVELOP)
        }

        if (Random.nextDouble() > 0.33) {
            desires.add(RecruitDesire.COMPETE)
        } else if (Random.nextDouble() > 0.33) {
            desires.add(RecruitDesire.STAR)
        } else {
            desires.add(RecruitDesire.GOOD_FIT)
        }

        return desires
    }

    private fun generateRecruit(
        id: Int,
        firstName: String,
        lastName: String,
        position: Int,
        rating: Int,
        potential: Int
    ): Recruit {
        return Recruit(
            id,
            firstName,
            lastName,
            position,
            PlayerFactory.getPlayerType(position - 1),
            generateDesires(),
            rating,
            potential,
            false,
            0,
            mutableListOf()
        )
    }
}