package com.appdev.jphil.basketballcoach.tournamentcompose.ui

import com.appdev.jphil.basketball.tournament.TournamentType
import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.compose.arch.ViewState
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleDataModel
import com.appdev.jphil.basketballcoach.schedulecompose.ui.ScheduleUiModel
import com.appdev.jphil.basketballcoach.schedulecompose.ui.SimDialogUiModel
import com.appdev.jphil.basketballcoach.simulation.SimulationState

interface TournamentContract {

    interface TournamentInteractor :
        ScheduleUiModel.Interactor,
        SimDialogUiModel.Interactor

    data class TournamentDataState(
        val conferenceId: Int,
        val isLoading: Boolean = true,
        val selectedGameId: Int = -1,
        val tournamentType: TournamentType? = null,
        val gameToPlay: ScheduleUiModel? = null,
        val simState: SimulationState? = null,
        val dataModels: List<ScheduleDataModel> = emptyList(),
        val dialogDataModels: List<ScheduleDataModel> = emptyList()
    )

    data class TournamentViewState(
        val isLoading: Boolean,
        val uiModels: List<UiModel>,
        val tournamentType: TournamentType?,
        val dialogUiModel: SimDialogUiModel?,
        val gameToPlay: ScheduleUiModel? = null,
    ) : ViewState
}
