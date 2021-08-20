package com.appdev.jphil.basketball.tournament

import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

interface Tournament {
    fun getScheduleDataModels(): MutableList<TournamentDataModel>
    fun generateNextRound(season: Int): List<Game>
    fun getWinnerOfTournament(): Team?
    fun replaceGames(newGames: List<Game>)
    fun getId(): Int
    fun getGames(): List<Game>
}
