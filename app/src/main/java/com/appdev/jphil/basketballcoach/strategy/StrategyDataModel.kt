package com.appdev.jphil.basketballcoach.strategy

data class StrategyDataModel(
    val pace: Int,
    val offenseFavorsThrees: Int,
    val aggression: Int,
    val defenseFavorsThrees: Int,
    val pressFrequency: Int,
    val pressAggression: Int
)