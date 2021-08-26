package com.appdev.jphil.basketballcoach.tournamentcompose.ui

import com.appdev.jphil.basketball.tournament.TournamentType
import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.schedulecompose.ui.SimDialogUiModel
import com.appdev.jphil.basketballcoach.simulation.SimulationState
import com.appdev.jphil.basketballcoach.tournamentcompose.data.TournamentDataModel
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
        val games = dataState.dataModels.mapIndexed { index, model ->
            TournamentGameUiModel(
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


        // TODO: cleanup / make pretty
        // TODO: show teams that have already made it to the next round
        val placeholders: List<UiModel> = when (dataState.tournamentType) {
            TournamentType.EIGHT -> {
                if (games.size == 7) {
                    emptyList()
                } else {
                    (games.size until 7).map {
                        TournamentGameUiModel(
                            id = -1,
                            gameNumber = it + 1,
                            topTeamName = "",
                            topTeamScore = "",
                            bottomTeamName = "",
                            bottomTeamScore = "",
                            isShowButtons = false,
                            isFinal = false,
                            isSelectedTeamWinner = true,
                            isHomeTeamUser = false
                        )
                    }
                }
            }
            TournamentType.TEN -> {
                if (games.size == 9) {
                    emptyList()
                } else {
                    (games.size until 9).map {
                        TournamentGameUiModel(
                            id = -1,
                            gameNumber = it + 1,
                            topTeamName = "",
                            topTeamScore = "",
                            bottomTeamName = "",
                            bottomTeamScore = "",
                            isShowButtons = false,
                            isFinal = false,
                            isSelectedTeamWinner = true,
                            isHomeTeamUser = false
                        )
                    }
                }
            }
            else -> emptyList()
        }

        return games + placeholders
    }

    private fun createDialogModel(
        simState: SimulationState?,
        dataModels: List<TournamentDataModel>
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

    private fun createDialogUiModels(dataModels: List<TournamentDataModel>): List<UiModel> {
        return dataModels.reversed().map { model ->
            TournamentGameUiModel(
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
