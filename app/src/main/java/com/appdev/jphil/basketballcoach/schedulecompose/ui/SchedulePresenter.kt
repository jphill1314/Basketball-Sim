package com.appdev.jphil.basketballcoach.schedulecompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleRepository
import com.appdev.jphil.basketballcoach.simulation.GameSimRepository2
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
        simState = null,
        teamId = params.teamId,
        dataModels = emptyList(),
        dialogDataModels = emptyList()
    )

    private var dialogJob: Job? = null

    private val _state = MutableStateFlow(initialState)
    lateinit var state: StateFlow<ScheduleContract.ScheduleViewState>

    private val _events = MutableSharedFlow<ScheduleContract.ScheduleEvent>()
    val events = _events.asSharedFlow()

    init {
        collectData()
        viewModelScope.launch {
            state = _state.map { transformer.transformDataModels(it) }.stateIn(this)
        }
    }

    private fun collectData() {
        viewModelScope.launch {
            scheduleRepository.getGames().collect { dataModels ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        dataModels = dataModels
                    )
                }
            }
        }
        viewModelScope.launch {
            gameSimRepository.simState.collect { simState ->
                _state.update {
                    it.copy(simState = simState)
                }
            }
        }
    }

    override fun toggleShowButtons(uiModel: ScheduleUiModel) {
        if (uiModel.isFinal) return
        _state.update {
            it.copy(
                selectedGameId = if (it.selectedGameId == uiModel.id) -1 else uiModel.id
            )
        }
    }

    override fun simulateGame(uiModel: ScheduleUiModel) {
        _state.update { it.copy(selectedGameId = -1) }
        launchDialog()
        gameSimRepository.simulateUpToAndIncludingGame(uiModel.id)
    }

    override fun playGame(uiModel: ScheduleUiModel) {
        _state.update {
            it.copy(
                selectedGameId = -1,
                gameToPlay = uiModel
            )
        }

        launchDialog()
        gameSimRepository.simulateUpToGame(uiModel.id)
    }

    override fun onDismissSimDialog() {
        dialogJob?.cancel()
        gameSimRepository.cancelSimulation()
        _state.update {
            it.copy(
                simState = null,
                dialogDataModels = emptyList()
            )
        }
    }

    override fun onStartGame(uiModel: ScheduleUiModel) {
        _state.update {
            it.copy(
                simState = null,
                dialogDataModels = emptyList()
            )
        }
        viewModelScope.launch {
            _events.emit(NavigateToGame(uiModel))
        }
    }

    override fun openTournament(tournamentId: Int) {
        if (tournamentId < 0) {
            launchDialog()
            gameSimRepository.simulateUntilConferenceTournaments()
        } else {
            viewModelScope.launch {
                _events.emit(NavigateToTournament)
            }
        }
    }

    private fun launchDialog() {
        dialogJob?.cancel()
        dialogJob = viewModelScope.launch {
            scheduleRepository.getGamesForDialog().collect { dataModels ->
                _state.update {
                    it.copy(dialogDataModels = dataModels)
                }
            }
        }
    }

    data class NavigateToGame(
        val gameModel: ScheduleUiModel
    ) : ScheduleContract.ScheduleEvent

    object NavigateToTournament : ScheduleContract.ScheduleEvent
}
