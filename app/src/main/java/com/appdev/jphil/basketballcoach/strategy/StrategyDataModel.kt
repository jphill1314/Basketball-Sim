package com.appdev.jphil.basketballcoach.strategy

import android.content.res.Resources
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.R

data class StrategyDataModel(
    var type: StrategyType,
    var title: String,
    var higher: String,
    var lower: String,
    var value: Int,
    val max: Int = 100,
    var isEnabled: Boolean = false
) {

    companion object {
        fun generateDataModels(coach: Coach, resources: Resources, isInGame: Boolean): List<StrategyDataModel> {
            val dataModels = mutableListOf(
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

            if (isInGame) {
                dataModels.add(
                    0,
                    StrategyDataModel(
                        StrategyType.INTENTIONAL_FOUL,
                        resources.getString(R.string.intentionally_foul),
                        "",
                        "",
                        0,
                        0,
                        coach.intentionallyFoul
                    )
                )
                dataModels.add(
                    1,
                    StrategyDataModel(
                        StrategyType.MOVE_QUICKLY,
                        resources.getString(R.string.move_quickly),
                        "",
                        "",
                        0,
                        0,
                        coach.shouldHurry
                    )
                )
                dataModels.add(
                    2,
                    StrategyDataModel(
                        StrategyType.WASTE_TIME,
                        resources.getString(R.string.waste_time),
                        "",
                        "",
                        0,
                        0,
                        coach.shouldWasteTime
                    )
                )
            }

            return dataModels
        }
    }
}
