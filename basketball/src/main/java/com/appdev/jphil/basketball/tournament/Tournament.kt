package com.appdev.jphil.basketball.tournament

import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team

interface Tournament {
    fun getScheduleDataModels(): MutableList<TournamentDataModel>
    fun generateNextRound(season: Int)
    fun getWinnerOfTournament(): Team?
    fun replaceGames(newGames: List<Game>)
    fun getAllGames(): List<Game>
    fun numberOfGames(): Int
    fun getId(): Int
}