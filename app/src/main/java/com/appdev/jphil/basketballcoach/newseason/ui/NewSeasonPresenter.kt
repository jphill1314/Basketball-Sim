package com.appdev.jphil.basketballcoach.newseason.ui

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.compose.arch.ComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Event
import com.appdev.jphil.basketballcoach.newseason.NewSeasonRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewSeasonPresenter(
    private val newSeasonRepository: NewSeasonRepository
) : ComposePresenter<
        NewSeasonContract.NewSeasonDataState,
        NewSeasonContract.NewSeasonViewState>(),
        NewSeasonContract.Interactor {

    override val initialDataState = NewSeasonContract.NewSeasonDataState()

    init {
        viewModelScope.launch {
            newSeasonRepository.state.collect {
                updateState {
                    NewSeasonContract.NewSeasonDataState(
                        stepNumber = it.stepNumber,
                        isDeletingGames = it.isDeletingOldGames,
                        totalTeams = it.totalTeams,
                        teamsUpdated = it.teamsUpdated,
                        isGeneratingGames = it.isGeneratingGames,
                        isGeneratingRecruits = it.isGeneratingRecruits
                    )
                }
            }
        }
        viewModelScope.launch {
            newSeasonRepository.startNewSeason()
        }
    }

    override fun transform(
        dataState: NewSeasonContract.NewSeasonDataState
    ): NewSeasonContract.NewSeasonViewState {
        return NewSeasonContract.NewSeasonViewState(
            uiModels = listOf(
                NewSeasonModel(
                    currentStepCount = dataState.stepNumber,
                    isDeletingGames = dataState.isDeletingGames,
                    numberOfTeamsUpdated = dataState.teamsUpdated,
                    numberOfTeamsToUpdate = dataState.totalTeams,
                    isGeneratingNewGames = dataState.isGeneratingGames,
                    isGeneratingRecruits = dataState.isGeneratingRecruits
                )
            ) + if (dataState.stepNumber > 4) {
                listOf(StartNewSeasonModel)
            } else {
                emptyList()
            }
        )
    }

    override fun startNewSeason() {
        sendEvent(StatNewSeasonEvent)
    }

    object StatNewSeasonEvent : Event
}
