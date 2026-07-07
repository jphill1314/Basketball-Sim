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
        NationalChampionshipUiModel.Interactor,
        FinishSeasonUiModel.Interactor

    data class ScheduleDataState(
        val teamId: Int = -1,
        val isLoading: Boolean = true,
        val selectedGameId: Int = -1,
        val isTournamentExisting: Boolean = false,
        val nationalChampExists: Boolean = false,
        val areAllGamesFinal: Boolean = false,
        val isSeasonOver: Boolean = false,
        val gameToPlay: ScheduleUiModel? = null,
        val simState: SimulationState? = null,
        val dataModels: List<ScheduleDataModel> = emptyList(),
        val dialogDataModels: List<ScheduleDataModel> = emptyList(),
    ) : DataState

    data class ScheduleViewState(
        val isLoading: Boolean,
        val uiModels: List<UiModel>,
        val dialogUiModel: SimDialogUiModel?,
        val gameToPlay: ScheduleUiModel? = null,
    ) : ViewState
}
