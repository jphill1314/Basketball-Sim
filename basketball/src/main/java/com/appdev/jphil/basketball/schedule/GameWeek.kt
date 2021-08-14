package com.appdev.jphil.basketball.schedule

import com.appdev.jphil.basketball.game.Game

class GameWeek(private val maxSize: Int) {
    private val games = mutableListOf<Game>()

    fun addGame(game: Game): Boolean {
        if (games.size >= maxSize) {
            return false
        }

        val hId = game.homeTeam.teamId
        val aId = game.awayTeam.teamId
        games.forEach {
            val homeId = it.homeTeam.teamId
            if (homeId == hId || homeId == aId) {
                return false
            }
            val awayId = it.awayTeam.teamId
            if (awayId == hId || awayId == aId) {
                return false
            }
        }
        games.add(game)
        return true
    }

    fun isAtMaxSize() = games.size >= maxSize

    fun getGames() = games
}
