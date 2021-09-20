package com.appdev.jphil.basketballcoach.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity

@Composable
fun CoachesView(
    coaches: List<CoachEntity>,
    interactor: TeamContract.Interactor
) {
    LazyColumn {
        item {
            Text(
                text = stringResource(id = R.string.coaches),
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
        item {
            CoachHeader()
        }
        items(coaches) {
            CoachItem(coach = it, interactor = interactor)
        }
    }
}

@Composable
private fun CoachHeader() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.pos),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(5f)
            )
            Text(
                text = stringResource(id = R.string.name),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(5f)
            )
            Text(
                text = stringResource(id = R.string.rating),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(5f)
            )
        }
        Divider()
    }
}

@Composable
private fun CoachItem(
    coach: CoachEntity,
    interactor: TeamContract.Interactor
) {
    val positions = LocalContext.current.resources.getStringArray(R.array.coach_positions)
    Column(
        modifier = Modifier.clickable { interactor.onCoachSelected(coach.id ?: -1) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = positions[coach.type.type],
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
            )
            Text(
                text = stringResource(id = R.string.two_strings, coach.firstName, coach.lastName),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = coach.rating.toString(),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
            )
        }
        Divider()
    }
}
