package com.appdev.jphil.basketballcoach.startscreen

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.compose.arch.BasicComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Event
import kotlinx.coroutines.launch

class StartScreenPresenter(
    private val startScreenRepository: StartScreenRepository
) : BasicComposePresenter<StartScreenContract.DataState>(),
    StartScreenContract.Interactor {

    override val initialDataState = StartScreenContract.DataState()

    init {
        viewModelScope.launch {
            val doesGameExist = startScreenRepository.doesGameExist()
            updateState { copy(showLoadGame = doesGameExist) }
        }
    }

    override fun onStartNewGame() {
        sendEvent(CreateNewGameEvent)
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

    object CreateNewGameEvent : Event
}
