package com.appdev.jphil.basketballcoach.customizeteam

import com.appdev.jphil.basketball.Pronouns
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

interface CustomizeContract {

    interface Interactor {
        fun onUpdateSchoolName(schoolName: String)
        fun onUpdateMascot(mascot: String)
        fun onUpdateAbbreviation(abbreviation: String)
        fun onUpdateTeamRating(rating: Int)
        fun onUpdateLocation(location: Location)
        fun onUpdateCoachFirstName(firstName: String)
        fun onUpdateCoachLastName(lastName: String)
        fun onUpdateCoachPronouns(pronouns: Pronouns)
        fun startNewGame()
    }

    data class DataState(
        val showSpinner: Boolean = false,
        val team: TeamGeneratorDataModel
    ) : com.appdev.jphil.basketballcoach.compose.arch.DataState
}
