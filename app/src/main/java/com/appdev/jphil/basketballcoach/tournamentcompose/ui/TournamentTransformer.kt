package com.appdev.jphil.basketballcoach.tournamentcompose.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleDataModel
import com.appdev.jphil.basketballcoach.schedulecompose.ui.ScheduleUiModel
import com.appdev.jphil.basketballcoach.schedulecompose.ui.SimDialogUiModel
import com.appdev.jphil.basketballcoach.simulation.SimulationState
import javax.inject.Inject

class TournamentTransformer @Inject constructor() {

    fun transformDataModels(
        dataState: TournamentContract.TournamentDataState
    ): TournamentContract.TournamentViewState {
        return TournamentContract.TournamentViewState(
            isLoading = dataState.isLoading,
            gameToPlay = dataState.gameToPlay,
            tournamentType = dataState.tournamentType,
            uiModels = createUiModels(dataState),
            dialogUiModel = createDialogModel(dataState.simState, dataState.dialogDataModels)
        )
    }

    private fun createUiModels(
        dataState: TournamentContract.TournamentDataState
    ): List<UiModel> {
        return dataState.dataModels.mapIndexed { index, model ->
            ScheduleUiModel(
                id = model.gameId,
                gameNumber = index + 1,
                topTeamName = model.topTeamName,
                bottomTeamName = model.bottomTeamName,
                topTeamScore = model.topTeamScore.toString(),
                bottomTeamScore = model.bottomTeamScore.toString(),
                isShowButtons = model.gameId == dataState.selectedGameId,
                isFinal = model.isFinal,
                isSelectedTeamWinner = true,
                isHomeTeamUser = model.isHomeTeamUser
            )
        }
    }

    private fun createDialogModel(
        simState: SimulationState?,
        dataModels: List<ScheduleDataModel>
    ): SimDialogUiModel? {
        return simState?.let {
            SimDialogUiModel(
                isSimActive = it.isSimActive,
                isSimulatingToGame = it.isSimulatingToGame,
                numberOfGamesToSim = it.numberOfGamesToSim,
                numberOfGamesSimmed = it.numberOfGamesSimmed,
                gameModels = createDialogUiModels(dataModels)
            )
        }
    }

    private fun createDialogUiModels(dataModels: List<ScheduleDataModel>): List<UiModel> {
        return dataModels.reversed().map { model ->
            ScheduleUiModel(
                id = model.gameId,
                gameNumber = 1,
                topTeamName = model.topTeamName,
                bottomTeamName = model.bottomTeamName,
                topTeamScore = model.topTeamScore.toString(),
                bottomTeamScore = model.bottomTeamScore.toString(),
                isShowButtons = false,
                isFinal = model.isFinal,
                isSelectedTeamWinner = false,
                isHomeTeamUser = model.isHomeTeamUser
            )
        }
    }
}
