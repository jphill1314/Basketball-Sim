package com.appdev.jphil.basketballcoach.team

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity

@Composable
fun RosterView(
    players: List<PlayerEntity>,
    selectedPlayerIndex: Int,
    interactor: TeamContract.Interactor
) {
    LazyColumn {
        item {
            RosterTitle(title = stringResource(id = R.string.starting_lineup))
        }
        item {
            RosterHeading()
        }
        item {
            Divider()
        }
        itemsIndexed(players.take(5)) { index, player ->
            RosterPlayer(
                player = player,
                position = index + 1,
                isSelected = selectedPlayerIndex == player.rosterIndex,
                interactor = interactor
            )
        }
        item {
            RosterTitle(title = stringResource(id = R.string.bench))
        }
        item {
            RosterHeading()
        }
        item {
            Divider()
        }
        items(players.drop(5)) { player ->
            RosterPlayer(
                player = player,
                position = player.position,
                isSelected = selectedPlayerIndex == player.rosterIndex,
                interactor = interactor
            )
        }
    }
}

@Composable
private fun RosterTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h5,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    )
}

@Composable
private fun RosterHeading() {
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
                .weight(3f)
        )
        Text(
            text = stringResource(id = R.string.name),
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .weight(10f)
        )
        Text(
            text = stringResource(id = R.string.rating),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(4f)
        )
        Text(
            text = stringResource(id = R.string.year),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(3f)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RosterPlayer(
    player: PlayerEntity,
    position: Int,
    isSelected: Boolean,
    interactor: TeamContract.Interactor
) {
    val textColor = if (isSelected) Color.LightGray else Color.Black
    val resources = LocalContext.current.resources
    val positions = resources.getStringArray(R.array.position_abbreviation)
    val year = resources.getStringArray(R.array.years)
    Column(
        modifier = Modifier.combinedClickable(
            onClick = { interactor.onPlayerSelected(player.rosterIndex) },
            onLongClick = { interactor.onPlayerLongPressed(player.id ?: -1) }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = positions[position - 1],
                style = MaterialTheme.typography.body1,
                color = textColor,
                modifier = Modifier.padding(end = 8.dp)
                    .weight(3f)
            )
            Text(
                text = stringResource(id = R.string.two_strings, player.firstName, player.lastName),
                style = MaterialTheme.typography.body1,
                color = textColor,
                modifier = Modifier.weight(10f)
            )
            Text(
                text = player.rating.toString(),
                style = MaterialTheme.typography.body1,
                color = textColor,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(end = 8.dp)
                    .weight(4f)
            )
            Text(
                text = year[player.year],
                color = textColor,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(3f)
            )
        }
        Divider()
    }
}
