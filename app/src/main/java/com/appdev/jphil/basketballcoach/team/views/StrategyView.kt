package com.appdev.jphil.basketballcoach.team

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.theme.appLightColors
import kotlin.math.roundToInt

@Composable
fun StrategyView(
    headCoach: CoachEntity,
    isInGame: Boolean,
    interactor: StrategyInteractor
) {
    LazyColumn {
        if (isInGame) {
            item {
                StrategyToggle(
                    title = stringResource(id = R.string.intentionally_foul),
                    isActive = headCoach.intentionallyFoul,
                    onUpdate = interactor::onIntentionallyFoulToggled
                )
            }
            item {
                StrategyToggle(
                    title = stringResource(id = R.string.move_quickly),
                    isActive = headCoach.shouldHurry,
                    onUpdate = interactor::onMoveQuicklyToggled
                )
            }
            item {
                StrategyToggle(
                    title = stringResource(id = R.string.waste_time),
                    isActive = headCoach.shouldWasteTime,
                    onUpdate = interactor::onWasteTimeToggled
                )
            }
        }

        item {
            StrategySlider(
                title = stringResource(id = R.string.pace),
                higher = stringResource(id = R.string.faster),
                lower = stringResource(id = R.string.slower),
                value = if (isInGame) headCoach.paceGame else headCoach.pace,
                onUpdate = interactor::onPaceChanged
            )
        }
        item {
            StrategySlider(
                title = stringResource(id = R.string.aggression),
                higher = stringResource(id = R.string.more_aggressive),
                lower = stringResource(id = R.string.less_aggressive),
                value = if (isInGame) headCoach.aggressionGame else headCoach.aggression,
                onUpdate = interactor::onAggressionChanged
            )
        }
        item {
            StrategySlider(
                title = stringResource(id = R.string.offense_favors_threes),
                higher = stringResource(id = R.string.more_threes),
                lower = stringResource(id = R.string.fewer_threes),
                value = if (isInGame) headCoach.offenseFavorsThreesGame else headCoach.offenseFavorsThrees,
                onUpdate = interactor::onOffenseFavorsThreesChanged
            )
        }
        item {
            StrategySlider(
                title = stringResource(id = R.string.defense_favors_threes),
                higher = stringResource(id = R.string.more_threes),
                lower = stringResource(id = R.string.fewer_threes),
                value = if (isInGame) headCoach.defenseFavorsThreesGame else headCoach.defenseFavorsThrees,
                onUpdate = interactor::onDefenseFavorsThreesChanged
            )
        }
        item {
            StrategySlider(
                title = stringResource(id = R.string.press_frequency),
                higher = stringResource(id = R.string.always_press),
                lower = stringResource(id = R.string.never_press),
                value = if (isInGame) headCoach.pressFrequencyGame else headCoach.pressFrequency,
                onUpdate = interactor::onPressFrequencyChanged
            )
        }
        item {
            StrategySlider(
                title = stringResource(id = R.string.press_aggression),
                higher = stringResource(id = R.string.more_aggressive),
                lower = stringResource(id = R.string.less_aggressive),
                value = if (isInGame) headCoach.pressAggressionGame else headCoach.pressAggression,
                onUpdate = interactor::onPressAggressionChanged
            )
        }
    }
}

@Composable
private fun StrategySlider(
    title: String,
    lower: String,
    higher: String,
    value: Int,
    maxValue: Int = 100,
    onUpdate: (Int) -> Unit
) {
    var sliderValue by remember { mutableStateOf(value.toFloat()) }
    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)) {
            Text(
                text = lower,
                style = MaterialTheme.typography.h6
            )
            Text(
                text = higher,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            onValueChangeFinished = { onUpdate(sliderValue.roundToInt()) },
            valueRange = 0f..maxValue.toFloat(),
            steps = maxValue,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Divider(modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
private fun StrategyToggle(
    title: String,
    isActive: Boolean,
    onUpdate: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                role = Role.Switch,
                onClick = { onUpdate(!isActive) }
            )
            .padding(top = 16.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isActive,
                onCheckedChange = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Divider(modifier = Modifier.padding(top = 16.dp))
    }
}

@Preview
@Composable
private fun StrategySliderPreview() {
    MaterialTheme(colors = appLightColors) {
        StrategySlider(
            title = "Offense Favors Threes",
            lower = "Fewer Threes",
            higher = "More Threes",
            value = 55,
            onUpdate = { }
        )
    }
}

@Preview
@Composable
private fun StrategyTogglePreview() {
    MaterialTheme(colors = appLightColors) {
        StrategyToggle(title = "Intentionally Foul", isActive = false, onUpdate = {})
    }
}
