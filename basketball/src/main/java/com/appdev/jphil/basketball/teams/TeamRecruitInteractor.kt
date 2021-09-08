package com.appdev.jphil.basketball.teams

import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketball.recruits.Recruit

object TeamRecruitInteractor {

    fun Team.updateRecruitingAssignments(allRecruits: List<Recruit>) {
        val positionalNeeds = List(5) { hasNeedAtPosition(it + 1) }
        val positionsWithNeed = positionalNeeds.map { if (it) 1 else 0 }.sum()
        if (positionsWithNeed == 0) return

        // Find 5 best matches for each position needed
        val recruitsToRecruit = positionalNeeds.mapIndexed { index, isNeeded ->
            if (isNeeded) {
                // TODO: make this more sophisticated
                allRecruits.getPreferredRecruitsAtPosition(index + 1, gamesPlayed, teamRating, teamId)
            } else {
                emptyList()
            }
        }

        val assistants = coaches.filter { it.type != CoachType.HEAD_COACH }
        val recruitsPerPosition = (5 * (assistants.size.toDouble() / positionsWithNeed)).toInt()
        val extras = 15 - recruitsPerPosition
        // Assign matches to coaches with equal distribution of positions
        val baseRecruitsToAssign = recruitsToRecruit.flatMap { it.take(recruitsPerPosition) }
        val extraRecruits = recruitsToRecruit.flatten().filter { !baseRecruitsToAssign.contains(it) }.shuffled().take(extras)
        val recruitsToAssign = baseRecruitsToAssign + extraRecruits

        assistants.forEachIndexed { index, coach ->
            coach.recruitingAssignments.apply {
                clear()

                val startIndex = 5 * index
                if (startIndex !in recruitsToAssign.indices) {
                    return@forEachIndexed
                }
                var endIndex = 5 + 5 * index
                if (endIndex !in recruitsToAssign.indices) {
                    endIndex = recruitsToAssign.size
                }

                addAll(recruitsToAssign.subList(startIndex, endIndex))
            }
        }
    }

    fun Team.getCommitmentsIfPossible(allRecruits: List<Recruit>) {
        val interestedRecruits = allRecruits.filter {
            !it.isCommitted && it.getInterest(teamId) >= 100
        }.sortedWith(compareBy { it.rating })

        for (position in 1..5) {
            if (hasNeedAtPosition(position)) {
                val recruitsAtPos = interestedRecruits.filter { it.position == position }.toMutableList()
                while (recruitsAtPos.isNotEmpty() && hasNeedAtPosition(position)) {
                    val commit = recruitsAtPos.first()
                    commit.apply {
                        isCommitted = true
                        teamCommittedTo = teamId
                    }
                    commitments.add(commit)
                    recruitsAtPos.remove(commit)
                }
            }
        }
    }

    private fun List<Recruit>.getPreferredRecruitsAtPosition(
        position: Int,
        gamesPlayed: Int,
        teamRating: Int,
        teamId: Int
    ): List<Recruit> {
        return this.filter {
            it.position == position && !it.isCommitted && it.meetsMinRating(gamesPlayed, teamRating) && it.meetsMinInterestForLevel(teamId)
        }.sortedWith(
            compareBy(
                { -it.getInterest(teamId) },
                { it.getRecruitmentLevel(teamId) },
                { -it.rating }
            )
        )
    }

    private fun Recruit.meetsMinRating(gamesPlayed: Int, teamRating: Int): Boolean {
        val minRating = 20 + gamesPlayed
        val minRatingModifier = minRating + 20 * ((101 - potential) / 100.0)
        return rating > teamRating - minRatingModifier
    }

    private fun Recruit.meetsMinInterestForLevel(teamId: Int): Boolean {
        return when (getRecruitmentLevel(teamId)) {
            0 -> true
            1 -> getInterest(teamId) >= 0
            2 -> getInterest(teamId) >= 10
            3 -> getInterest(teamId) >= 30
            4 -> getInterest(teamId) >= 50
            5 -> getInterest(teamId) >= 70
            else -> false
        }
    }
}
