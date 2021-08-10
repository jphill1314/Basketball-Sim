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
    val isSelectedTeamWinner: Boolean
) : UiModel {
    interface Interactor {
        fun toggleShowButtons(uiModel: ScheduleUiModel)
        fun simulateGame(uiModel: ScheduleUiModel)
        fun playGame(uiModel: ScheduleUiModel)
    }
}