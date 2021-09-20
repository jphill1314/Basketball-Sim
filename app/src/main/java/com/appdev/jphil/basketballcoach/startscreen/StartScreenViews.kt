package com.appdev.jphil.basketballcoach.startscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.theme.appLightColors
import kotlinx.coroutines.flow.StateFlow

@Composable
fun StartScreen(
    stateFlow: StateFlow<StartScreenContract.DataState>,
    interactor: StartScreenContract.Interactor
) {
    val state by stateFlow.collectAsState()
    StartScreenView(state = state, interactor = interactor)
}

@Composable
private fun StartScreenView(
    state: StartScreenContract.DataState,
    interactor: StartScreenContract.Interactor
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.college_basketball_coach),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 72.dp, horizontal = 16.dp)
        )
        StartScreenButton(text = stringResource(id = R.string.start_new_game)) {
            interactor.onStartNewGame()
        }
        StartScreenButton(
            text = stringResource(id = R.string.load_game),
            enabled = state.showLoadGame
        ) {
            interactor.onLoadGame()
        }
    }
}

@Composable
private fun StartScreenButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .width(250.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewStartScreen() {
    MaterialTheme(
        colors = appLightColors
    ) {
        StartScreenView(state = viewState, interactor = interactor)
    }
}

private val interactor = object : StartScreenContract.Interactor {
    override fun onLoadGame() {}
    override fun onStartNewGame() {}
}

private val viewState = StartScreenContract.DataState(
    showLoadGame = true,
)
