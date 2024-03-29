package com.appdev.jphil.basketballcoach.tournament.ui

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.appdev.jphil.basketball.tournament.TournamentType
import com.appdev.jphil.basketballcoach.LoadingScreen
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
        LoadingScreen()
    } else {
        TournamentView(viewState = viewState) {
            viewState.uiModels.forEach { item ->
                when (item) {
                    is TournamentGameUiModel -> GameItem(uiModel = item, interactor = interactor)
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
        TournamentType.NATIONAL_CHAMPIONSHIP -> NationalChampionshipView(contents = contents)
    }
}

@ExperimentalAnimationApi
@Composable
fun GameItem(
    uiModel: TournamentGameUiModel,
    interactor: TournamentGameUiModel.Interactor
) {
    Card(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp)
            .clickable { interactor.toggleShowButtons(uiModel) }
    ) {
        Column {
            GameItemRow(
                teamName = uiModel.topTeamName,
                teamSeed = uiModel.topTeamSeed,
                teamScore = uiModel.topTeamScore,
                isWinner = uiModel.topTeamScore >= uiModel.bottomTeamScore
            )
            GameItemRow(
                teamName = uiModel.bottomTeamName,
                teamSeed = uiModel.bottomTeamSeed,
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
                GameButtons(uiModel, interactor)
            }
        }
    }
}

@Composable
private fun GameItemRow(
    teamName: String,
    teamSeed: String,
    teamScore: String,
    isWinner: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = if (teamName.isNotEmpty()) "#$teamSeed $teamName" else "",
            color = if (isWinner) Color.Black else Color.Gray,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = teamScore,
            color = if (isWinner) Color.Black else Color.Gray,
            style = MaterialTheme.typography.body1,
        )
    }
}

@Composable
private fun GameButtons(
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
        if (uiModel.isUserGame) {
            TextButton(
                onClick = { interactor.playGame(uiModel) },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Play Game"
                )
            }
        } else {
            Box(modifier = Modifier.weight(1f))
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
