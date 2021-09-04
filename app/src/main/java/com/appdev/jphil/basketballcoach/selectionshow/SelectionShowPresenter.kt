package com.appdev.jphil.basketballcoach.selectionshow

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.basketball.NationalChampionshipHelper
import com.appdev.jphil.basketballcoach.compose.arch.ComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Event
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SelectionShowPresenter(
    private val nationalChampionshipHelper: NationalChampionshipHelper
) : ComposePresenter<SelectionShowContract.DataState, SelectionShowContract.ViewState>(),
    SelectionShowContract.Interactor {

    override val initialDataState = SelectionShowContract.DataState(
        nationalChampionshipHelper.state.value
    )

    init {
        viewModelScope.launch {
            nationalChampionshipHelper.state.collect {
                updateState { copy(state = it) }
            }
        }
        viewModelScope.launch {
            nationalChampionshipHelper.createNationalChampionship()
        }
    }

    override fun transform(
        dataState: SelectionShowContract.DataState
    ) = SelectionShowContract.ViewState(dataState.state)

    override fun onEnterBracket() {
        sendEvent(EnterTournamentEvent)
    }

    object EnterTournamentEvent : Event
}
