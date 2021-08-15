package com.appdev.jphil.basketballcoach.schedulecompose.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.compose.arch.ViewState
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleDataModel
import com.appdev.jphil.basketballcoach.simulation.GameSimRepository2

interface ScheduleContract {

    interface ScheduleInteractor :
        ScheduleUiModel.Interactor,
        SimDialogUiModel.Interactor,
        TournamentUiModel.Interactor

    interface ScheduleEvent

    data class ScheduleDataState(
        val teamId: Int,
        val isLoading: Boolean,
        val selectedGameId: Int,
        val gameToPlay: ScheduleUiModel? = null,
        val simState: GameSimRepository2.SimulationState?,
        val dataModels: List<ScheduleDataModel>,
        val dialogDataModels: List<ScheduleDataModel>
    )

    data class ScheduleViewState(
        val isLoading: Boolean,
        val uiModels: List<UiModel>,
        val dialogUiModel: SimDialogUiModel?,
        val gameToPlay: ScheduleUiModel? = null,
    ) : ViewState
}
