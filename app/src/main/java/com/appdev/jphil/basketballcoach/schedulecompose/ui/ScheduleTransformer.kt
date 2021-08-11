package com.appdev.jphil.basketballcoach.schedulecompose.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleDataModel
import javax.inject.Inject

class ScheduleTransformer @Inject constructor() {

    fun transformDataModels(
        dataState: ScheduleContract.ScheduleDataState
    ): ScheduleContract.ScheduleViewState {
        return ScheduleContract.ScheduleViewState(
            isLoading = dataState.isLoading,
            showSimDialog = dataState.showSimDialog,
            uiModels = createUiModels(dataState),
            dialogUiModels = createDialogUiModels(dataState.dialogDataModels)
        )
    }

    private fun createUiModels(
        dataState: ScheduleContract.ScheduleDataState
    ): List<ScheduleUiModel> {
        val teamRecords = calculateTeamRecords(dataState.dataModels)
        return dataState.dataModels.filter {
            it.topTeamId == dataState.teamId || it.bottomTeamId == dataState.teamId
        }.mapIndexed { index, model ->
            ScheduleUiModel(
                id = model.gameId,
                gameNumber = index + 1,
                topTeamName = model.topTeamName,
                bottomTeamName = model.bottomTeamName,
                topTeamScore = when {
                    model.isFinal -> model.topTeamScore.toString()
                    model.isInProgress -> model.topTeamScore.toString()
                    else -> {
                        val record = teamRecords.getOrElse(model.topTeamId) { Pair(0, 0) }
                        "${record.first} - ${record.second}"
                    }
                },
                bottomTeamScore = when {
                    model.isFinal -> model.bottomTeamScore.toString()
                    model.isInProgress -> model.bottomTeamScore.toString()
                    else -> {
                        val record = teamRecords.getOrElse(model.bottomTeamId) { Pair(0, 0) }
                        "${record.first} - ${record.second}"
                    }
                },
                isShowButtons = model.gameId == dataState.selectedGameId,
                isFinal = model.isFinal,
                isSelectedTeamWinner = when (model.topTeamId) {
                    dataState.teamId -> model.topTeamScore > model.bottomTeamScore
                    else -> model.bottomTeamScore > model.topTeamScore
                }
            )
        }
    }

    private fun calculateTeamRecords(dataModels: List<ScheduleDataModel>): Map<Int, Pair<Int, Int>> {
        val records = mutableMapOf<Int, Pair<Int, Int>>()
        dataModels.forEach { game ->
            if (!game.isFinal) return@forEach

            val topRecord = records.getOrPut(game.topTeamId) { Pair(0, 0) }
            val bottomRecord = records.getOrPut(game.bottomTeamId) { Pair(0, 0) }

            if (game.topTeamScore > game.bottomTeamScore) {
                records[game.topTeamId] = Pair(topRecord.first + 1, topRecord.second)
                records[game.bottomTeamId] = Pair(bottomRecord.first, bottomRecord.second + 1)
            } else {
                records[game.topTeamId] = Pair(topRecord.first, topRecord.second + 1)
                records[game.bottomTeamId] = Pair(bottomRecord.first + 1, bottomRecord.second)
            }
        }

        return records
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
                isSelectedTeamWinner = false
            )
        }
    }
}