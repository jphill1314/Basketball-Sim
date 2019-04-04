package com.appdev.jphil.basketballcoach.strategy

import android.content.res.Resources
import com.appdev.jphil.basketball.Coach
import com.appdev.jphil.basketballcoach.R

data class StrategyDataModel(
    var type: StrategyType,
    var title: String,
    var higher: String,
    var lower: String,
    var value: Int,
    val max: Int = 100
) {

    companion object {
        fun generateDataModels(coach: Coach, resources: Resources, isInGame: Boolean): List<StrategyDataModel> {
            return listOf(
                StrategyDataModel(
                    StrategyType.PACE,
                    resources.getString(R.string.pace),
                    resources.getString(R.string.faster),
                    resources.getString(R.string.slower),
                    if (isInGame) coach.paceGame - Coach.minimumPace else coach.pace - Coach.minimumPace,
                    30
                ),
                StrategyDataModel(
                    StrategyType.AGGRESSION,
                    resources.getString(R.string.aggression),
                    resources.getString(R.string.more_aggressive),
                    resources.getString(R.string.less_aggressive),
                    if (isInGame) coach.aggressionGame else coach.aggression
                ),
                StrategyDataModel(
                    StrategyType.OFFENSE_FAVORS_THREES,
                    resources.getString(R.string.offense_favors_threes),
                    resources.getString(R.string.more_threes),
                    resources.getString(R.string.fewer_threes),
                    if (isInGame) coach.offenseFavorsThreesGame else coach.offenseFavorsThrees
                ),
                StrategyDataModel(
                    StrategyType.DEFENSE_FAVORS_THREES,
                    resources.getString(R.string.defense_favors_threes),
                    resources.getString(R.string.more_threes),
                    resources.getString(R.string.fewer_threes),
                    if (isInGame) coach.defenseFavorsThreesGame else coach.defenseFavorsThrees
                ),
                StrategyDataModel(
                    StrategyType.PRESS_FREQUENCY,
                    resources.getString(R.string.press_frequency),
                    resources.getString(R.string.always_press),
                    resources.getString(R.string.never_press),
                    if (isInGame) coach.pressFrequencyGame else coach.pressFrequency
                ),
                StrategyDataModel(
                    StrategyType.PRESS_AGGRESSION,
                    resources.getString(R.string.press_aggression),
                    resources.getString(R.string.more_aggressive),
                    resources.getString(R.string.less_aggressive),
                    if (isInGame) coach.pressAggressionGame else coach.pressAggression
                )
            )
        }
    }
}