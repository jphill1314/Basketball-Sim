package com.appdev.jphil.basketballcoach.strategy

enum class StrategyType(val isToggle: Boolean) {
    OFFENSE_FAVORS_THREES(false),
    PACE(false),
    AGGRESSION(false),
    DEFENSE_FAVORS_THREES(false),
    PRESS_FREQUENCY(false),
    PRESS_AGGRESSION(false),
    INTENTIONAL_FOUL(true),
    MOVE_QUICKLY(true),
    WASTE_TIME(true)
}