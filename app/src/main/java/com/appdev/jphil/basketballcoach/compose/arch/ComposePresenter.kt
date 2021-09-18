package com.appdev.jphil.basketballcoach.compose.arch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class ComposePresenter<DS : DataState, VS : ViewState> :
    BasicComposePresenter<DS>(),
    Transformer<DS, VS> {

    lateinit var viewState: StateFlow<VS>

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        viewModelScope.launch {
            viewState = dataState.map { transform(it) }.stateIn(this)
        }
    }

    abstract override fun transform(dataState: DS): VS
}
