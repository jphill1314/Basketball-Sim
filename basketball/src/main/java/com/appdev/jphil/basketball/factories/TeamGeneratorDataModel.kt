package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.Pronouns
import com.appdev.jphil.basketball.location.Location
import java.io.Serializable

data class TeamGeneratorDataModel(
    val schoolName: String,
    val mascot: String,
    val abbreviation: String,
    val location: Location,
    val isUser: Boolean = false,
    val rating: Int,
    val headCoachFirstName: String? = null,
    val headCoachLastName: String? = null,
    val headCoachPronouns: Pronouns = Pronouns.HE
) : Serializable
