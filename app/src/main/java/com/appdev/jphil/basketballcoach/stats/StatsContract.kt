package com.appdev.jphil.basketballcoach.stats

import com.appdev.jphil.basketballcoach.compose.arch.UiModel
import com.appdev.jphil.basketballcoach.database.conference.ConferenceEntity
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

interface StatsContract {

    interface Interactor : TeamStandingModel.Interactor {
        fun onConferenceChanged(index: Int)
        fun onTabChanged(index: Int)
    }

    data class DataState(
        val tabIndex: Int = -1,
        val conferenceIndex: Int = 0,
        val conferences: List<ConferenceEntity> = emptyList(),
        val teams: List<TeamEntity> = emptyList(),
        val games: List<GameEntity> = emptyList()
    ) : com.appdev.jphil.basketballcoach.compose.arch.DataState

    data class ViewState(
        val isLoading: Boolean,
        val tabIndex: Int,
        val uiModels: List<UiModel>,
        val conferenceName: String,
        val dropdownOptions: List<String>
    ) : com.appdev.jphil.basketballcoach.compose.arch.ViewState
}
