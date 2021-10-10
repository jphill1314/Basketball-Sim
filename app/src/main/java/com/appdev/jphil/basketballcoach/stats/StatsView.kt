package com.appdev.jphil.basketballcoach.stats

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appdev.jphil.basketballcoach.LoadingScreen
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.theme.appLightColors
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

@Composable
fun StatsView(
    stateFlow: StateFlow<StatsContract.ViewState>,
    interactor: StatsContract.Interactor
) {
    val state by stateFlow.collectAsState()
    if (state.isLoading) {
        LoadingScreen()
    } else {
        StatsView(state, interactor)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StatsView(
    state: StatsContract.ViewState,
    interactor: StatsContract.Interactor
) {
    val tabs = listOf(
        stringResource(id = R.string.standings),
        stringResource(id = R.string.rankings)
    )
    Column {
        TabRow(selectedTabIndex = state.tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = index == state.tabIndex,
                    onClick = { interactor.onTabChanged(index) }
                )
            }
        }

        if (state.dropdownOptions.isNotEmpty()) {
            var expanded by remember { mutableStateOf(false) }
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { expanded = true },
                            role = Role.Button
                        )
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = state.conferenceName,
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 48.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    state.dropdownOptions.forEachIndexed { index, title ->
                        DropdownMenuItem(
                            onClick = {
                                interactor.onConferenceChanged(index)
                                expanded = false
                            }
                        ) {
                            Text(title)
                        }
                    }
                }
            }
        }
        val horizontalScrollState = rememberScrollState()
        LazyColumn {
            itemsIndexed(state.uiModels) { index, item ->
                when (item) {
                    is StandingsHeader -> StandingsHeader()
                    is TeamStandingModel -> TeamStandingItem(
                        index = index,
                        team = item,
                        interactor = interactor
                    )
                    is RankingsHeader -> RankingsHeader(Modifier.horizontalScroll(horizontalScrollState))
                    is TeamRankingModel -> TeamRankingItem(
                        team = item,
                        index = index,
                        interactor = interactor,
                        modifier = Modifier.horizontalScroll(horizontalScrollState)
                    )
                }
            }
        }
    }
}

@Composable
private fun StandingsHeader() {
    Column {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.pos),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(3f)
            )
            Text(
                text = stringResource(id = R.string.team),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(10f)
            )
            Text(
                text = stringResource(id = R.string.conf_w_l),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(6f)
            )
            Text(
                text = stringResource(id = R.string.w_l),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(3f)
            )
        }
        Divider()
    }
}

@Composable
private fun TeamStandingItem(
    index: Int,
    team: TeamStandingModel,
    interactor: StatsContract.Interactor
) {
    Column(modifier = Modifier.clickable { interactor.onTeamClicked(team.teamId) }) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = index.toString(),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(3f)
            )
            Text(
                text = team.teamName,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(10f)
            )
            Text(
                text = stringResource(id = R.string.standings_dash, team.confWins, team.confLoses),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(6f)
            )
            Text(
                text = stringResource(id = R.string.standings_dash, team.wins, team.loses),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(3f)
            )
        }
        Divider()
    }
}

@Composable
private fun RankingsHeader(
    modifier: Modifier
) {
    Column {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
            Text(
                text = stringResource(id = R.string.team),
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .width(170.dp)
                    .padding(start = 20.dp, end = 8.dp)
            )
            Row(modifier = modifier) {
                Text(
                    text = stringResource(id = R.string.adj_eff),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.pace),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.adj_off),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.adj_def),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(end = 8.dp)
                )
            }
        }
        Divider()
    }
}

@Composable
private fun TeamRankingItem(
    team: TeamRankingModel,
    index: Int,
    interactor: StatsContract.Interactor,
    modifier: Modifier
) {
    Column {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(end = 8.dp)
        ) {
            Text(
                text = index.toString(),
                color = Color.LightGray,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .width(28.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = team.teamName,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .width(150.dp)
                    .padding(end = 8.dp)
                    .clickable { interactor.onTeamClicked(team.teamId) }
            )
            Row(modifier = modifier) {
                Text(
                    text = formatDouble(team.eff),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = formatDouble(team.pace),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = formatDouble(team.offEff),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = formatDouble(team.defEff),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(100.dp)
                        .padding(end = 8.dp)
                )
            }
        }
        Divider()
    }
}

private fun formatDouble(value: Double): String {
    return "%.2f".format(Locale.getDefault(), value)
}

@Preview(showBackground = true)
@Composable
private fun StandingPreview() {
    MaterialTheme(colors = appLightColors) {
        StatsView(state = standingsState, interactor = interactor)
    }
}

@Preview(showBackground = true)
@Composable
private fun RankingsPreview() {
    MaterialTheme(colors = appLightColors) {
        StatsView(state = rankingState, interactor = interactor)
    }
}

private val interactor = object : StatsContract.Interactor {
    override fun onTeamClicked(teamId: Int) {}
    override fun onConferenceChanged(index: Int) {}
    override fun onTabChanged(index: Int) {}
}

private fun getStandingModel(index: Int) = TeamStandingModel(
    teamId = index,
    teamName = "Team $index",
    confWins = 10 - index,
    confLoses = index,
    wins = 10 - index,
    loses = index
)

private fun getRankingModel(index: Int) = TeamRankingModel(
    teamId = index,
    teamName = "Team $index",
    eff = 10.0,
    pace = 69.5,
    offEff = 100.0,
    defEff = 90.0
)

private val standingsState = StatsContract.ViewState(
    isLoading = false,
    tabIndex = 0,
    uiModels = listOf(StandingsHeader) + List(10) { getStandingModel(it) },
    conferenceName = "Test Conference",
    dropdownOptions = listOf("Conf 1", "Conf 2")
)

private val rankingState = StatsContract.ViewState(
    isLoading = false,
    tabIndex = 1,
    uiModels = listOf(RankingsHeader) + List(10) { getRankingModel(it) },
    conferenceName = "",
    dropdownOptions = emptyList()
)
