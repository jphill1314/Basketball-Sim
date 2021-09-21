package com.appdev.jphil.basketballcoach.team.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.appdev.jphil.basketball.Pronouns
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketball.location.Location
import com.appdev.jphil.basketball.players.PlayerType
import com.appdev.jphil.basketball.players.PracticeType
import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity
import com.appdev.jphil.basketballcoach.team.TeamContract
import com.appdev.jphil.basketballcoach.theme.appLightColors
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TeamView(
    stateFlow: StateFlow<TeamContract.DataState>,
    interactor: TeamContract.Interactor
) {
    val state by stateFlow.collectAsState()
    TeamView(state, interactor)
}

@Composable
private fun TeamView(
    state: TeamContract.DataState,
    interactor: TeamContract.Interactor
) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    val tabs = listOfNotNull(
        stringResource(id = R.string.roster),
        stringResource(id = R.string.coaches),
        if (state.team?.isUser == true) stringResource(id = R.string.strategy) else null
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
                    players = state.players,
                    selectedPlayerIndex = state.selectedPlayerIndex,
                    interactor = interactor
                )
                1 -> CoachesView(
                    coaches = state.coaches,
                    interactor = interactor
                )
                2 -> StrategyView(
                    headCoach = state.coaches.first { it.type == CoachType.HEAD_COACH },
                    interactor = interactor,
                    isInGame = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTeamView() {
    MaterialTheme(colors = appLightColors) {
        TeamView(
            state,
            interactor
        )
    }
}

private fun getPlayer(index: Int) = PlayerEntity(
    id = index,
    teamId = 0,
    firstName = "Player",
    lastName = index.toString(),
    position = index % 5 + 1,
    rating = 90,
    year = index % 4,
    type = PlayerType.SHOOTER.type,
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
    courtIndex = index,
    pronouns = Pronouns.HE,
    offensiveStatMod = 0,
    defensiveStatMod = 0,
    fatigue = 0.0,
    timePlayed = 0,
    inGame = false,
    twoPointAttempts = 0,
    twoPointMakes = 0,
    threePointAttempts = 0,
    threePointMakes = 0,
    assists = 0,
    offensiveRebounds = 0,
    defensiveRebounds = 0,
    turnovers = 0,
    steals = 0,
    fouls = 0,
    freeThrowShots = 0,
    freeThrowMakes = 0,
)

private fun getCoach(index: Int) = CoachEntity(
    id = index,
    teamId = 0,
    type = if (index == 0) CoachType.HEAD_COACH else CoachType.ASSISTANT_COACH,
    firstName = "Coach",
    lastName = index.toString(),
    rating = 90,
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
    teachRebounding = 90,
    recruitIds = emptyList(),
    pronouns = Pronouns.HE
)

private val interactor = object : TeamContract.Interactor {
    override fun onPlayerSelected(index: Int) {}
    override fun onPlayerLongPressed(playerId: Int) {}
    override fun onCoachSelected(coachId: Int) {}
    override fun onPaceChanged(pace: Int) {}
    override fun onOffenseFavorsThreesChanged(favorsThrees: Int) {}
    override fun onAggressionChanged(aggression: Int) {}
    override fun onDefenseFavorsThreesChanged(favorsThrees: Int) {}
    override fun onPressFrequencyChanged(frequency: Int) {}
    override fun onPressAggressionChanged(aggression: Int) {}
    override fun onIntentionallyFoulToggled(isChecked: Boolean) {}
    override fun onMoveQuicklyToggled(isChecked: Boolean) {}
    override fun onWasteTimeToggled(isChecked: Boolean) {}
}

private val state = TeamContract.DataState(
    team = TeamEntity(
        teamId = 0,
        schoolName = "Wofford",
        mascot = "Terriers",
        abbreviation = "Wof",
        color = TeamColor.Yellow.type,
        conferenceId = 1,
        isUser = true,
        location = Location.SC,
        prestige = 80,
        gamesPlayed = 1,
        postseasonTournamentId = -1,
        postseasonTournamentSeed = -1,
        twoPointAttempts = 0,
        twoPointMakes = 0,
        threePointAttempts = 0,
        threePointMakes = 0,
        offensiveRebounds = 0,
        defensiveRebounds = 0,
        turnovers = 0,
        offensiveFouls = 0,
        defensiveFouls = 0,
        freeThrowShots = 0,
        freeThrowMakes = 0,
        lastScoreDif = 0,
        practiceType = PracticeType.NO_FOCUS,
        rating = 80,
        commitments = emptyList()
    ),
    players = List(15) { getPlayer(it) },
    coaches = List(4) { getCoach(it) },
    selectedPlayerIndex = 8
)
