package com.appdev.jphil.basketballcoach.stats

import com.appdev.jphil.basketballcoach.compose.arch.UiModel

data class TeamStandingModel(
    val teamId: Int,
    val teamName: String,
    val confWins: Int,
    val confLoses: Int,
    val wins: Int,
    val loses: Int
) : UiModel {

    interface Interactor {
        fun onTeamClicked(teamId: Int)
    }
}

object StandingsHeader : UiModel

object RankingsHeader : UiModel

data class TeamRankingModel(
    val teamId: Int,
    val teamName: String,
    val eff: Double,
    val pace: Double,
    val offEff: Double,
    val defEff: Double
) : UiModel
