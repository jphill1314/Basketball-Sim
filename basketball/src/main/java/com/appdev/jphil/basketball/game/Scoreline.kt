package com.appdev.jphil.basketball.game

class Scoreline(
    val homeScores: MutableList<Int> = mutableListOf(),
    val awayScores: MutableList<Int> = mutableListOf()
) {

    fun addScores(homeTotalScore: Int, awayTotalScore: Int) {
        if (homeScores.isEmpty()) {
            homeScores.add(homeTotalScore)
        } else {
            val previousHomeScore = homeScores.reduce { acc, i -> acc + i }
            homeScores.add(homeTotalScore - previousHomeScore)
        }

        if (awayScores.isEmpty()) {
            awayScores.add(awayTotalScore)
        } else {
            val previousAwayScore = awayScores.reduce { acc, i -> acc + i }
            awayScores.add(awayTotalScore - previousAwayScore)
        }
    }
}