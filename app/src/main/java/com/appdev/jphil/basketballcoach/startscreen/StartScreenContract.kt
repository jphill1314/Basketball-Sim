package com.appdev.jphil.basketballcoach.startscreen

interface StartScreenContract {

    interface Interactor {
        fun onStartNewGame()
        fun onLoadGame()
    }

    data class DataState(
        val showLoadingScreen: Boolean = false,
        val showLoadGame: Boolean = false
    ) : com.appdev.jphil.basketballcoach.compose.arch.DataState

    data class ViewState(
        val showLoadingScreen: Boolean = false,
        val showLoadGame: Boolean = false
    ) : com.appdev.jphil.basketballcoach.compose.arch.ViewState
}
