package com.appdev.jphil.basketballcoach.recruitoverviewcompose

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.location.Location
import com.appdev.jphil.basketball.players.PlayerType
import com.appdev.jphil.basketball.recruits.NewRecruitInterest
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.HorizontalLine
import com.appdev.jphil.basketballcoach.LoadingScreen
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.toPositionString
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RecruitOverview(
    stateFlow: StateFlow<RecruitOverviewContract.ViewState>,
    interactor: RecruitOverviewContract.Interactor
) {
    val state by stateFlow.collectAsState()
    val recruit = state.recruit
    val interest = state.recruitInterest
    if (state.isLoading || recruit == null || interest == null) {
        LoadingScreen()
    } else {
        RecruitOverview(state.teamName, state.isActivelyRecruited, recruit, interest, interactor)
        if (state.coaches.isNotEmpty()) {
            CoachDialog(
                coaches = state.coaches,
                selectedCoach = state.coachForDialog,
                interactor = interactor
            )
        }
    }
}

@Composable
private fun RecruitOverview(
    teamName: String,
    isActivelyRecruited: Boolean,
    recruit: Recruit,
    recruitInterest: NewRecruitInterest,
    interactor: RecruitOverviewContract.Interactor
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        RecruitTopper(recruit = recruit)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalLine(color = Color.Gray)
            RecruitInterestLevel(teamName = teamName, level = 1, recruitInterest = recruitInterest)
            HorizontalLine(color = Color.Gray)
            RecruitInterestLevel(teamName = teamName, level = 2, recruitInterest = recruitInterest)
            HorizontalLine(color = Color.Gray)
            RecruitInterestLevel(teamName = teamName, level = 3, recruitInterest = recruitInterest)
            HorizontalLine(color = Color.Gray)
            RecruitInterestLevel(teamName = teamName, level = 4, recruitInterest = recruitInterest)
            HorizontalLine(color = Color.Gray)
            RecruitInterestLevel(teamName = teamName, level = 5, recruitInterest = recruitInterest)
            HorizontalLine(color = Color.Gray)
            RecruitInterestLevel(teamName = teamName, level = 0, recruitInterest = recruitInterest)
            HorizontalLine(color = Color.Gray)
        }
        TotalInterest(totalInterest = recruitInterest.getInterest())
        RecruitInteractions(isActivelyRecruited = isActivelyRecruited, interactor = interactor)
    }
}

