package com.appdev.jphil.basketballcoach.schedulecompose.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.compose.arch.ViewState

interface ScheduleContract {

    interface ScheduleInteractor :
        ScheduleUiModel.Interactor

    data class ScheduleViewState(
        val uiModels: List<UiModel>
    ): ViewState
}