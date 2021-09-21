package com.appdev.jphil.basketballcoach.stats

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.compose.arch.ComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Event
import com.appdev.jphil.basketballcoach.compose.arch.Transformer
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import kotlinx.coroutines.launch
import javax.inject.Inject

class StatsPresenter(
    transformer: StatsTransformer,
    private val params: Params,
    private val statsRepository: StatsRepository
) : ComposePresenter<StatsContract.DataState, StatsContract.ViewState>(),
    Transformer<StatsContract.DataState, StatsContract.ViewState> by transformer,
    StatsContract.Interactor {

    data class Params @Inject constructor(
        @TeamId val teamId: Int,
        @ConferenceId val conferenceId: Int
    )

    override val initialDataState = StatsContract.DataState()

    init {
        viewModelScope.launch {
            val games = statsRepository.getAllGames()
            val teams = statsRepository.getAllTeams()
            val conferences = statsRepository.getAllConferences()
            updateState {
                copy(
                    tabIndex = 0,
                    conferenceIndex = params.conferenceId,
                    conferences = conferences,
                    teams = teams,
                    games = games
                )
            }
        }
    }

    override fun onConferenceChanged(index: Int) {
        updateState {
            copy(conferenceIndex = index)
        }
    }

    override fun onTabChanged(index: Int) {
        updateState {
            copy(tabIndex = index)
        }
    }

    override fun onTeamClicked(teamId: Int) {
        sendEvent(SwitchTeams(teamId, state.value.teams.first { it.teamId == teamId }.conferenceId))
    }

    data class SwitchTeams(val teamId: Int, val conferenceId: Int) : Event
}
