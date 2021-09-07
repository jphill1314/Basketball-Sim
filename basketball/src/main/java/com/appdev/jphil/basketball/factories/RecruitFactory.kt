package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitDesire
import com.appdev.jphil.basketball.recruits.RecruitDesireData
import com.appdev.jphil.basketball.teams.Team
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object RecruitFactory {

    fun generateRecruits(
        firstNames: List<String>,
        lastNames: List<String>,
        numberOfRecruits: Int,
        allTeams: List<Team>
    ): List<Recruit> {
        val recruits = mutableListOf<Recruit>()
        val random = java.util.Random()

        for (i in 1..numberOfRecruits) {
            val recruit = generateRecruit(
                i,
                firstNames[Random.nextInt(firstNames.size)],
                lastNames[Random.nextInt(lastNames.size)],
                (i % 5) + 1,
                generateRating(random),
                generateRating(random)
            )

            val desires = RecruitDesireData.from(generateDesires(), recruit)
            recruit.generateInitialInterests(allTeams, desires)
            recruits.add(recruit)
        }

        return recruits
    }

    fun generateRating(random: java.util.Random): Int {
        val rating = (random.nextGaussian() * 15 + 65).toInt()
        return max(25, min(100, rating))
    }

    private fun generateDesires(): RecruitDesire {
        val rand = Random.nextDouble()
        return when {
            rand < .25 -> RecruitDesire.DEVELOP
            rand < .75 -> RecruitDesire.GOOD_FIT
            else -> RecruitDesire.STAR
        }
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
            rating,
            potential,
            false,
            0,
            LocationGenerator.getLocation(),
            mutableListOf()
        )
    }
}
