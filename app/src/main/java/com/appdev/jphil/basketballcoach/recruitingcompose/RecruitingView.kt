package com.appdev.jphil.basketballcoach.recruitingcompose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.appdev.jphil.basketballcoach.HorizontalLine
import com.appdev.jphil.basketballcoach.LoadingScreen
import com.appdev.jphil.basketballcoach.R
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RecruitingView(
    viewStateFlow: StateFlow<RecruitingContract.ViewState>,
    interactor: RecruitingContract.Interactor
) {
    val state by viewStateFlow.collectAsState()
    RecruitingView(state = state, interactor = interactor)
}

@Composable
private fun RecruitingView(
    state: RecruitingContract.ViewState,
    interactor: RecruitingContract.Interactor
) {
    if (state.isLoading || state.team == null) {
        LoadingScreen()
    } else {
        Column {
            TeamStateView(state = state, interactor = interactor)
            HorizontalLine(color = Color.Gray)
            RecruitList(recruits = state.recruits, interactor = interactor)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TeamStateView(
    state: RecruitingContract.ViewState,
    interactor: RecruitingContract.Interactor
) {
    val model = state.team!!
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 32.dp)
    ) {
        Text(
            text = stringResource(id = R.string.returning_players_plus_commits),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Row {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (state.positionFilters.contains(1)) {
                            Color.Gray
                        } else {
                            Color.White
                        }
                    )
                    .padding(4.dp)
                    .clickable { interactor.onPositionClicked(1) }
            ) {
                Text(
                    text = stringResource(id = R.string.pg),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(id = R.string.number_and_parens, model.returningPGs, model.committedPGs),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = model.recruitingPGs.toString(),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (state.positionFilters.contains(2)) {
                            Color.Gray
                        } else {
                            Color.White
                        }
                    )
                    .padding(4.dp)
                    .clickable { interactor.onPositionClicked(2) }
            ) {
                Text(
                    text = stringResource(id = R.string.sg),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(id = R.string.number_and_parens, model.returningSGs, model.committedSGs),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = model.recruitingSGs.toString(),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (state.positionFilters.contains(3)) {
                            Color.Gray
                        } else {
                            Color.White
                        }
                    )
                    .padding(4.dp)
                    .clickable { interactor.onPositionClicked(3) }
            ) {
                Text(
                    text = stringResource(id = R.string.sf),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(id = R.string.number_and_parens, model.returningSFs, model.committedSFs),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = model.recruitingSFs.toString(),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (state.positionFilters.contains(4)) {
                            Color.Gray
                        } else {
                            Color.White
                        }
                    )
                    .padding(4.dp)
                    .clickable { interactor.onPositionClicked(4) }
            ) {
                Text(
                    text = stringResource(id = R.string.pf),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(id = R.string.number_and_parens, model.returningPFs, model.committedPFs),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = model.recruitingPFs.toString(),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (state.positionFilters.contains(5)) {
                            Color.Gray
                        } else {
                            Color.White
                        }
                    )
                    .padding(4.dp)
                    .clickable { interactor.onPositionClicked(5) }
            ) {
                Text(
                    text = stringResource(id = R.string.c),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(id = R.string.number_and_parens, model.returningCs, model.committedCs),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = model.recruitingCs.toString(),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                )
            }
        }
        AnimatedVisibility(visible = state.showClearFilters) {
            TextButton(
                onClick = { interactor.clearFilters() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Clear Filters",
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

@Composable
private fun RecruitList(
    recruits: List<RecruitModel>,
    interactor: RecruitingContract.Interactor
) {
    val types = LocalContext.current.resources.getStringArray(R.array.player_types).toList()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        itemsIndexed(recruits) { _, recruit ->
            RecruitItem(recruit = recruit, typeArray = types, interactor = interactor)
        }
    }
}

@Composable
private fun RecruitItem(
    recruit: RecruitModel,
    typeArray: List<String>,
    interactor: RecruitingContract.Interactor
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { interactor.onRecruitClicked(recruit.id) }
    ) {
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)) {
                Text(
                    text = stringResource(id = recruit.position),
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
                        text = recruit.name,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = typeArray[recruit.type],
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = stringResource(id = R.string.recruitment_level, recruit.recruitmentLevel),
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.End
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
                    Text(
                        text = stringResource(id = R.string.interest_colon, recruit.interest),
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.End
                    )
                }
            }
            Text(
                text = stringResource(id = recruit.status),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecruitingView() {
    RecruitingView(state = state, interactor = interactor)
}

private val teamModel = TeamStateModel(
    returningPGs = 2,
    committedPGs = 1,
    recruitingPGs = 1,
    returningSGs = 3,
    committedSGs = 0,
    recruitingSGs = 5,
    returningSFs = 1,
    committedSFs = 1,
    recruitingSFs = 3,
    returningPFs = 2,
    committedPFs = 0,
    recruitingPFs = 0,
    returningCs = 2,
    committedCs = 0,
    recruitingCs = 3
)

private fun recruitModel(index: Int) = RecruitModel(
    id = 0,
    name = "Steve LastName",
    position = when (index) {
        0 -> R.string.pg
        1 -> R.string.sg
        2 -> R.string.sf
        3 -> R.string.pf
        else -> R.string.c
    },
    type = index,
    rating = 85,
    potential = 75,
    interest = 55,
    recruitmentLevel = 4,
    status = R.string.empty_string
)

private val state = RecruitingContract.ViewState(
    isLoading = false,
    showClearFilters = true,
    positionFilters = listOf(2),
    team = teamModel,
    recruits = List(5) { recruitModel(it) }
)

private val interactor = object : RecruitingContract.Interactor {
    override fun onRecruitClicked(id: Int) {}
    override fun onPositionClicked(pos: Int) {}
    override fun clearFilters() {}
}
