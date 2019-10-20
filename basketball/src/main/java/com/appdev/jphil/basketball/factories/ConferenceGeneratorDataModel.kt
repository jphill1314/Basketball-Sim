package com.appdev.jphil.basketball.factories

abstract class ConferenceGeneratorDataModel(
    val name: String,
    val teams: List<TeamGeneratorDataModel>,
    val minRating: Int
)