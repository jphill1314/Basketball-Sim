package com.appdev.jphil.basketball.teams

import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketball.recruits.Recruit
import kotlin.math.roundToInt

fun Team.updateRecruitingAssignments(allRecruits: List<Recruit>) {
    val positionalNeeds = List(5) { hasNeedAtPosition(it + 1) }
    val positionsWithNeed = positionalNeeds.map { if (it) 1 else 0 }.sum()
    if (positionsWithNeed == 0) return

    // Find 5 best matches for each position needed
    val recruitsToRecruit = positionalNeeds.mapIndexed { index, isNeeded ->
        if (isNeeded) {
            // TODO: make this more sophisticated
            allRecruits.getPreferredRecruitsAtPosition(index + 1, this)
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
        !it.isCommitted && it.getInterest(teamId) >= 100 && isInterestedInCommitment(it)
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

private fun Team.isInterestedInCommitment(recruit: Recruit): Boolean {
    val returningAtPos = roster.filter { it.position == recruit.position }
    val averageTalent = returningAtPos.map { it.getOverallRating() }.average()
    val recruitValue = if (recruit.rating > 80) recruit.rating else (recruit.rating * 2 + recruit.potential) / 3
    return recruitValue > averageTalent - gamesPlayedModifier()
}

private fun List<Recruit>.getPreferredRecruitsAtPosition(
    position: Int,
    team: Team
): List<Recruit> {
    return this.filter {
        it.position == position && !it.isCommitted && it.meetsMinRating(team) && it.meetsMinInterestForLevel(team.teamId)
    }.sortedWith(
        compareBy(
            { -getTeamInterestInPlayer(it.rating, it.potential, it.getInterest(team.teamId), team.gamesPlayed) },
            { it.getRecruitmentLevel(team.teamId) },
        )
    )
}

private fun Recruit.meetsMinRating(team: Team): Boolean {
    val returningAtPos = team.roster.filter { it.position == position }
    val averageTalent = returningAtPos.map { it.getOverallRating() }.average()
    val recruitValue = if (rating > 80) rating else (rating * 2 + potential) / 3
    return recruitValue > averageTalent - team.gamesPlayedModifier() - 10
}

private fun Team.gamesPlayedModifier() = if (gamesPlayed < 10) gamesPlayed else gamesPlayed * 2

private fun getTeamInterestInPlayer(
    rating: Int,
    potential: Int,
    interest: Int,
    gamesPlayed: Int
): Int {
    val value = if (rating > 80) rating else (rating * 2 + potential) / 3
    val valueModifier = 10.0 / gamesPlayed
    return (value * valueModifier).roundToInt() + interest
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
