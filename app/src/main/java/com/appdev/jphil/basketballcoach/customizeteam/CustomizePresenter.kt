package com.appdev.jphil.basketballcoach.customizeteam

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketball.Pronouns
import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import com.appdev.jphil.basketballcoach.compose.arch.BasicComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Event
import com.appdev.jphil.basketballcoach.newgame.NewGameRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class CustomizePresenter(
    private val params: Params,
    private val newGameRepository: NewGameRepository
) : BasicComposePresenter<CustomizeContract.DataState>(),
    CustomizeContract.Interactor {

    data class Params @Inject constructor(
        @Named("initialTeam") val initialTeam: TeamGeneratorDataModel,
        @Named("conferences") val conferences: List<ConferenceGeneratorDataModel>
    )

    override val initialDataState = CustomizeContract.DataState(team = params.initialTeam)

    override fun onUpdateSchoolName(schoolName: String) {
        updateState {
            CustomizeContract.DataState(team = team.copy(schoolName = schoolName))
        }
    }

    override fun onUpdateMascot(mascot: String) {
        updateState {
            CustomizeContract.DataState(team = team.copy(mascot = mascot))
        }
    }

    override fun onUpdateAbbreviation(abbreviation: String) {
        updateState {
            CustomizeContract.DataState(team = team.copy(abbreviation = abbreviation))
        }
    }

    override fun onUpdateTeamRating(rating: Int) {
        updateState {
            CustomizeContract.DataState(team = team.copy(rating = rating))
        }
    }

    override fun onUpdateLocation(location: Location) {
        updateState {
            CustomizeContract.DataState(team = team.copy(location = location))
        }
    }

    override fun onUpdateCoachFirstName(firstName: String) {
        updateState {
            CustomizeContract.DataState(team = team.copy(headCoachFirstName = firstName))
        }
    }

    override fun onUpdateCoachLastName(lastName: String) {
        updateState {
            CustomizeContract.DataState(team = team.copy(headCoachLastName = lastName))
        }
    }

    override fun onUpdateCoachPronouns(pronouns: Pronouns) {
        updateState {
            CustomizeContract.DataState(team = team.copy(headCoachPronouns = pronouns))
        }
    }

    override fun startNewGame() {
        updateState { copy(showSpinner = true) }
        viewModelScope.launch {
            val newConferences = params.conferences
            newConferences.forEach { conference ->
                if (conference.teams.any { it.isUser }) {
                    val oldUser = conference.teams.first { it.isUser }
                    conference.teams.remove(oldUser)
                    conference.teams.add(state.value.team)
                }
            }

            newGameRepository.deleteExistingGame()
            newGameRepository.generateNewGame(newConferences)
            val userTeam = newGameRepository.getUserTeamEntity()
            sendEvent(StartNewGame(userTeam.teamId, userTeam.conferenceId))
        }
    }

    data class StartNewGame(val teamId: Int, val conferenceId: Int) : Event
}
