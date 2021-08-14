package com.appdev.jphil.basketball.factories

import kotlin.random.Random

data class TeamGeneratorDataModel(
    val schoolName: String,
    val mascot: String,
    val abbreviation: String,
    val ratingVariance: Int = Random.nextInt(20) - 10
)
