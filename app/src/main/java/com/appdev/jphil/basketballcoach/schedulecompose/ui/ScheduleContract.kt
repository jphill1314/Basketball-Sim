package com.appdev.jphil.basketballcoach.schedulecompose.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.compose.arch.ViewState
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleDataModel

interface ScheduleContract {

    interface ScheduleInteractor :
        ScheduleUiModel.Interactor

    data class ScheduleDataState(
        val teamId: Int,
        val isLoading: Boolean,
        val selectedGameId: Int,
        val dataModels: List<ScheduleDataModel>
    )

    data class ScheduleViewState(
        val isLoading: Boolean,
        val uiModels: List<UiModel>
    ): ViewState
}