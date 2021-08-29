package com.appdev.jphil.basketballcoach.tournament.data

data class TournamentDataModel(
    val gameId: Int,
    val topTeamId: Int,
    val bottomTeamId: Int,
    val topTeamName: String,
    val bottomTeamName: String,
    val topTeamScore: Int,
    val bottomTeamScore: Int,
    val topTeamSeed: Int,
    val bottomTeamSeed: Int,
    val isInProgress: Boolean,
    val isFinal: Boolean,
    val isHomeTeamUser: Boolean
)
