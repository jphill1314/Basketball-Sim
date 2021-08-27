package com.appdev.jphil.basketballcoach.schedule.ui

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.compose.arch.ComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Event
import com.appdev.jphil.basketballcoach.compose.arch.Transformer
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.newseason.NewSeasonRepository
import com.appdev.jphil.basketballcoach.schedule.data.ScheduleRepository
import com.appdev.jphil.basketballcoach.simulation.GameSimRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SchedulePresenter(
    private val params: Params,
    private val transformer: ScheduleTransformer,
    private val scheduleRepository: ScheduleRepository,
    private val gameSimRepository: GameSimRepository,
    private val newSeasonRepository: NewSeasonRepository
) : ComposePresenter<ScheduleContract.ScheduleDataState, ScheduleContract.ScheduleViewState>(),
    Transformer<ScheduleContract.ScheduleDataState, ScheduleContract.ScheduleViewState> by transformer,
    ScheduleContract.ScheduleInteractor {

    data class Params @Inject constructor(
        @TeamId val teamId: Int,
        @ConferenceId val conferenceId: Int
    )

    override val initialDataState = ScheduleContract.ScheduleDataState(
        isLoading = true,
        selectedGameId = -1,
        isTournamentExisting = false,
        simState = null,
        teamId = params.teamId,
        dataModels = emptyList(),
        dialogDataModels = emptyList()
    )

    private var dialogJob: Job? = null

    init {
        collectData()
    }

    private fun collectData() {
        viewModelScope.launch {
            scheduleRepository.getGames().collect { dataModels ->
                updateState {
                    copy(
                        isLoading = false,
                        dataModels = dataModels
                    )
                }
            }
        }
        viewModelScope.launch {
            gameSimRepository.simState.collect { simState ->
                updateState {
                    copy(simState = simState)
                }
            }
        }
        viewModelScope.launch {
            scheduleRepository.isSeasonFinished().collect { isFinished ->
                updateState {
                    copy(isSeasonOver = isFinished)
                }
            }
        }
        viewModelScope.launch {
            scheduleRepository.areAllGamesComplete().collect { areAllComplete ->
                updateState {
                    copy(areAllGamesFinal = areAllComplete)
                }
            }
        }
        viewModelScope.launch {
            val exists = scheduleRepository.doesTournamentExistForConference(
                conferenceId = params.conferenceId
            )
            updateState { copy(isTournamentExisting = exists) }
        }
    }

    override fun toggleShowButtons(uiModel: ScheduleUiModel) {
        if (uiModel.isFinal) return
        updateState {
            copy(
                selectedGameId = if (selectedGameId == uiModel.id) -1 else uiModel.id
            )
        }
    }

    override fun simulateGame(uiModel: ScheduleUiModel) {
        updateState { copy(selectedGameId = -1) }
        launchDialog()
        gameSimRepository.simulateUpToAndIncludingGame(uiModel.id)
    }

    override fun playGame(uiModel: ScheduleUiModel) {
        updateState {
            copy(
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
        if (dataState.value.simState?.isSimulatingSeason == true) {
            sendEvent(NavigateToTournament(false))
        }
        updateState {
            copy(
                simState = null,
                dialogDataModels = emptyList()
            )
        }
    }

    override fun onStartGame(uiModel: ScheduleUiModel) {
        updateState {
            copy(
                simState = null,
                dialogDataModels = emptyList()
            )
        }
        sendEvent(NavigateToGame(uiModel))
    }

    override fun openTournament(isExisting: Boolean) {
        if (isExisting || dataState.value.areAllGamesFinal) {
            sendEvent(NavigateToTournament(true))
        } else {
            launchDialog()
            gameSimRepository.simulateUntilConferenceTournaments()
        }
    }

    private fun launchDialog() {
        dialogJob?.cancel()
        dialogJob = viewModelScope.launch {
            scheduleRepository.getGamesForDialog().collect { dataModels ->
                updateState {
                    copy(dialogDataModels = dataModels)
                }
            }
        }
    }

    override fun startNewSeason() {
        updateState { copy(isLoading = true) }
        viewModelScope.launch {
            newSeasonRepository.startNewSeason()
        }
    }

    data class NavigateToGame(
        val gameModel: ScheduleUiModel
    ) : Event

    data class NavigateToTournament(
        val isTournamentExisting: Boolean
    ) : Event
}
