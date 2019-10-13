package com.appdev.jphil.basketball.coaches

import com.appdev.jphil.basketball.recruits.Recruit
import kotlin.random.Random

class ScoutingAssignment(
    val id: Int? = null,
    val positions: MutableList<Int> = mutableListOf(1, 2, 3, 4, 5),
    var minRating: Int = 0,
    var maxRating: Int = 100,
    var minPotential: Int = 0,
    var maxPotential: Int = 100
) {

    fun doScouting(recruitingRating: Int, unknownRecruits: List<Recruit>): List<Recruit> {
        val discoveredRecruits = mutableListOf<Recruit>()
        val disoverableRecruits = unknownRecruits.filter { isDisoverable(it) }.toMutableList()
        while (Random.nextInt(100) < 70 && Random.nextInt(100) < recruitingRating && disoverableRecruits.isNotEmpty()) {
            val index = Random.nextInt(disoverableRecruits.size)
            discoveredRecruits.add(disoverableRecruits[index])
            disoverableRecruits.removeAt(index)
        }
        return discoveredRecruits
    }

    private fun isDisoverable(recruit: Recruit): Boolean {
        return (Random.nextInt(100) < 30 ||
               (recruit.rating in minRating..maxRating &&
                recruit.potential in minPotential..maxPotential)) &&
               positions.contains(recruit.position)
    }
}