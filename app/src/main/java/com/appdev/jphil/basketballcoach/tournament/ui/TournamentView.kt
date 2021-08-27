package com.appdev.jphil.basketballcoach.tournament.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.appdev.jphil.basketball.tournament.TournamentType
import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.schedule.ui.SimDialogUiModel
import kotlinx.coroutines.flow.StateFlow

@Composable
@ExperimentalAnimationApi
fun TournamentScreen(
    viewStateFlow: StateFlow<TournamentContract.TournamentViewState>,
    interactor: TournamentContract.TournamentInteractor
) {
    val viewState by viewStateFlow.collectAsState()
    if (viewState.isLoading || viewState.tournamentType == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(75.dp))
        }
    } else {
        TournamentView(viewState = viewState) {
            viewState.uiModels.forEach { item ->
                when (item) {
                    is TournamentGameUiModel -> ScheduleItem(uiModel = item, interactor = interactor)
                }
            }
        }
        viewState.dialogUiModel?.let {
            SimulationDialog(it, viewState.gameToPlay, interactor)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TournamentView(
    viewState: TournamentContract.TournamentViewState,
    contents: @Composable () -> Unit
) {
    when (viewState.tournamentType) {
        TournamentType.TEN -> TenTeamTournamentView(contents = contents)
        TournamentType.EIGHT -> EightTeamTournamentView(contents = contents)
    }
}

@Composable
fun TenTeamTournamentView(
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit
) {
    Layout(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState()),
        content = contents
    ) { measurables, constraints ->
        var maxHeight = 0
        var maxWidth = 0
        var totalWidth = 0
        var totalHeight = 0

        val itemConstraints = constraints.copy(
            maxWidth = (constraints.minWidth * 0.8).toInt(),
            minWidth = 0,
            minHeight = 0
        )

        val placeables = measurables.map {
            it.measure(itemConstraints).also { placeable ->
                if (placeable.height > maxHeight) {
                    maxHeight = placeable.height
                }
                if (placeable.width > maxWidth) {
                    maxWidth = placeable.width
                }
                totalWidth += placeable.width
                totalHeight += placeable.height
            }
        }

        layout(totalWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                // TODO: handle non 10-team tournaments
                // TODO: prevent user from scrolling everything off screen
                // TODO: animate item size?
                when (index) {
                    // Play in round
                    0 -> placeable.placeRelative(x = 0, y = 0)
                    1 -> placeable.placeRelative(x = 0, y = maxHeight * 3)
                    // 1st round
                    2 -> placeable.placeRelative(x = maxWidth, y = 0)
                    3 -> placeable.placeRelative(x = maxWidth, y = maxHeight)
                    4 -> placeable.placeRelative(x = maxWidth, y = maxHeight * 2)
                    5 -> placeable.placeRelative(x = maxWidth, y = maxHeight * 3)
                    // 2nd round
                    6 -> placeable.placeRelative(x = 2 * maxWidth, y = (maxHeight * 0.5).toInt())
                    7 -> placeable.placeRelative(x = 2 * maxWidth, y = (maxHeight * 2.5).toInt())
                    // 3rd round
                    8 -> placeable.placeRelative(x = 3 * maxWidth, y = (maxHeight * 1.5).toInt())
                }
            }
        }
    }
}

