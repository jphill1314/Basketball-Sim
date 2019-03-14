package com.appdev.jphil.basketballcoach.newseason

import android.content.res.Resources
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.factories.TeamFactory
import com.appdev.jphil.basketballcoach.R
import javax.inject.Inject

class NewSeasonRepository @Inject constructor(
    resources: Resources
){

    private val teamFactory = TeamFactory(
        resources.getStringArray(R.array.first_names).asList(),
        resources.getStringArray(R.array.last_names).asList()
    )

    fun startNewSeason() {

    }

    private fun startNewSeasonForTeam(team: Team) {

    }
}