package com.appdev.jphil.basketballcoach.recruitingcompose

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team

interface RecruitingContract {

    interface Interactor :
        RecruitModel.Interactor

    data class DataState(
        val team: Team? = null,
        val sortType: SortType = SortType.BEST_FIRST,
        val recruits: List<Recruit> = emptyList()
    ) : com.appdev.jphil.basketballcoach.compose.arch.DataState

    data class ViewState(
        val isLoading: Boolean,
        val team: TeamStateModel? = null,
        val recruits: List<RecruitModel> = emptyList()
    ) : com.appdev.jphil.basketballcoach.compose.arch.ViewState
}

enum class SortType {
    BEST_FIRST,
    BEST_LAST,
    MOST_INTEREST,
    LEAST_INTEREST,
    MOST_INTERACTION,
    LEAST_INTERACTION
}
