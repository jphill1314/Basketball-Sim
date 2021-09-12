package com.appdev.jphil.basketball.factories

abstract class ConferenceGeneratorDataModel(
    val name: String,
    val teams: MutableList<TeamGeneratorDataModel>,
    val minRating: Int
)
