package com.appdev.jphil.basketballcoach.schedulecompose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun ScheduleView(
    viewState: ScheduleContract.ScheduleViewState,
    interactor: ScheduleContract.ScheduleInteractor
) {
    LazyColumn {
        itemsIndexed(viewState.uiModels) { index, item ->
            when (item) {
                is ScheduleUiModel -> ScheduleItem(uiModel = item, interactor = interactor)
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun PreviewScheduleView() {
    ScheduleView(
        previewViewState,
        previewInteractor
    )
}

@ExperimentalAnimationApi
@Composable
fun ScheduleItem(
    uiModel: ScheduleUiModel,
    interactor: ScheduleUiModel.Interactor
) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .clickable { interactor.toggleShowButtons(uiModel) }
    ) {
        Column {
            ScheduleItemRow(teamName = uiModel.topTeamName, teamScore = uiModel.topTeamScore)
            ScheduleItemRow(teamName = uiModel.bottomTeamName, teamScore = uiModel.bottomTeamScore)
            AnimatedVisibility(visible = uiModel.isShowButtons) {
                ScheduleButtons(uiModel, interactor)
            }
        }
    }
}

@Composable
private fun ScheduleItemRow(
    teamName: String,
    teamScore: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = teamName,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = teamScore,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
private fun ScheduleButtons(
    uiModel: ScheduleUiModel,
    interactor: ScheduleUiModel.Interactor
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextButton(
            onClick = { interactor.simulateGame(uiModel) },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Simulate Game"
            )
        }
        TextButton(
            onClick = { interactor.playGame(uiModel) },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Play Game"
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun PreviewScheduleItem() {
    ScheduleItem(
        previewUiModel,
        previewInteractor
    )
}

private val previewUiModel = ScheduleUiModel(
    id = "",
    topTeamName = "Team A",
    bottomTeamName = "Team B",
    topTeamScore = " 90",
    bottomTeamScore = "85",
    isShowButtons = true
)

private val previewInteractor = object : ScheduleContract.ScheduleInteractor {
    override fun toggleShowButtons(uiModel: ScheduleUiModel) {}
    override fun simulateGame(uiModel: ScheduleUiModel) {}
    override fun playGame(uiModel: ScheduleUiModel) {}
}

private val previewViewState = ScheduleContract.ScheduleViewState(
    uiModels = List(15) { previewUiModel }
)