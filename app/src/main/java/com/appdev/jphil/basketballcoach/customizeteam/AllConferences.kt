package com.appdev.jphil.basketballcoach.customizeteam

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import java.io.Serializable

data class AllConferences(
    val conferences: List<ConferenceGeneratorDataModel>
) : Serializable
