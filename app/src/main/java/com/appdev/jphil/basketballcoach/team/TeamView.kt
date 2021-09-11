package com.appdev.jphil.basketballcoach.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketball.location.Location
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerType
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.theme.appLightColors

@Composable
private fun TeamView(
    state: TeamContract.ViewState,
    interactor: TeamContract.Interactor
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        stringResource(id = R.string.roster),
        stringResource(id = R.string.coaches),
        stringResource(id = R.string.strategy)
    )
    Column {
        TabRow(
            selectedTabIndex = selectedTab
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = index == selectedTab,
                    onClick = { selectedTab = index }
                )
            }
        }
        if (state.team != null) {
            when (selectedTab) {
                0 -> RosterView(
                    team = state.team,
                    selectedPlayerIndex = state.selectedPlayerIndex,
                    interactor = interactor
                )
            }
        }
    }
}

@Composable
private fun RosterView(
    team: Team,
    selectedPlayerIndex: Int,
    interactor: TeamContract.Interactor
) {
    LazyColumn {
        item {
            RosterTitle(title = stringResource(id = R.string.starting_lineup))
        }
        items(team.roster.take(5)) { player ->

        }
        item {
            RosterTitle(title = stringResource(id = R.string.bench))
        }
        items(team.roster.drop(5)) { player ->

        }
    }
}

@Composable
private fun RosterTitle(title: String) {
    Card(shape = RoundedCornerShape(0.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RosterPlayer(player: Player, isSelected: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {

    }
}

@Preview
@Composable
private fun PreviewTeamView() {
    MaterialTheme(colors = appLightColors) {
        TeamView(
            state,
            interactor
        )
    }
}

private fun getPlayer(index: Int) = Player(
    id = index,
    teamId = 0,
    firstName = "Player",
    lastName = index.toString(),
    position = index % 5 + 1,
    year = index % 4,
    type = PlayerType.SHOOTER,
    isOnScholarship = true,
    closeRangeShot = 90,
    midRangeShot = 90,
    longRangeShot = 90,
    freeThrowShot = 90,
    postMove = 90,
    ballHandling = 90,
    passing = 90,
    offBallMovement = 90,
    postDefense = 90,
    perimeterDefense = 90,
    onBallDefense = 90,
    offBallDefense = 90,
    stealing = 90,
    rebounding = 90,
    stamina = 90,
    aggressiveness = 90,
    potential = 90,
    rosterIndex = index,
    courtIndex = index
)

private fun getCoach(index: Int) = Coach(
    id = index,
    teamId = 0,
    type = if (index == 0) CoachType.HEAD_COACH else CoachType.ASSISTANT_COACH,
    firstName = "Coach",
    lastName = index.toString(),
    recruiting = 90,
    offenseFavorsThrees = 90,
    pace = 90,
    aggression = 90,
    defenseFavorsThrees = 90,
    pressFrequency = 0,
    pressAggression = 0,
    offenseFavorsThreesGame = 90,
    paceGame = 90,
    aggressionGame = 90,
    defenseFavorsThreesGame = 90,
    pressFrequencyGame = 0,
    pressAggressionGame = 0,
    intentionallyFoul = false,
    shouldHurry = false,
    shouldWasteTime = false,
    teachShooting = 90,
    teachBallControl = 90,
    teachConditioning = 90,
    teachPerimeterDefense = 90,
    teachPositioning = 90,
    teachPostDefense = 90,
    teachPostMoves = 90,
    teachRebounding = 90
)

private val interactor = object : TeamContract.Interactor {
    override fun onPlayerSelected(index: Int) {}
}

private val state = TeamContract.ViewState(
    team = Team(
        teamId = 0,
        schoolName = "Wofford",
        mascot = "Terriers",
        abbreviation = "Wof",
        color = TeamColor.Yellow,
        players = MutableList(15) { getPlayer(it) },
        conferenceId = 1,
        isUser = true,
        coaches = MutableList(4) { getCoach(it) },
        location = Location.SC,
        prestige = 80,
        gamesPlayed = 1,
        postSeasonTournamentId = -1,
        postSeasonTournamentSeed = -1
    ),
    selectedPlayerIndex = 0
)
