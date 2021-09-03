package com.appdev.jphil.basketballcoach.selectionshow

import com.appdev.jphil.basketballcoach.basketball.NationalChampionshipHelper

interface SelectionShowContract {

    interface Interactor {
        fun onEnterBracket()
    }

    data class DataState(
        val state: NationalChampionshipHelper.ChampionshipLoadingState
    ) : com.appdev.jphil.basketballcoach.compose.arch.DataState

    data class ViewState(
        val state: NationalChampionshipHelper.ChampionshipLoadingState
    ) : com.appdev.jphil.basketballcoach.compose.arch.ViewState
}
