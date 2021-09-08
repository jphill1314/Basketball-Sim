package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

data class TeamGeneratorDataModel(
    val schoolName: String,
    val mascot: String,
    val abbreviation: String,
    val location: Location,
    val ratingVariance: Int = Random.nextInt(20) - 10
)
