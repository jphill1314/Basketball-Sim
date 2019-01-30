package com.appdev.jphil.basketball

class Conference(
    val id: Int,
    val name: String,
    val teams: List<Team>
) {

    fun generateSchedule(): List<Game> {
        val games = mutableListOf<Game>()

        for (x in 0 until teams.size - 1) {
            for (y in 0 until teams.size - 1) {
                if (x != y) {
                    games.add(Game(teams[x], teams[y], false))
                }
            }
        }

        games.shuffle()

        return games
    }

}