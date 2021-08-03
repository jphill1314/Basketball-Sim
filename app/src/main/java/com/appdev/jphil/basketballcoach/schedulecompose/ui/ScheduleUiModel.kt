package com.appdev.jphil.basketballcoach.schedulecompose.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel

data class ScheduleUiModel(
    val id: String,
    val topTeamName: String,
    val bottomTeamName: String,
    val topTeamScore: String,
    val bottomTeamScore: String,
    val isShowButtons: Boolean
) : UiModel {
    interface Interactor {
        fun toggleShowButtons(uiModel: ScheduleUiModel)
        fun simulateGame(uiModel: ScheduleUiModel)
        fun playGame(uiModel: ScheduleUiModel)
    }
}