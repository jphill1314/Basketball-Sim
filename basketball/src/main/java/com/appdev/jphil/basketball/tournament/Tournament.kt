package com.appdev.jphil.basketball.tournament

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

interface Tournament {
    val id: Int
    val games: List<Game>

    fun generateNextRound(season: Int): List<Game>
    fun getWinnerOfTournament(): Team?
    fun replaceGames(newGames: List<Game>)
}
