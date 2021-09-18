package com.appdev.jphil.basketballcoach.newgame

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

interface NewGameContract {

    interface Interactor {
        fun onTeamClicked(team: TeamGeneratorDataModel)
    }

    data class DataState(
        val conferences: List<ConferenceGeneratorDataModel>
    ) : com.appdev.jphil.basketballcoach.compose.arch.DataState
}
