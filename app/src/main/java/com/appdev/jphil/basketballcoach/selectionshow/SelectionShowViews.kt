package com.appdev.jphil.basketballcoach.selectionshow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appdev.jphil.basketballcoach.basketball.NationalChampionshipHelper
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SelectionShowView(
    viewStateFlow: StateFlow<SelectionShowContract.ViewState>,
    interactor: SelectionShowContract.Interactor
) {
    val state by viewStateFlow.collectAsState()
    ListView(state.state, interactor)
}

@Composable
fun ListView(
    state: NationalChampionshipHelper.ChampionshipLoadingState,
    interactor: SelectionShowContract.Interactor
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "National Championship Selection Show", // TODO: can I use "selection show"
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        val text = when (state.stepCounter) {
            1 -> "Gathering committee"
            2 -> "Considering quality loses"
            3 -> "What's a mid-major?"
            in 4..90 -> "Bursting bubbles"
            else -> ""
        }
        SelectionShowStep(
            text = text,
            isLoading = !state.isFinished
        )
        if (state.isFinished) {
            EnterTournamentButton(interactor = interactor)
        }

        LazyColumn {
            val gamesToShow = state.stepCounter - 4
            if (gamesToShow > 0) {
                itemsIndexed(state.games.take(gamesToShow).reversed()) { _, game ->
                    Text(
                        text = game,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SelectionShowStep(
    text: String,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 1.dp,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically)
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
fun EnterTournamentButton(interactor: SelectionShowContract.Interactor) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    ) {
        TextButton(
            onClick = { interactor.onEnterBracket() },
        ) {
            Text(
                text = "View Bracket",
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Preview
@Composable
fun PreviewThis() {
    SelectionShowStep(text = "testing", isLoading = true)
}

@Preview
@Composable
fun MorePreviews() {
    ListView(
        state = NationalChampionshipHelper.ChampionshipLoadingState(
            stepCounter = 2,
            games = listOf("1. Team A vs 8. Team F", "2. Team B vs 7. Team G"),
            isFinished = false
        ),
        interactor = object : SelectionShowContract.Interactor {
            override fun onEnterBracket() {}
        }
    )
}
