package com.appdev.jphil.basketballcoach.newseason.ui

import com.appdev.jphil.basketballcoach.compose.arch.DataState
import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.compose.arch.ViewState

interface NewSeasonContract {

    interface Interactor :
            StartNewSeasonModel.Interactor

    data class NewSeasonDataState(
        val stepNumber: Int = 0,
        val isDeletingGames: Boolean = false,
        val totalTeams: Int = 0,
        val teamsUpdated: Int = 0,
        val isGeneratingGames: Boolean = false,
        val isGeneratingRecruits: Boolean = false
    ) : DataState

    data class NewSeasonViewState(
        val uiModels: List<UiModel>
    ) : ViewState
}
