package com.appdev.jphil.basketballcoach.newgame

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketballcoach.basketball.conferences.AtlanticAthleticAssociation
import com.appdev.jphil.basketballcoach.basketball.conferences.CaliforniaConference
import com.appdev.jphil.basketballcoach.basketball.conferences.CanadianAthleticConference
import com.appdev.jphil.basketballcoach.basketball.conferences.DesertConference
import com.appdev.jphil.basketballcoach.basketball.conferences.GreatLakesConference
import com.appdev.jphil.basketballcoach.basketball.conferences.GulfCoastConference
import com.appdev.jphil.basketballcoach.basketball.conferences.MiddleAmericaConference
import com.appdev.jphil.basketballcoach.basketball.conferences.MountainAthleticAssociation
import com.appdev.jphil.basketballcoach.basketball.conferences.NortheasternAthleticAssociation
import com.appdev.jphil.basketballcoach.basketball.conferences.TobaccoConference
import com.appdev.jphil.basketballcoach.basketball.conferences.WesternConference
import com.appdev.jphil.basketballcoach.compose.arch.ComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Event
import kotlinx.coroutines.launch

class NewGamePresenter(
    private val newGameRepository: NewGameRepository
) : ComposePresenter<NewGameContract.DataState, NewGameContract.ViewState>(),
    NewGameContract.Interactor {

    override val initialDataState = NewGameContract.DataState(
        conferences = listOf(
            NortheasternAthleticAssociation(70),
            GreatLakesConference(75),
            GulfCoastConference(70),
            TobaccoConference(55),
            AtlanticAthleticAssociation(75),
            MountainAthleticAssociation(60),
            MiddleAmericaConference(55),
            CaliforniaConference(65),
            DesertConference(60),
            WesternConference(55),
            CanadianAthleticConference(50)
        )
    )

    override fun transform(dataState: NewGameContract.DataState) = NewGameContract.ViewState(
        showLoadingScreen = dataState.showLoadingScreen,
        conferences = dataState.conferences
    )

    override fun onTeamClicked(team: TeamGeneratorDataModel) {
        updateState { copy(showLoadingScreen = true) }
        val newConferences = dataState.value.conferences.map { conference ->
            if (conference.teams.contains(team)) {
                conference.teams.remove(team)
                conference.teams.add(team.copy(isUser = true))
            }
            conference
        }
        viewModelScope.launch {
            newGameRepository.deleteExistingGame()
            newGameRepository.generateNewGame(newConferences)
            val userTeam = newGameRepository.getUserTeamEntity()
            sendEvent(StartNewGame(userTeam.teamId, userTeam.conferenceId))
        }
    }

    data class StartNewGame(val teamId: Int, val conferenceId: Int) : Event
}
