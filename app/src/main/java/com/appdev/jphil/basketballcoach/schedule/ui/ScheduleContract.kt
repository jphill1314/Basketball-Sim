package com.appdev.jphil.basketballcoach.schedule.ui

import com.appdev.jphil.basketballcoach.compose.arch.DataState
import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.compose.arch.ViewState
import com.appdev.jphil.basketballcoach.schedule.data.ScheduleDataModel
import com.appdev.jphil.basketballcoach.simulation.SimulationState

interface ScheduleContract {

    interface ScheduleInteractor :
        ScheduleUiModel.Interactor,
        SimDialogUiModel.Interactor,
        TournamentUiModel.Interactor,
        FinishSeasonUiModel.Interactor

    data class ScheduleDataState(
        val teamId: Int,
        val isLoading: Boolean,
        val selectedGameId: Int,
        val isTournamentExisting: Boolean,
        val areAllGamesFinal: Boolean = false,
        val isSeasonOver: Boolean = false,
        val gameToPlay: ScheduleUiModel? = null,
        val simState: SimulationState?,
        val dataModels: List<ScheduleDataModel>,
        val dialogDataModels: List<ScheduleDataModel>
    ) : DataState

    data class ScheduleViewState(
        val isLoading: Boolean,
        val uiModels: List<UiModel>,
        val dialogUiModel: SimDialogUiModel?,
        val gameToPlay: ScheduleUiModel? = null,
    ) : ViewState
}