@Composable
private fun RecruitTopper(
    recruit: Recruit,
) {
    val types = LocalContext.current.resources.getStringArray(R.array.player_types).toList()
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = recruit.position.toPositionString(),
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(8.dp)
                .width(24.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                text = recruit.fullName,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = types[recruit.playerType.type],
                style = MaterialTheme.typography.body1
            )
        }
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = stringResource(id = R.string.rating_colon, recruit.rating),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.End
            )
            Text(
                text = stringResource(id = R.string.potential_color, recruit.potential),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun RecruitInterestLevel(
    teamName: String,
    level: Int,
    recruitInterest: NewRecruitInterest
) {
    val interest: Int?
    val title: String
    val subtitle: String

    when (level) {
        1 -> {
            interest = recruitInterest.prestigeInterest
            title = "Team Prestige ($interest) - ${recruitInterest.preferredPrestige}"
            subtitle = when (interest) {
                null -> stringResource(id = R.string.recruit_to_see)
                0 -> stringResource(id = R.string.prestige_miss, teamName)
                in NewRecruitInterest.MAX_DESIRE..NewRecruitInterest.MAX_DESIRE_FROM_PRESTIGE -> stringResource(id = R.string.prestige_match, teamName)
                else -> stringResource(id = R.string.prestige_close)
            }
        }
        2 -> {
            interest = recruitInterest.locationInterest
            title = "Location ($interest)"
            subtitle = when (recruitInterest.wantsClose) {
                true -> when (interest) {
                    null -> stringResource(id = R.string.recruit_to_see)
                    0 -> stringResource(id = R.string.location_miss_near)
                    NewRecruitInterest.MAX_DESIRE -> stringResource(id = R.string.location_match_near)
                    else -> stringResource(id = R.string.location_close_near, teamName)
                }
                false -> when (recruitInterest.wantsFar) {
                    true -> when (interest) {
                        null -> stringResource(id = R.string.recruit_to_see)
                        0 -> stringResource(id = R.string.location_miss_far)
                        NewRecruitInterest.MAX_DESIRE -> stringResource(id = R.string.location_match_far)
                        else -> stringResource(id = R.string.location_close_far, teamName)
                    }
                    false -> when (interest) {
                        null -> stringResource(id = R.string.recruit_to_see)
                        else -> stringResource(id = R.string.location_unimportant)
                    }
                }
            }
        }
        3 -> {
            interest = recruitInterest.playingTimeInterest
            title = "Playing Time ($interest)"
            subtitle = when (recruitInterest.wantsImmediateStart) {
                true -> when (interest) {
                    null -> stringResource(id = R.string.recruit_to_see)
                    NewRecruitInterest.MAX_DESIRE -> stringResource(id = R.string.playing_time_start_hit, teamName)
                    else -> stringResource(id = R.string.playing_time_start_miss, teamName)
                }
                false -> when (recruitInterest.wantsToDevelop) {
                    true -> when (interest) {
                        null -> stringResource(id = R.string.recruit_to_see)
                        NewRecruitInterest.MAX_DESIRE -> stringResource(id = R.string.playing_time_develop_hit)
                        else -> stringResource(id = R.string.playing_time_develop_miss)
                    }
                    false -> when (interest) {
                        null -> stringResource(id = R.string.recruit_to_see)
                        else -> stringResource(id = R.string.playing_time_unimportant)
                    }
                }
            }
        }
        4 -> {
            interest = recruitInterest.playStyleInterest
            title = "Play Style ($interest)"
            subtitle = when (interest) {
                null -> stringResource(id = R.string.recruit_to_see)
                in 0..NewRecruitInterest.MAX_DESIRE / 4 -> stringResource(id = R.string.play_style_miss, teamName)
                in NewRecruitInterest.MAX_DESIRE / 4 * 3 .. NewRecruitInterest.MAX_DESIRE ->
                    stringResource(id = R.string.play_style_hit, teamName)
                else -> stringResource(id = R.string.play_style_middle, teamName)
            }
        }
        5 -> {
            interest = recruitInterest.teamAbilityInterest
            title = "Team Ability ($interest)"
            subtitle = when (recruitInterest.wantsToBeStar) {
                true -> when (interest) {
                    null -> stringResource(id = R.string.recruit_to_see)
                    NewRecruitInterest.MAX_DESIRE -> stringResource(id = R.string.team_ability_star_hit, teamName)
                    else -> stringResource(id = R.string.team_ability_star_miss)
                }
                false -> when (recruitInterest.wantsToDevelop) {
                    true -> when (interest) {
                        null -> stringResource(id = R.string.recruit_to_see)
                        0 -> stringResource(id = R.string.team_ability_develop_miss, teamName)
                        NewRecruitInterest.MAX_DESIRE -> stringResource(id = R.string.team_ability_develop_hit, teamName)
                        else -> stringResource(id = R.string.team_ability_develop_close, teamName)
                    }
                    false -> when (interest) {
                        null -> stringResource(id = R.string.recruit_to_see)
                        else -> stringResource(id = R.string.team_ability_unimportant)
                    }
                }
            }
        }
        else -> {
            interest = recruitInterest.recruitmentInterest
            title = "Recruiting Pitch ($interest)"
            subtitle = when  {
                interest > 0 -> stringResource(id = R.string.active_recruitment_positive)
                interest < 0 -> stringResource(id = R.string.active_recruitment_negative)
                else -> stringResource(id = R.string.recruit_to_see)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (level == 0 && interest != null) {
                    interest.getColorForPitch()
                } else {
                    interest.getColorForInterest()
                }
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun TotalInterest(
    totalInterest: Int
) {
    Text(
        text = stringResource(id = R.string.total_interest, totalInterest, 100),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h6,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

private fun Int?.getColorForInterest() = when (this) {
    // TODO: pick less offensive colors lol
    in NewRecruitInterest.MAX_DESIRE..NewRecruitInterest.MAX_DESIRE_FROM_PRESTIGE -> Color.Green
    0 -> Color.Red
    null -> Color.LightGray
    else -> Color.Yellow
}

private fun Int.getColorForPitch() = when {
    this > 0 -> Color.Green
    this < 0 -> Color.Red
    else -> Color.LightGray
}

@Composable
private fun RecruitInteractions(
    isActivelyRecruited: Boolean,
    interactor: RecruitOverviewContract.Interactor
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isActivelyRecruited) {
            TextButton(
                onClick = { interactor.stopActiveRecruitment() },
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.stop_recruiting),
                    style = MaterialTheme.typography.button
                )
            }
        } else {
            TextButton(
                onClick = { interactor.startActiveRecruitment() },
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.start_recruiting),
                    style = MaterialTheme.typography.button
                )
            }
        }
        TextButton(
            onClick = { interactor.gainCommitment() },
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.get_commitment),
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
private fun CoachDialog(
    coaches: List<Coach>,
    selectedCoach: Coach?,
    interactor: RecruitOverviewContract.Interactor
) {
    Dialog(onDismissRequest = { interactor.onDialogDismissed() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .background(Color.White)
        ) {
            Column {
                if (selectedCoach != null) {
                    Text(
                        text = stringResource(id = R.string.select_recruit_to_no_longer_recruit),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(8.dp)
                    )
                    CoachRow(coach = selectedCoach, interactor = null)
                    HorizontalLine(color = Color.Gray)
                    selectedCoach.recruitingAssignments.forEach {
                        HorizontalLine(color = Color.Gray)
                        RecruitRow(recruit = it, coach = selectedCoach, interactor = interactor)
                    }
                } else {
                    Text(
                        text = stringResource(id = R.string.select_coach_to_recruit),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(8.dp)
                    )
                    coaches.forEach {
                        HorizontalLine(color = Color.Gray)
                        CoachRow(coach = it, interactor = interactor)
                    }
                }
            }
        }
    }
}

@Composable
private fun CoachRow(
    coach: Coach,
    interactor: RecruitOverviewContract.Interactor?
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { interactor?.selectCoachForRecruitment(coach) }
        ) {
            Text(
                text = coach.fullName,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f)
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(id = R.string.recruiting_colon, coach.recruiting),
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = stringResource(id = R.string.coach_assignments, coach.recruitingAssignments.size),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Composable
private fun RecruitRow(
    recruit: Recruit,
    coach: Coach,
    interactor: RecruitOverviewContract.Interactor
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                interactor.startRecruitmentWithCoach(
                    coach = coach,
                    recruitToRemove = recruit
                )
            }
        ) {
            Text(
                text = recruit.fullName,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f)
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(id = R.string.rating_colon, recruit.rating),
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = stringResource(id = R.string.interest_colon, recruit.getInterest(coach.teamId)),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecruitOverview() {
    RecruitOverview(viewState.teamName, viewState.isActivelyRecruited, recruit, recruitInterest, interactor)
}

private val recruitInterest = NewRecruitInterest(
    id = null,
    teamId = 1,
    preferredPrestige = 70,
    wantsClose = false,
    wantsFar = true,
    wantsImmediateStart = true,
    wantsToDevelop = false,
    wantsThrees = true,
    wantsPress = false,
    wantsAggressive = false,
    wantsToBeStar = false,
    prestigeInterest = 20,
    locationInterest = 10,
    playingTimeInterest = 0,
    playStyleInterest = null,
    teamAbilityInterest = null,
    recruitmentInterest = 10
)

private val recruit = Recruit(
    id = 1,
    firstName = "Steve",
    lastName = "Testsubject",
    position = 1,
    playerType = PlayerType.SHOOTER,
    rating = 85,
    potential = 70,
    isCommitted = false,
    teamCommittedTo = -1,
    location = Location.HI,
    recruitInterests = mutableListOf(recruitInterest)
)

private val viewState = RecruitOverviewContract.ViewState(
    isLoading = false,
    teamName = "Boston",
    recruit = recruit,
    recruitInterest = recruitInterest
)

private val interactor = object : RecruitOverviewContract.Interactor {
    override fun startActiveRecruitment() {}
    override fun stopActiveRecruitment() {}
    override fun gainCommitment() {}
    override fun selectCoachForRecruitment(coach: Coach) {}
    override fun startRecruitmentWithCoach(coach: Coach, recruitToRemove: Recruit) {}
    override fun onDialogDismissed() {}
}
