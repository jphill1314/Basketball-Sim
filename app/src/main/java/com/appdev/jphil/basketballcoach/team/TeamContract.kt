package com.appdev.jphil.basketballcoach.team

import com.appdev.jphil.basketball.teams.Team

interface TeamContract {

    interface Interactor {
        fun onPlayerSelected(index: Int)
    }

    data class ViewState(
        val team: Team? = null,
        val selectedPlayerIndex: Int = -1
    ) : com.appdev.jphil.basketballcoach.compose.arch.ViewState
}
