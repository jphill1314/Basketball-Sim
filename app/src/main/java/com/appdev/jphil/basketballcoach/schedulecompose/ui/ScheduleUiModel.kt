package com.appdev.jphil.basketballcoach.schedulecompose.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel

data class ScheduleUiModel(
    val id: Int,
    val gameNumber: Int,
    val topTeamName: String,
    val bottomTeamName: String,
    val topTeamScore: String,
    val bottomTeamScore: String,
    val isShowButtons: Boolean,
    val isFinal: Boolean,
    val isSelectedTeamWinner: Boolean,
    val isHomeTeamUser: Boolean
) : UiModel {
    interface Interactor {
        fun toggleShowButtons(uiModel: ScheduleUiModel)
        fun simulateGame(uiModel: ScheduleUiModel)
        fun playGame(uiModel: ScheduleUiModel)
    }
}

data class SimDialogUiModel(
    val isSimActive: Boolean,
    val isSimulatingToGame: Boolean,
    val numberOfGamesToSim: Int,
    val numberOfGamesSimmed: Int,
    val gameModels: List<UiModel>
) : UiModel {
    interface Interactor {
        fun onDismissSimDialog()
        fun onStartGame(uiModel: ScheduleUiModel)
    }
}
