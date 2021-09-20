package com.appdev.jphil.basketballcoach.customizeteam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appdev.jphil.basketball.Pronouns
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import com.appdev.jphil.basketballcoach.LoadingScreen
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.theme.appLightColors
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CustomizeScreen(
    stateFlow: StateFlow<CustomizeContract.DataState>,
    interactor: CustomizeContract.Interactor
) {
    val state by stateFlow.collectAsState()
    if (state.showSpinner) {
        LoadingScreen()
    } else {
        CustomizeScreen(state = state, interactor = interactor)
    }
}

@Composable
private fun CustomizeScreen(
    state: CustomizeContract.DataState,
    interactor: CustomizeContract.Interactor
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.customize_team),
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(8.dp)
        )
        TextField(
            value = state.team.schoolName,
            label = { TextFieldLabel(textResId = R.string.school_name) },
            onValueChange = { interactor.onUpdateSchoolName(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = state.team.mascot,
            label = { TextFieldLabel(textResId = R.string.school_mascot) },
            onValueChange = { interactor.onUpdateMascot(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = state.team.abbreviation,
            label = { TextFieldLabel(textResId = R.string.school_abbreviation) },
            onValueChange = { interactor.onUpdateAbbreviation(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stringResource(id = R.string.rating_colon, state.team.rating),
                style = MaterialTheme.typography.h6,
            )

            var expanded by remember { mutableStateOf(false) }
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.weight(1f)
            ) {
                Button(
                    onClick = { expanded = true }
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.school_location,
                            state.team.location.value
                        ),
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Location.values().forEach { location ->
                        DropdownMenuItem(onClick = {
                            interactor.onUpdateLocation(location)
                            expanded = false
                        }) {
                            Text(location.value)
                        }
                    }
                }
            }
        }
        Slider(
            value = state.team.rating.toFloat(),
            onValueChange = { interactor.onUpdateTeamRating(it.roundToInt()) },
            valueRange = 0f..100f,
            steps = 100
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.customize_coach),
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(8.dp)
        )
        TextField(
            value = state.team.headCoachFirstName.orEmpty(),
            label = { TextFieldLabel(textResId = R.string.first_name) },
            onValueChange = { interactor.onUpdateCoachFirstName(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = state.team.headCoachLastName.orEmpty(),
            label = { TextFieldLabel(textResId = R.string.last_name) },
            onValueChange = { interactor.onUpdateCoachLastName(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Text(
            text = stringResource(id = R.string.preferred_pronouns),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .selectableGroup()
        ) {
            val pronouns = state.team.headCoachPronouns
            Row(
                modifier = Modifier
                    .selectable(
                        selected = pronouns == Pronouns.HE,
                        onClick = { interactor.onUpdateCoachPronouns(Pronouns.HE) },
                        role = Role.RadioButton
                    )
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.he_him),
                    modifier = Modifier.padding(end = 4.dp)
                )
                RadioButton(
                    selected = pronouns == Pronouns.HE,
                    onClick = null
                )
            }
            Row(
                modifier = Modifier
                    .selectable(
                        selected = pronouns == Pronouns.SHE,
                        onClick = { interactor.onUpdateCoachPronouns(Pronouns.SHE) },
                        role = Role.RadioButton
                    )
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.she_her),
                    modifier = Modifier.padding(end = 4.dp)
                )
                RadioButton(
                    selected = pronouns == Pronouns.SHE,
                    onClick = null
                )
            }
            Row(
                modifier = Modifier
                    .selectable(
                        selected = pronouns == Pronouns.THEY,
                        onClick = { interactor.onUpdateCoachPronouns(Pronouns.THEY) },
                        role = Role.RadioButton
                    )
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.they_their),
                    modifier = Modifier.padding(end = 4.dp)
                )
                RadioButton(
                    selected = pronouns == Pronouns.THEY,
                    onClick = null
                )
            }
        }

        Button(
            onClick = { interactor.startNewGame() },
            modifier = Modifier
                .padding(vertical = 32.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.start_new_game),
                style = MaterialTheme.typography.button,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun TextFieldLabel(textResId: Int) {
    Text(
        text = stringResource(id = textResId)
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomizePreview() {
    MaterialTheme(colors = appLightColors) {
        CustomizeScreen(
            state = state,
            interactor = interactor
        )
    }
}

private val state = CustomizeContract.DataState(
    team = TeamGeneratorDataModel(
        schoolName = "Wofford",
        mascot = "Terriers",
        abbreviation = "WOF",
        location = Location.SC,
        isUser = true,
        rating = 70
    )
)

private val interactor = object : CustomizeContract.Interactor {
    override fun onUpdateSchoolName(schoolName: String) {}
    override fun onUpdateMascot(mascot: String) {}
    override fun onUpdateAbbreviation(abbreviation: String) {}
    override fun onUpdateTeamRating(rating: Int) {}
    override fun onUpdateLocation(location: Location) {}
    override fun onUpdateCoachFirstName(firstName: String) {}
    override fun onUpdateCoachLastName(lastName: String) {}
    override fun onUpdateCoachPronouns(pronouns: Pronouns) {}
    override fun startNewGame() {}
}
