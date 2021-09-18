package com.appdev.jphil.basketballcoach.tournament.ui

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.compose.arch.ComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Transformer
import com.appdev.jphil.basketballcoach.simulation.TournamentSimRepository
import com.appdev.jphil.basketballcoach.tournament.data.TournamentRepository
import timber.log.Timber
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class TournamentPresenter(
    private val params: Params,
    private val transformer: TournamentTransformer,
    private val tournamentRepository: TournamentRepository,
    private val tournamentSimRepository: TournamentSimRepository
) : ComposePresenter<TournamentContract.TournamentDataState, TournamentContract.TournamentViewState>(),
    Transformer<TournamentContract.TournamentDataState, TournamentContract.TournamentViewState> by transformer,
    TournamentContract.TournamentInteractor {

    data class Params @Inject constructor(
        @Named("TournamentId") val conferenceId: Int,
        @Named("DoesTournamentExist") val isTournamentExisting: Boolean
    )

    override val initialDataState = TournamentContract.TournamentDataState(
        conferenceId = params.conferenceId
    )

    private var dialogJob: Job? = null

    init {
        collectData()
    }

    private fun collectData() {
        viewModelScope.launch {
            Timber.d("Tournament exists: ${params.isTournamentExisting}")
            if (!params.isTournamentExisting) {
                tournamentRepository.generateTournaments(params.conferenceId)
            }
            tournamentRepository.getGamesForTournament(params.conferenceId).collect { dataModels ->
                updateState {
                    copy(
                        isLoading = dataModels.isEmpty(),
                        dataModels = dataModels
                    )
                }
            }
        }
        viewModelScope.launch {
            tournamentSimRepository.simState.collect { simState ->
                updateState {
                    copy(simState = simState)
                }
            }
        }
        viewModelScope.launch {
            val type = tournamentRepository.getTournamentType(params.conferenceId)
            updateState {
                copy(
                    tournamentType = type
                )
            }
        }
    }

    override fun toggleShowButtons(uiModel: TournamentGameUiModel) {
        if (uiModel.isFinal) return
        updateState {
            copy(
                selectedGameId = if (selectedGameId == uiModel.id) -1 else uiModel.id
            )
        }
    }

    override fun simulateGame(uiModel: TournamentGameUiModel) {
        updateState { copy(selectedGameId = -1) }
        launchDialog()
        tournamentSimRepository.simulateGame(
            params.conferenceId,
            uiModel.id
        )
    }

    override fun playGame(uiModel: TournamentGameUiModel) {
        // TODO: prevent user from playing wrong game
        updateState {
            copy(
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
        updateState {
            copy(
                simState = null,
                dialogDataModels = emptyList()
            )
        }
    }

    override fun onStartGame(uiModel: TournamentGameUiModel) {
        TODO("Not yet implemented")
    }

    private fun launchDialog() {
        dialogJob?.cancel()
        dialogJob = viewModelScope.launch {
            tournamentRepository.getGamesForDialog().collect { dataModels ->
                updateState {
                    copy(dialogDataModels = dataModels)
                }
            }
        }
    }
}
