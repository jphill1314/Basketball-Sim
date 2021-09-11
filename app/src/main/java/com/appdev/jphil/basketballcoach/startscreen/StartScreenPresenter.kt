package com.appdev.jphil.basketballcoach.startscreen

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.compose.arch.ComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Event
import com.appdev.jphil.basketballcoach.newseason.NewGameRepository
import kotlinx.coroutines.launch

class StartScreenPresenter(
    private val startScreenRepository: StartScreenRepository,
    private val newGameRepository: NewGameRepository
) : ComposePresenter<StartScreenContract.DataState, StartScreenContract.ViewState>(),
    StartScreenContract.Interactor {

    override val initialDataState = StartScreenContract.DataState()

    init {
        viewModelScope.launch {
            val doesGameExist = startScreenRepository.doesGameExist()
            updateState { copy(showLoadGame = doesGameExist) }
        }
    }

    override fun transform(dataState: StartScreenContract.DataState) = StartScreenContract.ViewState(
        showLoadGame = dataState.showLoadGame,
        showLoadingScreen = dataState.showLoadingScreen
    )

    override fun onStartNewGame() {
        updateState { copy(showLoadingScreen = true) }
        viewModelScope.launch {
            newGameRepository.generateNewGame()
            val userTeam = startScreenRepository.getUserTeamEntity()
            sendEvent(StartGameEvent(userTeam.teamId, userTeam.conferenceId))
        }
    }

    override fun onLoadGame() {
        viewModelScope.launch {
            val userTeam = startScreenRepository.getUserTeamEntity()
            sendEvent(StartGameEvent(userTeam.teamId, userTeam.conferenceId))
        }
    }

    data class StartGameEvent(
        val userId: Int,
        val conferenceId: Int
    ) : Event
}
