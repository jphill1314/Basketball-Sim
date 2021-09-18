package com.appdev.jphil.basketball.factories

import java.io.Serializable

abstract class ConferenceGeneratorDataModel(
    val name: String,
    val teams: MutableList<TeamGeneratorDataModel>,
    val minRating: Int
) : Serializable
