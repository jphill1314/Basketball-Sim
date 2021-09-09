package com.appdev.jphil.basketballcoach.schedule.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.appdev.jphil.basketballcoach.LoadingScreen
import com.appdev.jphil.basketballcoach.R
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
        LoadingScreen()
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
                is NationalChampionshipUiModel -> NationalChampionshipItem(
                    uiModel = item,
                    interactor = interactor
                )
                is FinishSeasonUiModel -> FinishSeasonItem(finishSeasonModel = item, interactor = interactor)
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
            ScheduleItemRow(
                teamName = uiModel.topTeamName,
                teamScore = uiModel.topTeamScore,
                isTextFaded = uiModel.isFinal && uiModel.topTeamScore < uiModel.bottomTeamScore
            )
            ScheduleItemRow(
                teamName = uiModel.bottomTeamName,
                teamScore = uiModel.bottomTeamScore,
                isTextFaded = uiModel.isFinal && uiModel.bottomTeamScore < uiModel.topTeamScore
            )
            Text(
                text = stringResource(id = uiModel.gameStatus),
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

@Composable
private fun ScheduleItemRow(
    teamName: String,
    teamScore: String,
    isTextFaded: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = teamName,
            color = if (isTextFaded) Color.Gray else Color.Black,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = teamScore,
            color = if (isTextFaded) Color.Gray else Color.Black,
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

@Composable
fun NationalChampionshipItem(
    uiModel: NationalChampionshipUiModel,
    interactor: ScheduleContract.ScheduleInteractor
) {
    Card(
        modifier = Modifier
            .clickable { interactor.openNationalChampionship(uiModel.isExisting) }
    ) {
        Text(
            text = "National Championship",
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
fun FinishSeasonItem(
    finishSeasonModel: FinishSeasonUiModel,
    interactor: ScheduleContract.ScheduleInteractor
) {
    Card(
        modifier = Modifier
            .clickable { interactor.startNewSeason() }
    ) {
        Text(
            text = "Start new season",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        )
    }
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
            color = if (isWinner) Color.Black else Color.Gray,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = teamScore,
            color = if (isWinner) Color.Black else Color.Gray,
            style = MaterialTheme.typography.body1
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
    gameStatus = R.string.non_conference_game,
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
    override fun openNationalChampionship(isExisting: Boolean) {}
    override fun startNewSeason() {}
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
