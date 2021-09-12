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
import com.appdev.jphil.basketballcoach.theme.lightColors
import kotlinx.coroutines.flow.StateFlow

@Composable
fun StartScreen(
    stateFlow: StateFlow<StartScreenContract.ViewState>,
    interactor: StartScreenContract.Interactor
) {
    val state by stateFlow.collectAsState()
    StartScreenView(state = state, interactor = interactor)
}

@Composable
private fun StartScreenView(
    state: StartScreenContract.ViewState,
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
        if (state.showLoadGame) {
            StartScreenButton(text = stringResource(id = R.string.load_game)) {
                interactor.onLoadGame()
            }
        }
    }
}

@Composable
private fun StartScreenButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
                .width(136.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewStartScreen() {
    MaterialTheme(
        colors = lightColors
    ) {
        StartScreenView(state = viewState, interactor = interactor)
    }
}

private val interactor = object : StartScreenContract.Interactor {
    override fun onLoadGame() {}
    override fun onStartNewGame() {}
}

private val viewState = StartScreenContract.ViewState(
    showLoadGame = true,
)
