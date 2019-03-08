package com.appdev.jphil.basketball

class Conference(
    val id: Int,
    val name: String,
    val teams: List<Team>
) {

    fun generateSchedule(season: Int): List<Game> {
        val games = mutableListOf<Game>()

        for (x in 0 until teams.size) {
            for (y in 0 until teams.size) {
                if (x != y) {
                    games.add(Game(teams[x], teams[y], false, season))
                }
            }
        }

        games.shuffle()

        return games
    }

}