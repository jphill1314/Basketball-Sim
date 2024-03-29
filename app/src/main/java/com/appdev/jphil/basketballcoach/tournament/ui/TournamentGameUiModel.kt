package com.appdev.jphil.basketballcoach.tournament.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel

data class TournamentGameUiModel(
    val id: Int,
    val gameNumber: Int,
    val topTeamName: String,
    val bottomTeamName: String,
    val topTeamScore: String,
    val bottomTeamScore: String,
    val topTeamSeed: String,
    val bottomTeamSeed: String,
    val isShowButtons: Boolean,
    val isFinal: Boolean,
    val isSelectedTeamWinner: Boolean,
    val isHomeTeamUser: Boolean,
    val isUserGame: Boolean
) : UiModel {
    interface Interactor {
        fun toggleShowButtons(uiModel: TournamentGameUiModel)
        fun simulateGame(uiModel: TournamentGameUiModel)
        fun playGame(uiModel: TournamentGameUiModel)
    }
}

data class TournamentDialogUiModel(
    val isSimActive: Boolean,
    val isSimulatingToGame: Boolean,
    val numberOfGamesToSim: Int,
    val numberOfGamesSimmed: Int,
    val gameModels: List<UiModel>
) : UiModel {
    interface Interactor {
        fun onDismissSimDialog()
        fun onStartGame(uiModel: TournamentGameUiModel)
    }
}
