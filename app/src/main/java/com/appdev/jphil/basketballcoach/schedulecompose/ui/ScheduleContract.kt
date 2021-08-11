package com.appdev.jphil.basketballcoach.schedulecompose.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.compose.arch.ViewState
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleDataModel

interface ScheduleContract {

    interface ScheduleInteractor :
        ScheduleUiModel.Interactor {
            fun onDismissSimDialog()
        }

    data class ScheduleDataState(
        val teamId: Int,
        val isLoading: Boolean,
        val selectedGameId: Int,
        val showSimDialog: Boolean,
        val dataModels: List<ScheduleDataModel>,
        val dialogDataModels: List<ScheduleDataModel>
    )

    data class ScheduleViewState(
        val isLoading: Boolean,
        val showSimDialog: Boolean,
        val uiModels: List<UiModel>,
        val dialogUiModels: List<UiModel>
    ): ViewState
}