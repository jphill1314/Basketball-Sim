package com.appdev.jphil.basketball

import com.appdev.jphil.basketball.game.Game

fun MutableList<Game>.smartShuffleList(numberOfTeams: Int) {
    this.shuffle()

    val maxSize = numberOfTeams / 2
    val gameWeeks = mutableListOf(GameWeek(0, maxSize))
    var index = 1
    while (size > 0) {
        if (gameWeeks.last().isAtMaxSize() || index > size) {
            gameWeeks.add(GameWeek(gameWeeks.size, maxSize))
            index = 1
        }

        if (gameWeeks.last().addGame(this[size - index])) {
            remove(this[size - index])
        } else {
            index++
        }
    }

    gameWeeks.forEach { gameWeek ->
        gameWeek.getGames().forEach { game ->
            add(game)
        }
    }
}