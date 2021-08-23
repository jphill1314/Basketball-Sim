package com.appdev.jphil.basketballcoach.schedulecompose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import kotlinx.coroutines.flow.StateFlow

@Composable
@ExperimentalAnimationApi
fun ScheduleScreen(
    viewStateFlow: StateFlow<ScheduleContract.ScheduleViewState>,
    interactor: ScheduleContract.ScheduleInteractor
) {
    val viewState by viewStateFlow.collectAsState()
    if (viewState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(75.dp))
        }
    } else {
        ScheduleView(viewState = viewState, interactor = interactor)
        viewState.dialogUiModel?.let {
            SimulationDialog(it, viewState.gameToPlay, interactor)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ScheduleView(
    viewState: ScheduleContract.ScheduleViewState,
    interactor: ScheduleContract.ScheduleInteractor
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(viewState.uiModels) { item ->
            when (item) {
                is ScheduleUiModel -> ScheduleItem(uiModel = item, interactor = interactor)
                is TournamentUiModel -> TournamentItem(tournamentUiModel = item, interactor = interactor)
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
        modifier = Modifier.clickable { interactor.toggleShowButtons(uiModel) }
    ) {
        Column {
            Row {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(30.dp)
                        .align(CenterVertically)
                        .clip(CircleShape)
                        .background(uiModel.getIconColor())
                )
                Column {
                    ScheduleItemRow(teamName = uiModel.topTeamName, teamScore = uiModel.topTeamScore)
                    ScheduleItemRow(teamName = uiModel.bottomTeamName, teamScore = uiModel.bottomTeamScore)
                }
            }
            Text(
                text = "Game ${uiModel.gameNumber}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
            AnimatedVisibility(visible = uiModel.isShowButtons) {
                ScheduleButtons(uiModel, interactor)
            }
        }
    }
}

private fun ScheduleUiModel.getIconColor() = if (isFinal) {
    if (isSelectedTeamWinner) Color.Green else Color.Red
} else {
    Color.Gray
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
            .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
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

@Composable
fun TournamentItem(
    tournamentUiModel: TournamentUiModel,
    interactor: ScheduleContract.ScheduleInteractor
) {
    Card(
        modifier = Modifier
            .clickable { interactor.openTournament(tournamentUiModel.isExisting) }
    ) {
        Text(
            text = tournamentUiModel.name,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        )
    }
}

@Preview
@Composable
fun PreviewTournamentItem() {
    TournamentItem(previewTournamentUiModel, previewInteractor)
}

@Composable
fun SimulationDialog(
    simDialogUiModel: SimDialogUiModel,
    gameToPlay: ScheduleUiModel?,
    interactor: ScheduleContract.ScheduleInteractor
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
                is ScheduleUiModel -> DialogGame(model)
            }
        }
    }
}

@Composable
fun DialogGame(model: ScheduleUiModel) {
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
                color = if (isWinner) Color.Green else Color.Gray
            ),
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = teamScore,
            style = MaterialTheme.typography.body1.copy(
                color = if (isWinner) Color.Green else Color.Gray
            )
        )
    }
}

@Preview
@Composable
fun PreviewDialog() {
    SimulationDialog(previewDialogState, previewUiModel, previewInteractor)
}

private val previewUiModel = ScheduleUiModel(
    id = 1,
    gameNumber = 1,
    topTeamName = "Team A",
    bottomTeamName = "Team B",
    topTeamScore = " 90",
    bottomTeamScore = "85",
    isShowButtons = true,
    isFinal = true,
    isSelectedTeamWinner = true,
    isHomeTeamUser = true
)

private val previewTournamentUiModel = TournamentUiModel(
    "Conference Tournament",
    true
)

private val previewInteractor = object : ScheduleContract.ScheduleInteractor {
    override fun toggleShowButtons(uiModel: ScheduleUiModel) {}
    override fun simulateGame(uiModel: ScheduleUiModel) {}
    override fun playGame(uiModel: ScheduleUiModel) {}
    override fun onDismissSimDialog() {}
    override fun onStartGame(uiModel: ScheduleUiModel) {}
    override fun openTournament(isExisting: Boolean) {}
}

private val previewDialogState = SimDialogUiModel(
    isSimActive = true,
    isSimulatingToGame = false,
    numberOfGamesSimmed = 5,
    numberOfGamesToSim = 10,
    gameModels = List(5) { previewUiModel }
)

private val previewViewState = ScheduleContract.ScheduleViewState(
    isLoading = false,
    uiModels = List(15) { previewUiModel },
    dialogUiModel = previewDialogState
)
