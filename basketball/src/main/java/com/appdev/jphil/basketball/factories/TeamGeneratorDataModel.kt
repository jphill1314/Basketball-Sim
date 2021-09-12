package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.location.Location

data class TeamGeneratorDataModel(
    val schoolName: String,
    val mascot: String,
    val abbreviation: String,
    val location: Location,
    val isUser: Boolean = false,
    val rating: Int,
)
