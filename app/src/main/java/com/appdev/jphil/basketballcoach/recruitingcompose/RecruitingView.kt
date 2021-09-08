package com.appdev.jphil.basketballcoach.recruitingcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(75.dp))
        }
    } else {
        Column {
            TeamStateView(model = state.team)
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.Gray))
            RecruitList(recruits = state.recruits, interactor = interactor)
        }
    }
}

@Composable
private fun TeamStateView(
    model: TeamStateModel
) {
    Column(
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
            Text(
                text = stringResource(id = R.string.pg),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.sg),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.sf),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.pf),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.c),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
        Row {
            Text(
                text = stringResource(id = R.string.number_and_parens, model.returningPGs, model.committedPGs),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.number_and_parens, model.returningSGs, model.committedSGs),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.number_and_parens, model.returningSFs, model.committedSFs),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.number_and_parens, model.returningPFs, model.committedPFs),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.number_and_parens, model.returningCs, model.committedCs),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
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
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(
                text = stringResource(id = recruit.position),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp).width(24.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
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
                    text = stringResource(id = R.string.interest_colon, recruit.interest),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.End
                )
            }
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
    returningSGs = 3,
    committedSGs = 0,
    returningSFs = 1,
    committedSFs = 1,
    returningPFs = 2,
    committedPFs = 0,
    returningCs = 2,
    committedCs = 0
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
    status = ""
)

private val state = RecruitingContract.ViewState(
    isLoading = false,
    team = teamModel,
    recruits = List(5) { recruitModel(it) }
)

private val interactor = object : RecruitingContract.Interactor {
    override fun onRecruitClicked(id: Int) {}
}
