package com.appdev.jphil.basketballcoach.tournamentcompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import com.appdev.jphil.basketballcoach.schedulecompose.ui.ScheduleUiModel
import com.appdev.jphil.basketballcoach.simulation.TournamentSimRepository
import com.appdev.jphil.basketballcoach.tournamentcompose.data.TournamentRepository
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TournamentPresenter(
    private val params: Params,
    private val transformer: TournamentTransformer,
    private val tournamentRepository: TournamentRepository,
    private val tournamentSimRepository: TournamentSimRepository
) : ViewModel(), TournamentContract.TournamentInteractor {

    data class Params @Inject constructor(
        @ConferenceId val conferenceId: Int,
        @Named("testing") val isTournamentExisting: Boolean
    )

    private val initialState = TournamentContract.TournamentDataState(
        conferenceId = params.conferenceId
    )

    private var dialogJob: Job? = null

    private val _state = MutableStateFlow(initialState)
    lateinit var state: StateFlow<TournamentContract.TournamentViewState>

    init {
        collectData()
        viewModelScope.launch {
            state = _state.map { transformer.transformDataModels(it) }.stateIn(this)
        }
    }

    private fun collectData() {
        viewModelScope.launch {
            if (!params.isTournamentExisting) {
                tournamentRepository.generateTournaments(params.conferenceId)
            }
            tournamentRepository.getGamesForTournament(params.conferenceId).collect { dataModels ->
                _state.update {
                    it.copy(
                        isLoading = dataModels.isEmpty(),
                        dataModels = dataModels
                    )
                }
            }
        }
        viewModelScope.launch {
            tournamentSimRepository.simState.collect { simState ->
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
        tournamentSimRepository.simulateGame(
            params.conferenceId,
            uiModel.id
        )
    }

    override fun playGame(uiModel: ScheduleUiModel) {
        // TODO: prevent user from playing wrong game
        _state.update {
            it.copy(
                selectedGameId = -1,
                gameToPlay = uiModel
            )
        }

        launchDialog()
        tournamentSimRepository.simulateToGame(
            params.conferenceId,
            uiModel.id
        )
    }

    override fun onDismissSimDialog() {
        dialogJob?.cancel()
        tournamentSimRepository.cancelSimulation()
        _state.update {
            it.copy(
                simState = null,
                dialogDataModels = emptyList()
            )
        }
    }

    override fun onStartGame(uiModel: ScheduleUiModel) {
        TODO("Not yet implemented")
    }

    private fun launchDialog() {
        dialogJob?.cancel()
        dialogJob = viewModelScope.launch {
            tournamentRepository.getGamesForDialog().collect { dataModels ->
                _state.update {
                    it.copy(dialogDataModels = dataModels)
                }
            }
        }
    }
}
