package com.appdev.jphil.basketballcoach.newseason.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NewSeasonLoadingView(
    viewStateFlow: StateFlow<NewSeasonContract.NewSeasonViewState>,
    interactor: NewSeasonContract.Interactor
) {
    val viewState by viewStateFlow.collectAsState()
    LazyColumn(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Starting new season",
                    style = MaterialTheme.typography.h5
                )
            }
        }
        items(viewState.uiModels) {
            when (it) {
                is NewSeasonModel -> it.NewSeasonSteps()
                is StartNewSeasonModel -> NewSeasonButton(interactor = interactor)
            }
        }
    }
}

@Composable
fun NewSeasonModel.NewSeasonSteps() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (currentStepCount > 0) {
            NewSeasonStep(text = "Deleting old games", isLoading = isDeletingGames)
        }
        if (currentStepCount > 1) {
            UpdateTeams(
                numberOfTeamsUpdated = numberOfTeamsUpdated,
                numberOfTeamsToUpdate = numberOfTeamsToUpdate
            )
        }
        if (currentStepCount > 2) {
            NewSeasonStep(text = "Scheduling new games", isLoading = isGeneratingNewGames)
        }
        if (currentStepCount > 3) {
            NewSeasonStep(text = "Creating new recruits", isLoading = isGeneratingRecruits)
        }
    }
}

@Composable
fun NewSeasonButton(interactor: NewSeasonContract.Interactor) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(
            onClick = { interactor.startNewSeason() },
        ) {
            Text(
                text = "Begin new season",
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
fun NewSeasonStep(
    text: String,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 1.dp,
                modifier = Modifier.size(16.dp)
                    .align(CenterVertically)
            )
        }
        val padding = if (isLoading) 8.dp else 0.dp
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = padding)
        )
    }
}

@Composable
fun UpdateTeams(
    numberOfTeamsUpdated: Int,
    numberOfTeamsToUpdate: Int
) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        if (numberOfTeamsToUpdate != numberOfTeamsUpdated) {
            CircularProgressIndicator(
                strokeWidth = 1.dp,
                modifier = Modifier.size(16.dp)
                    .align(CenterVertically)
            )
        }
        val padding = if (numberOfTeamsToUpdate != numberOfTeamsUpdated) {
            8.dp
        } else {
            0.dp
        }
        Text(
            text = "Updating teams... $numberOfTeamsUpdated of $numberOfTeamsToUpdate complete",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = padding)
        )
    }
}

@Preview
@Composable
fun PreviewStep() {
    NewSeasonStep(text = "Generating new games", isLoading = true)
}

@Preview
@Composable
fun PreviewTeams() {
    UpdateTeams(numberOfTeamsUpdated = 25, numberOfTeamsToUpdate = 102)
}
