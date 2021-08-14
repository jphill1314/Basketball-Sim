package com.appdev.jphil.basketballcoach.compose.arch

import androidx.lifecycle.ViewModel

abstract class ComposePresenter : ViewModel() {

    abstract val initialViewState: ViewState

}