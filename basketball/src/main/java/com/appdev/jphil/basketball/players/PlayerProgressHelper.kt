package com.appdev.jphil.basketball.players

object PlayerProgressHelper {

    private fun getPracticePerformance(progressions: List<PlayerProgression>): Double { // TODO: make this give a useful number
        var start = 0.0
        var finish = 0.0
        if (progressions.isNotEmpty()) {
            if (progressions.size < PRACTICES_TO_CONSIDER) {
                progressions.first().getProgressAsList().forEach { start += it }
            } else {
                progressions[progressions.size - PRACTICES_TO_CONSIDER - 1].getProgressAsList().forEach { start += it }
            }
            progressions.last().getProgressAsList().forEach { finish += it }
        }
        return finish - start
    }

    fun sortPlayersByPerformance(players: List<Player>): List<Pair<String, Double>> {
        val sortedList = mutableListOf<Pair<String, Double>>()
        players.forEach { player -> sortedList.add(Pair(player.fullName, getPracticePerformance(player.progression))) }
        return sortedList.sortedBy { -it.second }
    }

    private const val PRACTICES_TO_CONSIDER = 5
}