@Composable
fun EightTeamTournamentView(
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit
) {
    Layout(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState()),
        content = contents
    ) { measurables, constraints ->
        var maxHeight = 0
        var maxWidth = 0
        var totalWidth = 0
        var totalHeight = 0

        val itemConstraints = constraints.copy(
            maxWidth = (constraints.minWidth * 0.8).toInt(),
            minWidth = 0,
            minHeight = 0
        )

        val placeables = measurables.map {
            it.measure(itemConstraints).also { placeable ->
                if (placeable.height > maxHeight) {
                    maxHeight = placeable.height
                }
                if (placeable.width > maxWidth) {
                    maxWidth = placeable.width
                }
                totalWidth += placeable.width
                totalHeight += placeable.height
            }
        }

        layout(totalWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                // TODO: prevent user from scrolling everything off screen
                // TODO: animate item size?
                when (index) {
                    // 1st round
                    0 -> placeable.placeRelative(x = 0, y = 0)
                    1 -> placeable.placeRelative(x = 0, y = maxHeight)
                    2 -> placeable.placeRelative(x = 0, y = maxHeight * 2)
                    3 -> placeable.placeRelative(x = 0, y = maxHeight * 3)
                    // 2nd round
                    4 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 0.5).toInt())
                    5 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 2.5).toInt())
                    // 3rd round
                    6 -> placeable.placeRelative(x = 2 * maxWidth, y = (maxHeight * 1.5).toInt())
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ScheduleItem(
    uiModel: TournamentGameUiModel,
    interactor: TournamentGameUiModel.Interactor
) {
    Card(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp)
            .clickable { interactor.toggleShowButtons(uiModel) }
    ) {
        Column {
            ScheduleItemRow(
                teamName = uiModel.topTeamName,
                teamScore = uiModel.topTeamScore,
                isWinner = uiModel.topTeamScore >= uiModel.bottomTeamScore
            )
            ScheduleItemRow(
                teamName = uiModel.bottomTeamName,
                teamScore = uiModel.bottomTeamScore,
                isWinner = uiModel.bottomTeamScore >= uiModel.topTeamScore
            )
            AnimatedVisibility(visible = !uiModel.isShowButtons) {
                Text(
                    text = "Game ${uiModel.gameNumber}",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
            AnimatedVisibility(visible = uiModel.isShowButtons) {
                ScheduleButtons(uiModel, interactor)
            }
        }
    }
}

@Composable
private fun ScheduleItemRow(
    teamName: String,
    teamScore: String,
    isWinner: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = teamName,
            style = MaterialTheme.typography.body1.copy(
                color = if (isWinner) Color.Black else Color.Gray
            ),
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = teamScore,
            style = MaterialTheme.typography.body1.copy(
                color = if (isWinner) Color.Black else Color.Gray
            ),
        )
    }
}

@Composable
private fun ScheduleButtons(
    uiModel: TournamentGameUiModel,
    interactor: TournamentGameUiModel.Interactor
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

@Composable
fun SimulationDialog(
    simDialogUiModel: SimDialogUiModel,
    gameToPlay: TournamentGameUiModel?,
    interactor: TournamentContract.TournamentInteractor
) {
    Dialog(onDismissRequest = { interactor.onDismissSimDialog() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .background(Color.White)
        ) {
            Column {
                val titleText = if (simDialogUiModel.isSimActive) {
                    "Simulating game: ${simDialogUiModel.numberOfGamesSimmed} of ${simDialogUiModel.numberOfGamesToSim}"
                } else {
                    "Simulation complete. ${simDialogUiModel.numberOfGamesSimmed} games simulated."
                }
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                DialogGames(simDialogUiModel.gameModels)
                Row {
                    TextButton(
                        onClick = { interactor.onDismissSimDialog() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = if (simDialogUiModel.isSimActive) "Cancel Sim" else "Close")
                    }
                    if (!simDialogUiModel.isSimActive && simDialogUiModel.isSimulatingToGame && gameToPlay != null) {
                        TextButton(
                            onClick = { interactor.onStartGame(gameToPlay) },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = "Start Game")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DialogGames(uiModels: List<UiModel>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier.fillMaxHeight(0.9f)
    ) {
        items(uiModels) { model ->
            when (model) {
                is TournamentGameUiModel -> DialogGame(model)
            }
        }
    }
}

@Composable
fun DialogGame(model: TournamentGameUiModel) {
    Card {
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            DialogGameRow(
                teamName = model.topTeamName,
                teamScore = model.topTeamScore,
                isWinner = model.topTeamScore > model.bottomTeamScore
            )
            DialogGameRow(
                teamName = model.bottomTeamName,
                teamScore = model.bottomTeamScore,
                isWinner = model.topTeamScore < model.bottomTeamScore
            )
        }
    }
}

@Composable
private fun DialogGameRow(
    teamName: String,
    teamScore: String,
    isWinner: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = teamName,
            style = MaterialTheme.typography.body1.copy(
                color = if (isWinner) Color.Black else Color.Gray
            ),
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = teamScore,
            style = MaterialTheme.typography.body1.copy(
                color = if (isWinner) Color.Black else Color.Gray
            )
        )
    }
}
