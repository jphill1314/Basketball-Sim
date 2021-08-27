package com.appdev.jphil.basketballcoach.tournament.ui

import com.appdev.jphil.basketball.tournament.TournamentType
import com.appdev.jphil.basketballcoach.compose.arch.DataState
import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.compose.arch.ViewState
import com.appdev.jphil.basketballcoach.schedule.ui.SimDialogUiModel
import com.appdev.jphil.basketballcoach.simulation.SimulationState
import com.appdev.jphil.basketballcoach.tournament.data.TournamentDataModel

interface TournamentContract {

    interface TournamentInteractor :
        TournamentGameUiModel.Interactor,
        TournamentDialogUiModel.Interactor

    data class TournamentDataState(
        val conferenceId: Int,
        val isLoading: Boolean = true,
        val selectedGameId: Int = -1,
        val tournamentType: TournamentType? = null,
        val gameToPlay: TournamentGameUiModel? = null,
        val simState: SimulationState? = null,
        val dataModels: List<TournamentDataModel> = emptyList(),
        val dialogDataModels: List<TournamentDataModel> = emptyList()
    ) : DataState

    data class TournamentViewState(
        val isLoading: Boolean,
        val uiModels: List<UiModel>,
        val tournamentType: TournamentType?,
        val dialogUiModel: SimDialogUiModel?,
        val gameToPlay: TournamentGameUiModel? = null,
    ) : ViewState
}
