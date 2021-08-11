package com.appdev.jphil.basketballcoach.schedulecompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleDataModel
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleRepository
import com.appdev.jphil.basketballcoach.simulation.GameSimRepository2
import com.flurry.sdk.it
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SchedulePresenter(
    private val params: Params,
    private val transformer: ScheduleTransformer,
    private val scheduleRepository: ScheduleRepository,
    private val gameSimRepository: GameSimRepository2
) : ViewModel(), ScheduleContract.ScheduleInteractor {

    data class Params @Inject constructor(
        @TeamId val teamId: Int
    )

    private val initialState = ScheduleContract.ScheduleDataState(
        isLoading = true,
        selectedGameId = -1,
        teamId = params.teamId,
        dataModels = emptyList()
    )

    private val _state = MutableStateFlow(initialState)
    lateinit var state: StateFlow<ScheduleContract.ScheduleViewState>

    init {
        collectSchedule()
        viewModelScope.launch {
            state = _state.map { transformer.transformDataModels(it) }.stateIn(this)
        }
    }

    private fun collectSchedule() {
        viewModelScope.launch {
            scheduleRepository.getGames().collect {
                _state.value = _state.value.copy(
                    isLoading = false,
                    dataModels = it
                )
            }
        }
    }

    override fun toggleShowButtons(uiModel: ScheduleUiModel) {
        val currentState = _state.value
        _state.value = currentState.copy(
            selectedGameId = if (currentState.selectedGameId == uiModel.id) -1 else uiModel.id
        )
    }

    override fun simulateGame(uiModel: ScheduleUiModel) {
        gameSimRepository.simulateUpToAndIncludingGame(uiModel.id)
    }

    override fun playGame(uiModel: ScheduleUiModel) {

    }
}