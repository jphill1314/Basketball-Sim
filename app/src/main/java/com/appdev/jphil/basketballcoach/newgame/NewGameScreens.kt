package com.appdev.jphil.basketballcoach.newgame

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.theme.lightColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NewGameScreen(
    viewStateFlow: StateFlow<NewGameContract.DataState>,
    interactor: NewGameContract.Interactor
) {
    val state by viewStateFlow.collectAsState()
    Column {
        Text(
            text = stringResource(id = R.string.select_team_title),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Text(
            text = stringResource(id = R.string.select_team_subtitle),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        SelectTeamList(conferences = state.conferences, interactor = interactor)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SelectTeamList(
    conferences: List<ConferenceGeneratorDataModel>,
    interactor: NewGameContract.Interactor
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier.background(Color.Black)
    ) {
        conferences.forEach { conference ->
            stickyHeader {
                ConferenceHeader(conference = conference)
            }
            items(conference.teams) { team ->
                TeamItem(team, interactor)
            }
        }
    }
}

@Composable
private fun ConferenceHeader(
    conference: ConferenceGeneratorDataModel
) {
    Card(
        shape = RoundedCornerShape(0.dp),
        backgroundColor = Color.LightGray,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = conference.name,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        )
    }
}

@Composable
private fun TeamItem(
    team: TeamGeneratorDataModel,
    interactor: NewGameContract.Interactor
) {
    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { interactor.onTeamClicked(team) }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.two_strings, team.schoolName, team.mascot),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(id = R.string.rating_colon, team.rating),
                    style = MaterialTheme.typography.body1,
                )
            }
            Text(
                text = team.location.value,
                style = MaterialTheme.typography.body1,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewNewGameScreen() {
    MaterialTheme(colors = lightColors) {
        NewGameScreen(
            viewStateFlow = MutableStateFlow(
                NewGameContract.DataState(
                    listOf(conferenceDataModel, conferenceDataModel)
                )
            ),
            interactor = interactor
        )
    }
}

private val teamDataModel = TeamGeneratorDataModel(
    schoolName = "Wofford",
    mascot = "Terriers",
    abbreviation = "WOF",
    location = Location.SC,
    isUser = false,
    rating = 70
)

private val conferenceDataModel = object : ConferenceGeneratorDataModel(
    name = "Southern Conference",
    teams = mutableListOf(teamDataModel, teamDataModel, teamDataModel),
    minRating = 70
) {}

private val interactor = object : NewGameContract.Interactor {
    override fun onTeamClicked(team: TeamGeneratorDataModel) {}
}
