package com.appdev.jphil.basketballcoach.newseason.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel

data class NewSeasonModel(
    val currentStepCount: Int,
    val isDeletingGames: Boolean,
    val numberOfTeamsUpdated: Int,
    val numberOfTeamsToUpdate: Int,
    val isGeneratingNewGames: Boolean,
    val isGeneratingRecruits: Boolean
) : UiModel

object StartNewSeasonModel : UiModel {
    interface Interactor {
        fun startNewSeason()
    }
}
