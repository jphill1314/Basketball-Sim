package com.appdev.jphil.basketballcoach.recruiting.ui

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.compose.arch.ComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Event
import com.appdev.jphil.basketballcoach.compose.arch.Transformer
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.recruiting.data.RecruitingRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecruitingPresenter(
    transformer: RecruitingTransformer,
    private val params: Params,
    private val recruitingRepository: RecruitingRepository
) : ComposePresenter<RecruitingContract.DataState, RecruitingContract.ViewState>(),
    RecruitingContract.Interactor,
    Transformer<RecruitingContract.DataState, RecruitingContract.ViewState> by transformer {

    override val initialDataState = RecruitingContract.DataState()

    data class Params @Inject constructor(
        @TeamId val teamId: Int
    )

    init {
        viewModelScope.launch {
            recruitingRepository.getRecruitingData(params.teamId).collect {
                updateState { copy(team = it.team, recruits = it.recruits) }
            }
        }
    }

    override fun onRecruitClicked(id: Int) {
        sendEvent(LaunchRecruitOverview(id))
    }

    fun sortRatingHigh() {
        updateState { copy(sortType = SortType.BEST_FIRST) }
    }

    fun sortRatingLow() {
        updateState { copy(sortType = SortType.BEST_LAST) }
    }

    fun sortInterestHigh() {
        updateState { copy(sortType = SortType.MOST_INTEREST) }
    }

    fun sortInterestLow() {
        updateState { copy(sortType = SortType.LEAST_INTEREST) }
    }

    fun sortInteractionsMost() {
        updateState { copy(sortType = SortType.MOST_INTERACTION) }
    }

    fun sortInteractionsLeast() {
        updateState { copy(sortType = SortType.LEAST_INTERACTION) }
    }

    override fun onPositionClicked(pos: Int) {
        val filters = dataState.value.positionFilters.toMutableList()
        if (filters.contains(pos)) {
            filters.remove(pos)
        } else {
            filters.add(pos)
        }
        updateState { copy(positionFilters = filters) }
    }

    override fun onShowRecruitingClicked() {
        updateState {
            copy(onlyShowRecruiting = !onlyShowRecruiting)
        }
    }

    override fun clearFilters() {
        updateState {
            copy(
                positionFilters = emptyList(),
                onlyShowRecruiting = false
            )
        }
    }

    data class LaunchRecruitOverview(val recruitId: Int) : Event
}
